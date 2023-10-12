package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 内容管理服务启动类
 *
 * @author xuqizheng
 * @date 2023/10/12
 */
@EnableSwagger2 //生成接口文档
@SpringBootApplication
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args );
    }
}
