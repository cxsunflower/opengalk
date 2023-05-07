package com.opengalk.server.业务逻辑层;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opengalk.server.接受对象.UpdatePasswordForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import com.opengalk.server.接受对象.RegisterForm;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.UserInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cx
 * @description 针对表【用户信息】的数据库操作Service
 * @createDate 2023-03-14 14:32:58
 */
public interface UserInfoService extends IService<UserInfo> {

    @Transactional
    ResponseResult<?> register(RegisterForm registerForm);

    ResponseResult<?> getUserList(Integer currentPage, Integer pageSize, String conditon, String keyword);

    ResponseResult<?> getUserInfoById(Long id);

    ResponseResult<?> getUserInfo();

    @Transactional
    ResponseResult<?> addUser(UserInfo userInfo);

    @Transactional
    ResponseResult<?> updateUserPassword(Long id, String newPassword);

    ResponseResult<?> updatePersonalPassword(UpdatePasswordForm updatePasswordForm);

    @Transactional
    ResponseResult<?> updateUserInfo(UserInfo userInfo);

    @Transactional
    ResponseResult<?> updateUserInfoById(UserInfo userInfo, Long id);

    @Transactional
    ResponseResult<?> deleteUserById(Long id);

    ResponseResult<?> uploadAvatar(MultipartFile file);

    ResponseResult<?> getAvatar();
}
