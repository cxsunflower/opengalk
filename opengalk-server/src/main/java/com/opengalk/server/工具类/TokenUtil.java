package com.opengalk.server.工具类;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenUtil {

    /**
     * @param request 请求
     * @param header  头字段
     * @return token
     */
    public String getToken(@NonNull HttpServletRequest request, String header) {
        return request.getHeader(header);
    }

    /**
     * @param token token
     * @param key   payload中id对应的键
     * @return id
     */
    public String getIdByToken(String token, String key) {
        String id;
        try {
            id = JWTUtil.parseToken(token).getPayload().getClaim(key).toString();
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            return null;
        }
        return id;
    }
}
