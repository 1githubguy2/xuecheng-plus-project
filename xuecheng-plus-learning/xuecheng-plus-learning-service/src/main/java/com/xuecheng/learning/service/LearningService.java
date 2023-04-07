package com.xuecheng.learning.service;

import com.xuecheng.base.model.RestResponse;

/**
 * @author unbroken
 * @Description 在线学习接口
 * @Version 1.0
 * @date 2023/4/6 14:52
 */
public interface LearningService {

    /**
     * 获取教学视频
     * @param userId 用户id
     * @param courseId 课程id
     * @param teachplanId 课程计划id
     * @param mediaId 视频id
     * @return
     */
    public RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId);
}
