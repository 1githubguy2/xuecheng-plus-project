package com.xuecheng.base.exception;

/**
 * @author unbroken
 * @Description 本项目自定义异常类型
 * @Version 1.0
 * @date 2023/3/29 22:21
 */
public class XueChengPlusException extends RuntimeException{
    private String errMessage;


    public XueChengPlusException() {
    }

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public static void cast(String message) {
        throw new XueChengPlusException(message);
    }
    public static void cast(CommonError error) {
        throw new XueChengPlusException(error.getErrMessage());
    }
}
