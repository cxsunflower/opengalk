package com.opengalk.server.处理类;

import cn.hutool.core.lang.UUID;
import cn.hutool.jwt.JWTUtil;
import com.opengalk.server.业务逻辑层.实现.VerifyCodeServiceImpl;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.UserInfo;
import com.opengalk.server.实体类.UserLoginAndLogoutRecord;
import com.opengalk.server.工具类.RedisUtil;
import com.opengalk.server.工具类.ResponseUtil;
import com.opengalk.server.数据访问层.UserLoginAndLogoutRecordMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final RedisUtil redisUtil;

    private final UserLoginAndLogoutRecordMapper userLoginAndLogoutLogMapper;

    private final ResponseUtil responseUtil;

    private final VerifyCodeServiceImpl verifyCodeService;

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        ResponseResult<?> result = verifyCodeService.verifiedResponse(request.getParameter("uuid"), request.getParameter("verificationCode"));

        if (!ObjectUtils.isEmpty(result)) {
            responseUtil.renderResponse(response, result);
            return;
        }

        UserInfo loginUser = (UserInfo) authentication.getPrincipal();

        // 获取用户信息封装到token
        Long id = loginUser.getId();
        String account = loginUser.getAccount();
        Integer authority = loginUser.getAuthority();

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("account", account);
        payload.put("authority", authority);
        payload.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
        // 用户id作为键
        String redisKey = String.valueOf(id);

        // HS256(HmacSHA256)密钥
        byte[] key = UUID.fastUUID().toString().getBytes();
        String jwt = JWTUtil.createToken(payload, key);
        log.info("用户id:" + id + ";token:" + jwt);

        // 将token封装到用户信息
        loginUser.setToken(jwt);
        loginUser.setPassword(null);
        // 设置redis缓存对象
        redisUtil.setObject("LOGIN_ID_" + redisKey, loginUser);
        redisUtil.expire(redisKey, 12, TimeUnit.HOURS);

        // 记录用户登陆
        // TODO 记录更多登陆时的信息
        UserLoginAndLogoutRecord record = UserLoginAndLogoutRecord.builder()
                .loginOrLogout(0)
                .id(id)
                .build();
        userLoginAndLogoutLogMapper.insert(record);
        responseUtil.renderResponse(response, new ResponseResult<>(1, "登陆成功", jwt));
    }

}
