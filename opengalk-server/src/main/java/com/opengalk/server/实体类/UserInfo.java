package com.opengalk.server.实体类;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @TableName user_info
 */
@TableName(value = "user_info", schema = "\"user\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements UserDetails, Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 账号
     */
    @TableField(value = "account")
    @Size(min = 5, max = 20)
    private String account;
    /**
     * 密码
     */

    @TableField(value = "password")
    @Size(min = 6, max = 20)
    private String password;
    /**
     * 0为管理员，1为教师，2为学生
     */
    @TableField(value = "authority")
    private Integer authority;
    /**
     * 0为账号未删除，1为账号已删除
     */

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted;
    /**
     * 用户创建时间
     */

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 由谁创建
     */
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 0账号未锁定，1账号锁定
     */
    @TableField(value = "is_locked")
    private Integer isLocked;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 学校id
     */
    @TableField(value = "college_id")
    private Integer collegeId;

    /**
     * 电子邮箱
     */
    @Email
    @TableField(value = "email")
    private String email;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 出生日期
     */
    @TableField(value = "birthday")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @TableField(exist = false)
    private String token;

    @TableField(exist = false)
    private String collegeName;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (StringUtils.hasText(String.valueOf(authority))) {
            List<SimpleGrantedAuthority> list = new ArrayList<>();
            list.add(new SimpleGrantedAuthority(String.valueOf(authority)));
            return list;
        }

        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.getAccount();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}