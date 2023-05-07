package com.opengalk.server.处理类;

import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.工具类.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthenticationFailureHandlerImpl
        implements AuthenticationFailureHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        responseUtil.renderResponse(response, new ResponseResult<>(0, "用户名或密码错误", null));
    }
}

