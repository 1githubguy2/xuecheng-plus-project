package com.xuecheng.base.exception;

import java.io.Serializable;

/**
 * @author unbroken
 * @Description 和前端约定返回的异常信息模型
 * @Version 1.0
 * @date 2023/3/29 22:19
 */
public class RestErrorResponse implements Serializable {
    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
