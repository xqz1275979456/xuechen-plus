package com.xuecheng.content.feignclient;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 搜索服务降级工厂类
 * 可以拿到熔断的异常信息
 * @author xuqizheng
 * @date 2023/11/20
 */
@Component
@Slf4j
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {
    //拿到熔断的异常信息
    @Override
    public SearchServiceClient create(Throwable throwable) {
        return new SearchServiceClient() {
            @Override
            public Boolean add(CourseIndex courseIndex) {
                log.debug("添加课程索引发生熔断，索引的信息：{},熔断的异常信息：{}",courseIndex,throwable.toString(),throwable);
                //走降级返回false
                return false;
            }
        };
    }
}
