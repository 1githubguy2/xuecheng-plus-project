package com.xuecheng.content.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author unbroken
 * @Description freemarker入门程序
 * @Version 1.0
 * @date 2023/4/2 1:00
 */
@Controller
public class FreemarkerController {

    @GetMapping("/testfreemarker")
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        //指定模型
        modelAndView.addObject("name","小明");
        //指定模板
        modelAndView.setViewName("test");//根据视图名称加.ftl找到模板
        return modelAndView;
    }
}
