package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    CourseMarketMapper courseMarketMapper;
    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Transactional
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {
        //拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //根据名称模糊查询,在sql中拼接course_base.name like ‘%值%’
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());
        //根据课程审核状态查询，在sql中拼接course_audit.status=？
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());
        //todo:按课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()),CourseBase::getStatus,courseParamsDto.getPublishStatus());
        //创建page分页参数对象 参数：当前页码，每页记录数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        //开始进行分页查询
        Page<CourseBase> selectPage = courseBaseMapper.selectPage(page, queryWrapper);
        //数据列表
        List<CourseBase> items = selectPage.getRecords();
        //记录数
        long total = selectPage.getTotal();
        PageResult<CourseBase> courseBasePageResult = new PageResult<>(items, total, pageParams.getPageNo(), pageParams.getPageSize());
        return courseBasePageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {

        //参数的合法性校验
        if (StringUtils.isBlank(dto.getName())) {
            XueChengPlusException.cast("课程名称为空");
        }

        if (StringUtils.isBlank(dto.getMt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getSt())) {
            XueChengPlusException.cast("课程分类为空");
        }

        if (StringUtils.isBlank(dto.getGrade())) {
            XueChengPlusException.cast("课程等级为空");
        }

        if (StringUtils.isBlank(dto.getTeachmode())) {
            XueChengPlusException.cast("教育模式为空");
        }

        if (StringUtils.isBlank(dto.getUsers())) {
            XueChengPlusException.cast("适应人群为空");
        }

        if (StringUtils.isBlank(dto.getCharge())) {
            XueChengPlusException.cast("收费规则为空");
        }


        //向课程基本信息表course_base写入数据
        CourseBase courseBase = new CourseBase();
        //将传入的页面参数放入到courseBase中
        BeanUtils.copyProperties(dto,courseBase); //只要属性名一致就可以拷贝
        //审核状态默认为未提交
        courseBase.setAuditStatus("202002");
        //发布状态默认为未发布
        courseBase.setStatus("203001");
        //机构Id
        courseBase.setCompanyId(companyId);
        //添加时间
        courseBase.setCreateDate(LocalDateTime.now());
        //插入课程基本信息表
        int insert = courseBaseMapper.insert(courseBase);
        if (insert<=0){
            XueChengPlusException.cast("新增课程基本信息失败");
        }
        //向课程营销系courese_marke写入数据
        CourseMarket courseMarket=new CourseMarket();
        //课程Id
        Long courseId = courseBase.getId();
        //将页面输入的数据拷贝到courseMarket
        BeanUtils.copyProperties(dto,courseMarket);
        courseMarket.setId(courseId);
        //保存营销信息
        int i = saveCourseMarket(courseMarket);
        if(i<=0){
            XueChengPlusException.cast("保存课程营销信息失败");
        }
        //从数据库中查询课程的详细信息
        return getCourseBaseInfo(courseId);
    }
    //根据查询课程信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){
        //从课程基本信息表查询
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase==null){
            return null;
        }
        //从课程营销表查询
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

        //组装
        CourseBaseInfoDto courseBaseInfoDto=new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if(courseMarket!=null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }
        //通过courseCategoryMapper查询分类信息，将分类名称放入courseBaseInfoDto中
        CourseCategory courseCategoryBySt = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategoryBySt.getName());
        CourseCategory courseCategoryByMt = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategoryByMt.getName());
        return courseBaseInfoDto;
    }

    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {

        //拿到课程id
        Long courseId = editCourseDto.getId();
        //查询课程信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase==null){
            XueChengPlusException.cast("课程不存在");
        }

        //数据合法性校验
        //根据具体业务逻辑校验
        //本机构只能修改本机构的课程
        if (!companyId.equals(courseBase.getCompanyId())){
            XueChengPlusException.cast("本机构只能修改本机构的课程");
        }
        //封装数据
        BeanUtils.copyProperties(editCourseDto,courseBase);
        //修改时间
        courseBase.setChangeDate(LocalDateTime.now());

        //更新数据库
        int i = courseBaseMapper.updateById(courseBase);
        if (i<=0){
            XueChengPlusException.cast("修改课程失败");
        }
        //更新营销信息
        //todo:
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        saveCourseMarket(courseMarket);
        //查询课程信息
        return getCourseBaseInfo(courseId);
    }

    //保存营销信息
    private int saveCourseMarket(CourseMarket courseMarket){
        //参数的合法性校验
        String charge=courseMarket.getCharge();
        if (StringUtils.isEmpty(charge)){
            XueChengPlusException.cast("收费规则为空");
        }
        //如果课程收费，价格没有填写也要抛出异常
        if (charge.equals("201001")){
            if (courseMarket.getPrice()==null||courseMarket.getPrice().floatValue()<=0){
                XueChengPlusException.cast("课程的价格不能为空并且必须大于0");
            }
        }
        //从数据库查询营销信息，存在则更新，不存在则添加
        Long id = courseMarket.getId();
        CourseMarket courseMarket1 = courseMarketMapper.selectById(id);
        if (courseMarket1==null){
            //插入数据库
            return courseMarketMapper.insert(courseMarket);
        }else {
            //将courseMarket拷贝到courseMarket1
            BeanUtils.copyProperties(courseMarket,courseMarket1);
            courseMarket1.setId(courseMarket.getId());
            //更新
            return courseMarketMapper.updateById(courseMarket1);
        }
    }

}
