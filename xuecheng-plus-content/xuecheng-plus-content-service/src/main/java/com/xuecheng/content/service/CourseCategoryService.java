package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

import java.util.List;

/**
 * 课程分类接口
 *
 * @author xuqizheng
 * @date 2023/10/12
 */
public interface CourseCategoryService {

    /**
     * 课程分类树型结构查询
     *
     * @param id id
     * @return {@link List}<{@link CourseCategoryTreeDto}>
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
