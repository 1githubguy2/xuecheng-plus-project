package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author unbroken
 * @Description TODO
 * @Version 1.0
 * @date 2023/4/3 14:39
 */
@Slf4j
@Component
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {
    @Override
    public SearchServiceClient create(Throwable throwable) {
        return new SearchServiceClient() {
            @Override
            public Boolean add(CourseIndex courseIndex) {
                log.error("添加课程索引发送熔断，索引信息:{},熔断异常:{}", courseIndex, throwable.toString(), throwable);
                //走降级了返回false
                return false;
            }
        };
    }
}
