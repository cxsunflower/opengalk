package com.opengalk.server.工具类;

import com.opengalk.server.实体类.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @author cx
 */
@Component
public class LoginUserUtil {

    /**
     * 获取登陆用户
     *
     * @return UserInfo
     */
    public UserInfo getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectUtils.isEmpty(authentication) ? null : (UserInfo) authentication.getPrincipal();
    }

    /**
     *
     * @return id
     */
    public Long getLoginUserId() {
        UserInfo loginUser = this.getLoginUser();
        return ObjectUtils.isEmpty(loginUser) ? null : loginUser.getId();
    }
}
