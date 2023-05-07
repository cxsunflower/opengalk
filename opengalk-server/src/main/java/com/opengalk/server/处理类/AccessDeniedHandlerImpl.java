package com.opengalk.server.处理类;

import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.工具类.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        responseUtil.renderResponse(response, new ResponseResult<>(0, "你的权限不足", null));
    }
}
