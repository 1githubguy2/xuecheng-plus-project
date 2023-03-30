package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;

/**
 * @author unbroken
 * @Description 测试minio的SDK
 * @Version 1.0
 * @date 2023/3/30 20:26
 */
public class MinioTest {
    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.31.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void test_upload() throws Exception {
        //通过扩展名得到媒体资源类型 mimeType
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();
        }
        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")//桶
                .filename("E:\\temp\\1.mp4") //指定本地文件路径
//                .object("1.mp4")//对象名 在桶下存储该文件
                .object("test/01/1.mp4")//对象名 放在子目录下
                .contentType(mimeType)//设置媒体文件类型
                .build();
        //上传文件
        minioClient.uploadObject(uploadObjectArgs);
    }

    @Test
    public void test_delete() throws Exception {
        //removeObjectArgs
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                        .object("1.mp4")
                                .build();
        //删除文件
        minioClient.removeObject(removeObjectArgs);
    }

    @Test
    public void test_getFile() throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                        .bucket("testbucket")
                                .object("test/01/1.mp4")
                                        .build();
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        // 指定输出流
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\temp\\1a.mp4"));
        IOUtils.copy(inputStream, outputStream);
        // 检验文件的完整性对文件的内容进行md5
        String sourceMd5 = DigestUtils.md5Hex(inputStream);//minio中文件的MD5
        String localMd5 = DigestUtils.md5Hex(new FileInputStream("E:\\temp\\1a.mp4"));//下载到本地的文件的MD5
        if(sourceMd5.equals(localMd5)) {
            System.out.println("下载成功");
        }else {
            System.out.println("下载不成功");
        }
    }
}
