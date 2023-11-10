package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;

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
}
