package com.opengalk.server.控制层;

import com.opengalk.server.业务逻辑层.实现.VerifyCodeServiceImpl;
import com.opengalk.server.响应类.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@CrossOrigin
@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class VerifyCodeController {

    private final VerifyCodeServiceImpl verifyCodeService;

    @GetMapping("/getVerificationCode")
    public ResponseResult<?> getVerificationCode(){
        return verifyCodeService.getVerificationCode();
    }


}
