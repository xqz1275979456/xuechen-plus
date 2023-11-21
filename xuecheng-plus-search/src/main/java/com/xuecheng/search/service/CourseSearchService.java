package com.xuecheng.search.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.search.dto.SearchCourseParamDto;
import com.xuecheng.search.dto.SearchPageResultDto;
import com.xuecheng.search.po.CourseIndex;


/**
 * 课程搜索service
 *
 * @author xuqizheng
 * @date 2023/11/20
 */
public interface CourseSearchService {


    /**
     * 搜索课程列表
     *
     * @param pageParams           分页参数
     * @param searchCourseParamDto 搜索条件
     * @return {@link SearchPageResultDto}<{@link CourseIndex}> 课程列表
     */
    SearchPageResultDto<CourseIndex> queryCoursePubIndex(PageParams pageParams, SearchCourseParamDto searchCourseParamDto);

 }
