package com.xuecheng.media.service.jobhandler;

import com.xuecheng.base.utils.Mp4VideoUtil;
import com.xuecheng.media.model.po.MediaProcess;
import com.xuecheng.media.service.MediaFileProcessService;
import com.xuecheng.media.service.MediaFileService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 视频处理任务类
 */
@Slf4j
@Component
public class VideoTask {

    @Autowired
    private MediaFileProcessService mediaFileProcessServiceImpl;

    //ffmpeg的路径
    @Value("${videoprocess.ffmpegpath}")
    private String ffmpegpath;

    @Autowired
    private MediaFileService mediaFileServiceImpl;
    /**
     * 视频处理任务
     */
    @XxlJob("videoJobHandler")
    public void shardingJobHandler() throws Exception {
        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();//执行器的序号，从0开始
        int shardTotal = XxlJobHelper.getShardTotal();//执行器总数
        //确定cpu的核心数
        int processors = Runtime.getRuntime().availableProcessors();
        //查询待处理的任务
        List<MediaProcess> mediaProcessList = mediaFileProcessServiceImpl.getMediaProcessList(shardTotal, shardIndex, processors);
        //任务数量
        int size = mediaProcessList.size();
        log.debug("取到的视频处理任务数:"+size);
        if(size <= 0) {
            return;
        }
        //创建一个线程池(视频处理非常耗cpu)
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        //使用的计数器
        CountDownLatch countDownLatch = new CountDownLatch(size);
        mediaProcessList.forEach(mediaProcess -> {
            //将任务加入线程池
            executorService.execute(() -> {
                // 使用try..finally是为了保证执行任务中途出现异常，而countDownLatch没有及时减1的情况，即保证try完之后，再finally那里进行countDownLatch减1
                try{
                    //==========任务执行的具体逻辑========
                    //任务id
                    Long taskId = mediaProcess.getId();
                    //文件id就是md5
                    String fileId = mediaProcess.getFileId();
                    //开启任务（从数据库中获取锁）
                    boolean b = mediaFileProcessServiceImpl.startTask(taskId);
                    if(!b) {
                        log.debug("抢占任务失败，任务id:{}", taskId);
                        return;
                    }
                    //===执行视频转码===
                    //桶
                    String bucket = mediaProcess.getBucket();
                    //objectName
                    String objectName = mediaProcess.getFilePath();
                    //从minio下载视频到本地
                    File file = mediaFileServiceImpl.downloadFileFromMinIO(bucket, objectName);
                    if(file == null) {
                        log.debug("下载视频出错，任务id:{},bucket:{},objectName:{}", taskId, bucket, objectName);
                        //保存任务处理失败的结果
                        mediaFileProcessServiceImpl.saveProcessFinishStatus(taskId, "3", fileId, null, "下载视频到本地失败");
                        return;
                    }
                    //源avi视频的路径
                    String video_path = file.getAbsolutePath();
                    //转换后mp4文件的名称
                    String mp4_name = fileId + ".mp4";
                    //转换后mp4文件的路径
                    //先创建一个临时文件，作为转换后的文件
                    File mp4File = null;
                    try {
                        mp4File = File.createTempFile("minio", ".mp4");
                    } catch (IOException e) {
                        log.debug("创建临时文件异常，{}", e.getMessage());
                        //保存任务处理失败的结果
                        mediaFileProcessServiceImpl.saveProcessFinishStatus(taskId, "3", fileId, null, "创建临时文件异常");
                        return;
                    }
                    String mp4_path = mp4File.getAbsolutePath();
                    //创建工具类对象
                    Mp4VideoUtil videoUtil = new Mp4VideoUtil(ffmpegpath,video_path,mp4_name,mp4_path);
                    //开始视频转换，成功将返回success，失败返回失败原因
                    String result = videoUtil.generateMp4();
                    if(!"success".equals(result)) {
                        log.debug("视频转码失败，原因:{},bucket:{},objectName:{},", result, bucket, objectName);
                        mediaFileProcessServiceImpl.saveProcessFinishStatus(taskId, "3", fileId, null, result);
                        return;
                    }
                    //上传转码后的视频文件到minio
                    String objectNameMp4 = getFilePathByMd5(fileId, ".mp4");
                    boolean b1 = mediaFileServiceImpl.addMediaFilesToMinIO(mp4File.getAbsolutePath(), "video/mp4", bucket, objectNameMp4);
                    if(!b1) {
                        log.debug("上传mp4到minio失败，taskid:{},", taskId);
                        mediaFileProcessServiceImpl.saveProcessFinishStatus(taskId, "3", fileId, null, "上传mp4到minio失败");
                        return;
                    }
                    //mp4文件的url
                    String url = "/"+bucket+"/"+objectNameMp4;
                    //更新任务状态为成功
                    mediaFileProcessServiceImpl.saveProcessFinishStatus(taskId, "2", fileId, url, "更新任务状态为成功");
                }finally {
                    // 保证执行完任务之后countDownLatch才进行减一
                    countDownLatch.countDown();
                }
            });
        });
        //阻塞，指定最大限制的等待时间，阻塞最多等待一定的时间后就解除阻塞
        //阻塞方法，保证让countDownLatch减到0或者过期时间到了，这个阻塞才放行，videoJobHandler方法才算结束
        //（即保证大家都完成任务之后，整个方法videoJobHandler才可以结束，要不然的话开启多线程之后，该方法立即就结束了，任务在线程池中被处理，但是任务是还没有被执行完的）
        countDownLatch.await(30, TimeUnit.MINUTES);
    }
    /**
     * 得到合并后的文件的地址
     *
     * @param fileMd5 文件id即md5值
     * @param fileExt 文件扩展名
     * @return
     */
    private String getFilePathByMd5(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + fileExt;
    }
}
