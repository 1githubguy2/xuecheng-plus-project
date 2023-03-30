package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author unbroken
 * @Description 统一异常处理
 * @Version 1.0
 * @date 2023/3/29 22:32
 */
@Slf4j
@ControllerAdvice//控制器增强
//@RestControllerAdvice 相当于 ControllerAdvice+ResponseBody
public class GlobalExceptionHandler {
    //对项目的自定义异常类型进行处理(即捕获自定义异常)
    @ResponseBody//将信息以json格式返回
    @ExceptionHandler(XueChengPlusException.class)// 此方法捕获XueChengPlusException异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)// 状态码返回500
    public RestErrorResponse customException(XueChengPlusException e) {
        //记录异常
        log.error("系统异常{}",e.getErrMessage(),e);
        //..

        //解析出异常信息
        String errMessage = e.getErrMessage();
        return new RestErrorResponse(errMessage);
    }

    // 捕获其它异常
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        //记录异常
        log.error("系统异常{}",e.getMessage(),e);

        //解析出异常信息
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }

    // 捕获MethodArgumentNotValidException异常
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        //存储错误信息
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().stream().forEach(item -> {
            errors.add(item.getDefaultMessage());
        });
        //将list中的错误信息拼接起来
        String errMessage = StringUtils.join(errors, ",");
        //记录异常
        log.error("系统异常{}",e.getMessage(),errMessage);

        //解析出异常信息
        return new RestErrorResponse(errMessage);
    }
}
