package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author unbroken
 * @Description TODO
 * @Version 1.0
 * @date 2023/4/3 18:18
 */
@Slf4j
@Component
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private XcUserMapper xcUserMapper;
    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //将传入的json转成AuthParamsDto对象
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        } catch (Exception e) {
            throw new RuntimeException("请求认证参数不符合要求");
        }

        //认证类型,有password，wx...
        String authType = authParamsDto.getAuthType();

        //根据认证类型从spring容器取出指定的bean
        String beanName = authType + "_authservice";
        AuthService authService = applicationContext.getBean(beanName, AuthService.class);
        //调用统一execute方法完成认证
        XcUserExt xcUserExt = authService.execute(authParamsDto);

        //封装xcUserExt用户信息为UserDetails
        UserDetails userPrinciple = getUserPrinciple(xcUserExt);
        return userPrinciple;
    }

    /**
     * 查询用户信息
     * @param xcUser 用户id ，主键
     * @return 用户信息
     */
    public UserDetails getUserPrinciple(XcUserExt xcUser) {
        String password = xcUser.getPassword();
        //权限
        String[] authorities = {"test"};
        xcUser.setPassword(null);
        //将用户信息转json(扩展用户信息到JWT令牌)
        String userJson = JSON.toJSONString(xcUser);
        UserDetails userDetails = User.withUsername(userJson).password(password).authorities(authorities).build();
        return userDetails;
    }
}
