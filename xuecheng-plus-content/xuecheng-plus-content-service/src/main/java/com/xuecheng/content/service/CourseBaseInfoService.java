package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * 课程信息管理接口
 *
 * @author xuqizheng
 * @date 2023/10/12
 */
public interface CourseBaseInfoService {
    /**
     * 课程分页查询
     *
     * @param pageParams      分页查询参数
     * @param courseParamsDto 查询条件
     * @return {@link PageResult}<{@link CourseBase}> 查询结果
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto);

    /**
     * 新增课程
     *
     * @param companyId    机构Id
     * @param addCourseDto 课程信息
     * @return {@link CourseBaseInfoDto} 课程详细信息
     */
    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);
}
