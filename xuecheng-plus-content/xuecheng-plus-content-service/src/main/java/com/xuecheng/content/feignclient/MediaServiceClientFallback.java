package com.xuecheng.content.feignclient;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author unbroken
 * @Description 下游服务出问题后，上游服务进行熔断，采取降级处理，即调用fallback函数
 * @Version 1.0
 * @date 2023/4/2 23:53
 */
public class MediaServiceClientFallback implements MediaServiceClient{
    @Override
    public String upload(MultipartFile filedata, String objectName) {

        return null;
    }
}
