package com.xuecheng.content;

import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 课程计划mapper测试
 *
 * @author xuqizheng
 * @date 2023/10/17
 */
@SpringBootTest
public class TeachplanMapperTests {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Test
    public void testSelectTreeNodes(){
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(117L);
        System.out.println(teachplanDtos);
    }
}
