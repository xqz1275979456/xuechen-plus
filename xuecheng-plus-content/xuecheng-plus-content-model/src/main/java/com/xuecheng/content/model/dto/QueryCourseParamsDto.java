package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 课程查询条件模型类
 *
 * @author xuqizheng
 * @date 2023/10/12
 */
@Data
@ToString
public class QueryCourseParamsDto {
    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    //发布状态
    private String publishStatus;

}
