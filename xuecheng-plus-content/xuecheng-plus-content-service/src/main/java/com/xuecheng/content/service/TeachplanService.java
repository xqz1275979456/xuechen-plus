package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 课程计划管理相关接口
 *
 * @author xuqizheng
 * @date 2023/10/17
 */
public interface TeachplanService {
    /**
     * 根据课程id查询课程计划
     *
     * @param couseId 课程计划id
     * @return {@link List}<{@link TeachplanDto}> 课程计划信息
     */
    public List<TeachplanDto> findTeachplanTree(Long courseId);

    /**
     * 新增/修改/保存课程计划
     *
     * @param saveTeachplanDto
     */
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto);


    /**
     *
     * 删除课程计划章节
     *
     * @param teachplanId
     */
    public void deleteTeachplan(Long teachplanId);

    /**
     * 移动课程计划章节
     *
     * @param Type        移动类型
     * @param  id
     */
    public void moveTeachplan(String Type,Long id);

    /**
     * 课程计划绑定媒资
     *
     * @param bindTeachplanMediaDto
     */
    public void associationMedia(BindTeachplanMediaDto bindTeachplanMediaDto);

    /**
     * 课程计划解绑媒资
     *
     * @param teachPlanId 教学计划id
     * @param mediaId     媒体id
     */
    public void untieassociationMedia(int teachPlanId, String mediaId);
}
