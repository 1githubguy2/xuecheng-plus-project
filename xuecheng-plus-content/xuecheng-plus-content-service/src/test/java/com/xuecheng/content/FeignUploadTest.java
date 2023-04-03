package com.xuecheng.content;

import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author unbroken
 * @Description 测试远程调用媒资服务
 * @Version 1.0
 * @date 2023/4/2 22:56
 */
@SpringBootTest
public class FeignUploadTest {

    @Autowired
    private MediaServiceClient mediaServiceClient;

    @Test
    public void test() {
        //将file转成MultipartFile
        File file = new File("E:\\temp\\123.html");
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        //远程调用
        String upload = mediaServiceClient.upload(multipartFile, "course/123.html");
        if (upload == null) {
            System.out.println("走了降级逻辑");
        }
    }
}
