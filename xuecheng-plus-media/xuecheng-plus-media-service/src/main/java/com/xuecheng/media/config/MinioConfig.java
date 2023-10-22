package com.xuecheng.media.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置类
 *
 * @author xuqizheng
 * @date 2023/10/19
 */
@Configuration
public class MinioConfig {
    //读取配置文件上内容
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKey, secretKey).build();

    }

}
