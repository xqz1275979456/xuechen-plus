package com.xuecheng.content.feignclient;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用搜索服务接口
 *
 * @author xuqizheng
 * @date 2023/11/20
 */
@FeignClient(value = "search",fallbackFactory = SearchServiceClientFallbackFactory.class)
public interface SearchServiceClient {

    @ApiOperation("添加课程索引")
    @PostMapping("/search/index/course")
    public Boolean add(@RequestBody CourseIndex courseIndex);

}
