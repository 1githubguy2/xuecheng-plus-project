package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author unbroken
 * @Description 分类信息的模型类
 * @Version 1.0
 * @date 2023/3/29 17:09
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
    // 子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
