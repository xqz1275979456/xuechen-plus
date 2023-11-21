package com.xuecheng.content.feignclient;

import com.xuecheng.media.model.dto.UploadFileResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 媒资服务服务降级类
 * 无法取出熔断所抛出的异常
 * @author xuqizheng
 * @date 2023/11/10
 */
public class MediaServiceClientFallback implements MediaServiceClient {
    @Override
    public UploadFileResultDto upload(MultipartFile filedata, String objectName) throws IOException {
        return null;
    }
}
