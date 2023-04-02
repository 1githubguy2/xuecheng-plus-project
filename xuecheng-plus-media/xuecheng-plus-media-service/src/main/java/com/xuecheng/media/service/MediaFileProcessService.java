package com.xuecheng.media.service;

import com.xuecheng.media.model.po.MediaProcess;

import java.util.List;

/**
 * @author unbroken
 * @Description 任务处理
 * @Version 1.0
 * @date 2023/4/1 16:08
 */
public interface MediaFileProcessService {

    /**
     * 获取待处理任务
     * @param shardTotal 分片总数
     * @param shardIndex 分片序号
     * @param count 获取记录数
     * @return
     */
    public List<MediaProcess> getMediaProcessList(int shardTotal, int shardIndex, int count);

    /**
     * 开启一个任务
     * @param id 任务id
     * @return true开启任务成功，false开启任务失败
     */
    public boolean startTask(long id);

    /**
     * 保存任务结果
     * @param taskId 任务id
     * @param status 任务状态
     * @param fileId 文件id
     * @param url url
     * @param errorMsg 错误信息
     */
    void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);
}
