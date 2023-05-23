package com.opengalk.server.工具类;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.opengalk.server.响应类.ResponseResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ResponseUtil {
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param result   ResponseResult<?>
     */
    public void renderResponse(@NotNull HttpServletResponse response, ResponseResult<?> result) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSONUtil.toJsonStr(result));
        } catch (IOException e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        }
    }
}