package com.xuecheng.content.model.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * 课程类别树dto
 * 课程分类查询模型类
 *
 * @author xuqizheng
 * @date 2023/10/15
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements java.io.Serializable{

    //子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
