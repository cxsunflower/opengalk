package com.opengalk.server.过滤器;

import cn.hutool.jwt.JWTUtil;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.UserInfo;
import com.opengalk.server.工具类.RedisUtil;
import com.opengalk.server.工具类.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * @author cx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JavaWebTokenFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;

    private final ResponseUtil responseUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            // 放行到登陆
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        String id;
        try {
            id = JWTUtil.parseToken(token).getPayload().getClaim("id").toString();
        } catch (Exception e) {
            responseUtil.renderResponse(response, new ResponseResult<>(0, "非法token", null));
            return;
        }

        log.info("当前tokenid:" + id);
        // 从redis中获取用户信息
        UserInfo loginUser = redisUtil.getObject("LOGIN_ID_" + id);

        if (ObjectUtils.isEmpty(loginUser)) {
            responseUtil.renderResponse(response, new ResponseResult<>(0, "用户未登陆", null));
            return;
        }

        if (!token.equals(loginUser.getToken())) {
            responseUtil.renderResponse(response, new ResponseResult<>(0, "疑似篡改token", null));
            return;
        }

        // 存入SecurityContextHolder,获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,
                null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        redisUtil.expire(id, 12, TimeUnit.HOURS);
        // 放行
        filterChain.doFilter(request, response);
    }
}
