package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author unbroken
 * @Description 课程预览数据模型
 * @Version 1.0
 * @date 2023/4/2 12:15
 */
@Data
@ToString
public class CoursePreviewDto {
    //课程基本信息，营销信息
    private CourseBaseInfoDto courseBase;
    //课程计划信息
    private List<TeachplanDto> teachplans;
    //课程师资信息...
}
