package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author unbroken
 * @Description 课程分类相关接口
 * @Version 1.0
 * @date 2023/3/29 17:12
 */
@RestController
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryServiceImpl;
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        return courseCategoryServiceImpl.queryTreeNodes("1");
    }
}
