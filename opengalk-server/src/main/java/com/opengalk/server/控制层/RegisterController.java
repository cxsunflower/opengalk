package com.opengalk.server.控制层;

import com.opengalk.server.业务逻辑层.UserInfoService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.接受对象.RegisterForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserInfoService userInfoService;

    @PostMapping
    public ResponseResult<?> register(@RequestBody @Valid RegisterForm registerForm) {
        return userInfoService.register(registerForm);
    }
}
