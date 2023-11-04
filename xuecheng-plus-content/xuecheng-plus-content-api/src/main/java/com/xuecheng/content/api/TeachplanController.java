package com.xuecheng.content.api;

import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 课程计划管理相关接口
 *
 * @author xuqizheng
 * @date 2023/10/17
 */
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
@RestController
public class TeachplanController {
    @Autowired
    TeachplanService teachplanService;
    @ApiOperation("查询课程计划树型结构")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return  teachplanService.findTeachplanTree(courseId);
    }
    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto teachplanDto){
        teachplanService.saveTeachplan(teachplanDto);
    }
    @ApiOperation("删除课程计划")
    @DeleteMapping("/teachplan/{teachplanId}")
    public void deleteTeachplan(@PathVariable Long teachplanId){
        teachplanService.deleteTeachplan(teachplanId);
    }
    @ApiOperation("课程计划向上移动")
    @PostMapping("/teachplan/movedown/{id}")
    public void  movedownTeachplan(@PathVariable Long id,HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        String type = url.substring(url.lastIndexOf("/") + 1);
        teachplanService.moveTeachplan(type,id);
    }
    @ApiOperation("课程计划向下移动")
    @PostMapping("/teachplan/moveup/{id}")
    public void  moveupTeachplan(@PathVariable Long id,HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        String type = url.substring(url.lastIndexOf("/") + 1);
        teachplanService.moveTeachplan(type,id);
    }

    @ApiOperation("课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachplanService.associationMedia(bindTeachplanMediaDto);

    }
    @ApiOperation("课程计划和媒资信息解绑")
    @DeleteMapping("/teachplan/association/media/{teachPlanId}/{mediaId}")
    public void untieassociationMedia(@PathVariable int teachPlanId,@PathVariable String mediaId){
        teachplanService.untieassociationMedia(teachPlanId,mediaId);
    }


}
