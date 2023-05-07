package com.opengalk.server.处理类;

import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.UserLoginAndLogoutRecord;
import com.opengalk.server.工具类.RedisUtil;
import com.opengalk.server.工具类.ResponseUtil;
import com.opengalk.server.工具类.TokenUtil;
import com.opengalk.server.数据访问层.UserLoginAndLogoutRecordMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final RedisUtil redisUtil;

    private final TokenUtil tokenUtil;

    private final UserLoginAndLogoutRecordMapper userLoginAndLogoutLogMapper;

    private final ResponseUtil responseUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = tokenUtil.getToken(request, "token");
        log.info(token);
        String id = tokenUtil.getIdByToken(token, "id");
        log.info(id);

        if (!StringUtils.hasText(id)) {
            responseUtil.renderResponse(response, new ResponseResult<>(0, "异常token", null));
            return;
        }

        log.info("当前注销id:" + id);
        if (token.equals(redisUtil.getObject("LOGIN_ID_" + id).getToken())) {
            redisUtil.deleteObject("LOGIN_ID_" + id);
            log.info("用户" + id + "注销成功");
            UserLoginAndLogoutRecord record = UserLoginAndLogoutRecord.builder()
                    .id(Long.parseLong(id))
                    .loginOrLogout(1)
                    .build();
            userLoginAndLogoutLogMapper.insert(record);
            responseUtil.renderResponse(response, new ResponseResult<>(1, "注销成功", null));
            return;
        }
        responseUtil.renderResponse(response, new ResponseResult<>(0, "注销失败", null));
    }
}
