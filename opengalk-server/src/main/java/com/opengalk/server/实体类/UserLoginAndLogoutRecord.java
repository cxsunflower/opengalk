package com.opengalk.server.实体类;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName user_login_and_logout_record
 */
@TableName(value = "user_login_and_logout_record",schema = "\"user\"")
@Data
@Builder
public class UserLoginAndLogoutRecord implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */

    private Long id;
    /**
     * 0为登陆，1为注销
     */
    @TableField(value = "login_or_logout")
    private Integer loginOrLogout;
    /**
     * 登陆或注销时间
     */
    @TableField(value = "time", fill = FieldFill.INSERT)
    private LocalDateTime time;
}