package com.xuecheng.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.base.exception.CommonError;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.content.service.TeachplanService;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * 课程发布相关接口实现
 *
 * @author xuqizheng
 * @date 2023/11/04
 */
@Service
@Slf4j
public class CoursePublishServiceImpl implements CoursePublishService {
    @Autowired
    CourseBaseInfoService courseBaseInfoService;
    @Autowired
    TeachplanService teachplanService;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CoursePublishPreMapper coursePublishPreMapper;
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CoursePublishMapper coursePublishMapper;
    @Autowired
    MqMessageService mqMessageService;
    @Autowired
    MediaServiceClient mediaServiceClient;


    /**
     * 获取课程预览信息
     *
     * @param courseId 课程id
     * @return {@link CoursePreviewDto}
     */
    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        //课程基本信息以及营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        coursePreviewDto.setCourseBase(courseBaseInfo);
        //课程计划信息
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
        coursePreviewDto.setTeachplans(teachplanTree);
        return coursePreviewDto;
    }

    /**
     * 提交审核
     *
     * @param companyId 机构id
     * @param courseId  课程id
     */
    @Transactional
    @Override
    public void commitAudit(Long companyId, Long courseId) {
        //课程基本信息
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        if (courseBaseInfo == null) {
            XueChengPlusException.cast("课程找不到");
        }
        //本机构只能提交本机构的课程
        if (!companyId.equals(courseBaseInfo.getCompanyId())) {
            XueChengPlusException.cast("本机构只能提交本机构的课程");
        }
        //审核状态
        String auditStatus = courseBaseInfo.getAuditStatus();
        //如果课程信息中审核信息为已提交则不允许提交
        if (auditStatus.equals("202003")) {
            XueChengPlusException.cast("课程已提交，请等待审核");
        }
        //课程的图片
        String pic = courseBaseInfo.getPic();
        //课程的图片没有填写不允许提交
        if (StringUtils.isEmpty(pic)) {
            XueChengPlusException.cast("请求上传课程图片");
        }
        //课程计划
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);
        //课程计划没有填写不允许提交
        if (teachplanTree == null || teachplanTree.size() == 0) {
            XueChengPlusException.cast("请编写课程计划");
        }
        //课程预发布表
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        //设置机构Id
        coursePublishPre.setCompanyId(companyId);
        //营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //营销信息转JSON格式
        String courseMarketJson = JSON.toJSONString(courseMarket);
        coursePublishPre.setMarket(courseMarketJson);
        //课程计划信息转JSON
        String teachplanTreeJson = JSON.toJSONString(teachplanTree);
        coursePublishPre.setTeachplan(teachplanTreeJson);
        //状态改为已提交
        coursePublishPre.setStatus("202003");
        //提交时间
        coursePublishPre.setCreateDate(LocalDateTime.now());
        //查询预发布表，如果有记录则更新，没有则插入
        CoursePublishPre coursePublishPreObj = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPreObj == null) {
            //插入
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            //更新
            coursePublishPreMapper.updateById(coursePublishPre);
        }
        //更新课程基本信息表中的审核状态为已提交
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        //审核状态为已提交
        courseBase.setStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }

    /**
     * 课程发布
     *
     * @param companyId 机构id
     * @param courseId  课程id
     */
    @Override
    @Transactional
    public void publish(Long companyId, Long courseId) {
        //查询课程预发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            XueChengPlusException.cast("课程没有审核记录，无法发布");
        }
        //课程状态
        String status = coursePublishPre.getStatus();
        //课程如果没有审核通过不允许发布
        if (!status.equals("202004")) {
            XueChengPlusException.cast("课程没有审核通过，不允许发布");
        }
        //向课程发布表写数据
        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre, coursePublish);
        //先查询课程发布表，有则更新，没则插入
        CoursePublish coursePublishObj = coursePublishMapper.selectById(courseId);
        if (coursePublishObj == null) {
            coursePublishMapper.insert(coursePublish);
        } else {
            coursePublishMapper.updateById(coursePublish);
        }

        //向消息表写数据
        saveCoursePublishMessage(courseId);


        //将预发布表信息删除
        coursePublishPreMapper.deleteById(courseId);


    }

    /**
     * 课程静态化
     *
     * @param courseId 课程id
     * @return {@link File} 静态化文件
     */
    @Override
    public File generateCourseHtml(Long courseId) {
        Configuration configuration = new Configuration(Configuration.getVersion());
        File htmlFile=null;
        try {
            //拿到classpath路径
            String classpath = this.getClass().getResource("/").getPath();
            //指定模板目录
            configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates"));
            //指定编码
            configuration.setDefaultEncoding("utf-8");
            //得到模板
            Template template = configuration.getTemplate("course_template.ftl");
            //模板数据
            CoursePreviewDto coursePreviewInfo = this.getCoursePreviewInfo(courseId);
            HashMap<String, Object> map = new HashMap<>();
            map.put("model", coursePreviewInfo);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            //输入流
            InputStream inputStream = IOUtils.toInputStream(html, "utf-8");
            //临时文件
            htmlFile=File.createTempFile("coursepublish",".html");
            //输出文件
            FileOutputStream outputStream = new FileOutputStream(htmlFile);
            //使用流将html写入文件
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error("页面静态化出现问题,课程id{}",courseId,e);
            e.printStackTrace();
        }
        return htmlFile;
    }

    /**
     * 上传课程静态化文件
     *
     * @param courseId 课程id
     * @param file     静态化文件
     */
    @Override
    public void uploadCourseHtml(Long courseId, File file) {
        //将file转成MultipartFile
        try {
            MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
            UploadFileResultDto upload = mediaServiceClient.upload(multipartFile, "course/" + courseId + ".html");
            if (upload==null){
                log.debug("远程调用走降级逻辑得到上传结果为null，课程id：{}",courseId);
                XueChengPlusException.cast("上传静态文件过程存在异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            XueChengPlusException.cast("上传静态文件过程存在异常");
        }


    }

    //保存消息表记录
    private void saveCoursePublishMessage(Long courseId) {
        MqMessage mqMessage = mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
        if (mqMessage == null) {
            XueChengPlusException.cast(CommonError.UNKOWN_ERROR);
        }
    }

}
