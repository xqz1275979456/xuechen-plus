package com.xuecheng.content.feignclient;

import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 远程调用媒资服务接口
 *
 * @author xuqizheng
 * @date 2023/11/10
 */
@FeignClient(value = "media-api",configuration = {MultipartSupportConfig.class},fallbackFactory = MediaServiceClientFallbackFactory.class)
public interface MediaServiceClient {
    @RequestMapping(value = "/media/upload/coursefile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile filedata,
                                      @RequestParam(value = "objectName",required = false)String objectName) throws IOException;

}
