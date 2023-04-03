package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CoursePreviewDto;

import java.io.File;

/**
 * @author unbroken
 * @Description 课程发布相关的接口
 * @Version 1.0
 * @date 2023/4/2 12:19
 */
public interface CoursePublishService {

    /**
     * 获取课程预览信息
     * @param courseId 课程id
     * @return
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * 提交审核
     * @param companyId 机构id
     * @param courseId 课程id
     */
    public void commitAudit(Long companyId, Long courseId);

    /**
     * 课程发布接口
     * @param company 机构id
     * @param courseId 课程id
     */
    public void publish(Long company, Long courseId);

    /**
     * 课程静态化
     * @param courseId 课程id
     * @return File 静态化文件
     */
    public File generateCourseHtml(Long courseId);

    /**
     * 上传课程静态化页面
     * @param courseId 课程id
     * @param file 静态化文件
     */
    public void uploadCourseHtml(Long courseId, File file);
}
