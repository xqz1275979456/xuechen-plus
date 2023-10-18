package com.xuecheng.content.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;
    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    private int getTeachplanCount(Long courseId,Long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper=queryWrapper.eq(Teachplan::getCourseId,courseId).eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count+1;
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //通过课程计划Id判断是否是新增和修改
        Long teachplanId = saveTeachplanDto.getId();
        if (teachplanId==null){
            //新增
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            //确定排序字段，找到同级节点个数，排序字段就是个数加一
            Long parentid = saveTeachplanDto.getParentid();
            Long courseId = saveTeachplanDto.getCourseId();
            teachplan.setOrderby(getTeachplanCount(courseId,parentid));
            teachplanMapper.insert(teachplan);

        }else {
            //修改
            Teachplan teachplan = teachplanMapper.selectById(teachplanId);
            //将参数复制到teachplan
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }
    }
    @Transactional
    @Override
    public void deleteTeachplan(Long teachplanId) {
        //判断是大章节还是小章节
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan==null){
            XueChengPlusException.cast("课程信息不存在");
        }
        //如果是大章节
        if (teachplan.getGrade()==1){
            //判断此章节下是否有小章节
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid,teachplan.getId());
            Integer count = teachplanMapper.selectCount(queryWrapper);
            //说明大章节下有小章节
            if (count>0){
                XueChengPlusException.cast("课程计划信息还有子级信息，无法操作");
            }
            //没有小章节，删除
                teachplanMapper.deleteById(teachplanId);
            return;
        }
        //删除小章节
        int i = teachplanMapper.deleteById(teachplanId);
        //删除小章节成功，删除媒体信息
        if (i>0){
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMedia::getTeachplanId,teachplanId);
            teachplanMediaMapper.delete(queryWrapper);
        }

    }
    @Transactional
    @Override
    public void moveTeachplan(String Type, Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        //判断课程计划是否存在
        if (teachplan==null){
            XueChengPlusException.cast("该课程计划不存在");
        }
        //查询同一个层级的章节数目有几个
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId()).
                eq(Teachplan::getGrade,teachplan.getGrade()).
                eq(Teachplan::getParentid,teachplan.getParentid());
        Integer count = teachplanMapper.selectCount(queryWrapper);
        //查询当前课程排序是多少
        Integer orderby = teachplan.getOrderby();
        //处理上移动的情况
        //如果是第一个
        if("movedown".equals(Type)){
            if (orderby.equals(1)){
                return;
            }
            //获取要移动的前一个
            Teachplan teachplanSwaq = teachplanMapper.selectOne(queryWrapper.eq(Teachplan::getOrderby, orderby - 1));
            //将当前的排序-1
            LambdaUpdateWrapper<Teachplan> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Teachplan::getOrderby,orderby-1).eq(Teachplan::getId,teachplan.getId());
            teachplanMapper.update(null,updateWrapper);

            //将之前的排序加一 等于将这两个交换位置
            teachplanMapper.update(null,new  LambdaUpdateWrapper<Teachplan>().set(Teachplan::getOrderby,teachplanSwaq.getOrderby()+1).eq(Teachplan::getId,teachplanSwaq.getId()));

            return;
        }
        //处理向下的情况
        //如果是最后一个
        if(orderby.equals(count)){
            return;
        }
        //获取要移动的后一个
        Teachplan teachplanSwaq = teachplanMapper.selectOne(queryWrapper.eq(Teachplan::getOrderby, orderby + 1));
        //将当前的排序+1
        LambdaUpdateWrapper<Teachplan> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Teachplan::getOrderby,orderby+1).eq(Teachplan::getId,teachplan.getId());
        teachplanMapper.update(null,updateWrapper);

        //将之后的排序-1
        teachplanMapper.update(null,new  LambdaUpdateWrapper<Teachplan>().set(Teachplan::getOrderby,teachplanSwaq.getOrderby()-1).eq(Teachplan::getId,teachplanSwaq.getId()));
    }
}
