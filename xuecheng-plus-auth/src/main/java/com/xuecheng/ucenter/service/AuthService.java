package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;

/**
 * @author unbroken
 * @Description 统一的认证接口
 * @Version 1.0
 * @date 2023/4/3 21:11
 */
public interface AuthService {

    /**
     * 认证方法
     * @param authParamsDto 认证参数
     * @return 用户信息
     */
    XcUserExt execute(AuthParamsDto authParamsDto);
}
