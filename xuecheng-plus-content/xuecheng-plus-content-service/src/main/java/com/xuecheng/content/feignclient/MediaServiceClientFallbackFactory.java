package com.xuecheng.content.feignclient;

import com.xuecheng.media.model.dto.UploadFileResultDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 媒资服务服务降级工厂类
 * 可以拿到熔断的异常信息
 * @author xuqizheng
 * @date 2023/11/10
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
    //拿到熔断的异常信息
    @Override
    public MediaServiceClient create(Throwable throwable) {

        return new MediaServiceClient() {
            //当发生了熔断上游服务调用此方法执行降级逻辑
            @Override
            public UploadFileResultDto upload(MultipartFile filedata, String objectName) throws IOException {
                log.debug("远程调用上传文件的接口发生熔断：{}",throwable.toString(),throwable);
                return null;
            }
        };
    }
}
