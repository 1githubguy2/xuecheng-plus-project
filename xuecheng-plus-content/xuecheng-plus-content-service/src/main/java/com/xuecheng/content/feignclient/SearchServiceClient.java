package com.xuecheng.content.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author unbroken
 * @Description TODO
 * @Version 1.0
 * @date 2023/4/3 14:36
 */
@FeignClient(value = "search", fallbackFactory = SearchServiceClientFallbackFactory.class)
public interface SearchServiceClient {

    @PostMapping("/search/index/course")
    public Boolean add(@RequestBody CourseIndex courseIndex);
}
