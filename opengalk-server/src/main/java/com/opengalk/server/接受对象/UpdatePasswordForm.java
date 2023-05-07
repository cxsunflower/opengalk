package com.opengalk.server.接受对象;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordForm {

    @Size(min = 6, max = 20)
    String oldPassword;

    @Size(min = 6, max = 20)
    String newPassword;
}
