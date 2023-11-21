package com.xuecheng.content;


import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 测试远程调用媒资服务
 *
 * @author xuqizheng
 * @date 2023/11/10
 */
@SpringBootTest
public class FeignUploadTest {
    @Autowired
    MediaServiceClient mediaServiceClient;
    @Test
    public void test() throws IOException {
        //将file类型转为MultipartFile
        File file = new File("E:\\develop\\upload\\120.html");
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        mediaServiceClient.upload(multipartFile,"course/120.html");
    }
}
