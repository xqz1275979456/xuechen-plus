package com.xuecheng.search.service;

import com.xuecheng.search.po.CourseIndex;


/**
 * 课程索引service
 *
 * @author xuqizheng
 * @date 2023/11/20
 */
public interface IndexService {


    /**
     * 添加课程索引
     *
     * @param indexName 索引名称
     * @param id        主键
     * @param object    索引对象
     * @return {@link Boolean} true添加成功 false添加失败
     */
    public Boolean addCourseIndex(String indexName,String id,Object object);


    /**
     * 更新课程索引
     *
     * @param indexName 索引名称
     * @param id        主键
     * @param object    索引对象
     * @return {@link Boolean} true表示成功,false失败
     */
    public Boolean updateCourseIndex(String indexName,String id,Object object);


    /**
     * 删除课程索引
     *
     * @param indexName 索引名称
     * @param id        主键
     * @return {@link Boolean} true表示成功,false失败
     */
    public Boolean deleteCourseIndex(String indexName,String id);

}
