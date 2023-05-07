package com.opengalk.server.接受对象;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm {

    @Size(min = 5,max = 20)
    private String account;

    @Size(min = 6,max = 20)
    private String password;

    @Size(min = 6,max = 20)
    private String confirmPassword;

    @Size(min = 6, max = 6)
    private String verificationCode;

    @Size(min = 36, max = 36)
    private String uuid;
}
