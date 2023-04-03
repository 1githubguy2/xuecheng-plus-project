package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author unbroken
 * @Description 内容管理服务启动类
 * @Version 1.0
 * @date 2023/3/29 0:52
 */
@EnableFeignClients(basePackages = {"com.xuecheng.content.feignclient"})
@SpringBootApplication
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
