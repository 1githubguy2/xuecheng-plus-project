package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author unbroken
 * @Description TODO
 * @Version 1.0
 * @date 2023/4/2 23:58
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
    //这里可以拿到发生熔断的异常信息throwable
    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient() {
            //发生熔断，上传服务调用此方法执行降级逻辑
            @Override
            public String upload(MultipartFile filedata, String objectName) {
                log.debug("远程调用上传文件的接口发生熔断:{}",throwable.toString(), throwable);
                return null;
            }
        };
    }
}
