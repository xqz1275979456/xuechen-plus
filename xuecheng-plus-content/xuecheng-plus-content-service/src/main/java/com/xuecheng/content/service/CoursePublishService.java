package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;

import java.io.File;

/**
 * 课程发布相关接口
 *
 * @author xuqizheng
 * @date 2023/11/04
 */
public interface CoursePublishService {

    /**
     * 获取课程预览信息
     *
     * @param courseId 课程id
     * @return {@link CoursePreviewDto}
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * 提交审核
     *
     * @param companyId 机构id
     * @param courseId  课程id
     */
    public void commitAudit(Long companyId,Long courseId);

    /**
     * 课程发布
     *
     * @param companyId 机构id
     * @param courseId  课程id
     */
    public void publish(Long companyId,Long courseId);

    /**
     * 课程静态化
     *
     * @param courseId 课程id
     * @return {@link File} 静态化文件
     */
    public File generateCourseHtml(Long courseId);

    /**
     * 上传课程静态化文件
     *
     * @param courseId 课程id
     * @param file     静态化文件
     */
    public void uploadCourseHtml(Long courseId,File file);

}
