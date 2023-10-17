package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改课程
 *
 * @author xuqizheng
 * @date 2023/10/16
 */
@Data
public class EditCourseDto extends AddCourseDto{
    @ApiModelProperty(value = "课程id",required = true)
    private Long id;

}
