package com.opengalk.server.工具类;

import com.opengalk.server.实体类.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class LoginUserUtil {

    /**
     * 获取登陆用户
     *
     * @return LoginUserDetails
     */
    public UserInfo getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication))
            return null;
        return (UserInfo) authentication.getPrincipal();
    }

    public Long getLoginUserID() {
        UserInfo loginUser = this.getLoginUser();
        if (ObjectUtils.isEmpty(loginUser)) {
            return null;
        }
        return loginUser.getId();
    }
}
