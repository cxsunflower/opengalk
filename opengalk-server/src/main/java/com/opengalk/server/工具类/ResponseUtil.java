package com.opengalk.server.工具类;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import com.opengalk.server.响应类.ResponseResult;

import java.io.IOException;

@Component
public class ResponseUtil {
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param result   ResponseResult<?>
     */
    public void renderResponse(@NonNull HttpServletResponse response, ResponseResult<?> result) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSONUtil.toJsonStr(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}