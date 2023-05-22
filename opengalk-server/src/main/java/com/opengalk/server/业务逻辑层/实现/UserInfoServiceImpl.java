package com.opengalk.server.业务逻辑层.实现;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.业务逻辑层.UserInfoService;
import com.opengalk.server.业务逻辑层.UserUpdateRecordService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.UserInfo;
import com.opengalk.server.实体类.UserUpdateRecord;
import com.opengalk.server.工具类.LoginUserUtil;
import com.opengalk.server.工具类.RedisUtil;
import com.opengalk.server.接受对象.RegisterForm;
import com.opengalk.server.接受对象.UpdatePasswordForm;
import com.opengalk.server.数据访问层.UserInfoMapper;
import com.opengalk.server.数据访问层.UserUpdateRecordMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author cx
 * @description 针对表【用户信息】的数据库操作Service实现
 * @createDate 2023-03-14 14:32:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    private final static String FILE_DIR = System.getProperty("user.dir") + "/user_avatar/";
    private final UserInfoMapper userInfoMapper;
    private final UserUpdateRecordMapper userUpdateRecordMapper;
    private final UserUpdateRecordService userUpdateRecordService;
    private final VerifyCodeServiceImpl verifyCodeService;
    private final LoginUserUtil loginUserUtil;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public ResponseResult<?> register(@NotNull RegisterForm registerForm) {
        log.info("注册用户:" + registerForm);
        ResponseResult<?> result = verifyCodeService.verifiedResponse(registerForm.getUuid(), registerForm.getVerificationCode());

        if (!ObjectUtils.isEmpty(result)) {
            return result;
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", registerForm.getAccount());

        if (userInfoMapper.selectOne(queryWrapper) == null) {
            UserInfo newUser = UserInfo.builder()
                    .account(registerForm.getAccount())
                    .password(passwordEncoder.encode(registerForm.getPassword()))
                    .build();
            userInfoMapper.insert(newUser);

            newUser.setCreateBy(newUser.getId());
            userInfoMapper.updateById(newUser);

            return new ResponseResult<>(1, "注册成功", null);
        }
        return new ResponseResult<>(0, "用户名重复", null);
    }

    @Override
    public ResponseResult<?> getUserList(Integer currentPage, Integer pageSize, String condition, @NotNull String keyword) {
        Page<UserInfo> page = new Page<>(currentPage, pageSize);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("id", "account", "name", "authority", "is_locked", "is_deleted", "create_time", "create_by")
                .gt("authority", 0);

        if (!keyword.isBlank() && !"id".equals(condition)) {
            queryWrapper.like(condition, keyword);
        } else if (!keyword.isBlank() && "id".equals(condition)) {
            long id;
            try {
                id = Long.parseLong(keyword);
            } catch (Exception e) {
                return new ResponseResult<>(0, "异常id参数", null);
            }

            queryWrapper.like("CAST(id AS VARCHAR)", id);
        }

        return new ResponseResult<>(1, null, userInfoMapper.selectPage(page, queryWrapper));
    }

    @Override
    public ResponseResult<?> getUserInfoById(Long id) {
        UserInfo userInfo = userInfoMapper.getUserInfo(id);
        log.info("user:" + userInfo);
        userInfo.setPassword(null);
        if (loginUserUtil.getLoginUser().getAuthority() != 0) {
            userInfo.setIsLocked(null);
            userInfo.setCreateTime(null);
            userInfo.setCreateBy(null);
            userInfo.setAuthority(null);
        }
        return new ResponseResult<>(1, null, userInfo);
    }

    @Override
    public ResponseResult<?> getUserInfo() {
        Long id = loginUserUtil.getLoginUserID();
        log.info("获取用户信息id:" + id);
        if (ObjectUtils.isEmpty(id)) {
            return new ResponseResult<>(0, null, null);
        }

        return getUserInfoById(id);
    }

    @Transactional
    @Override
    public ResponseResult<?> addUser(@NotNull UserInfo userInfo) {
        log.info(userInfo.toString());
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", userInfo.getAccount());

        if (userInfoMapper.selectOne(queryWrapper) == null) {
            Long id = loginUserUtil.getLoginUserID();
            if (ObjectUtils.isEmpty(id)) {
                return new ResponseResult<>(0, null, null);
            }

            UserInfo newUser = UserInfo.builder()
                    .account(userInfo.getAccount())
                    .password(passwordEncoder.encode(userInfo.getPassword()))
                    .authority(userInfo.getAuthority())
                    .createBy(id)
                    .build();

            userInfoMapper.insert(newUser);

            UserUpdateRecord record = userUpdateRecordService.setUserUpdateRecordMapper(
                    newUser.getId(),
                    "user.user_info",
                    "all",
                    null,
                    null,
                    id,
                    "添加账号"
            );

            return userUpdateRecordMapper.insert(record) == 1
                    ? new ResponseResult<>(1, "添加用户成功", null)
                    : new ResponseResult<>(0, "添加用户失败", null);
        }
        return new ResponseResult<>(0, "用户名重复", null);
    }

    @Transactional
    @Override
    public ResponseResult<?> updateUserPassword(Long id, String newPassword) {
        newPassword = passwordEncoder.encode(newPassword);
        UserInfo updateUser = UserInfo.builder()
                .id(id)
                .password(newPassword)
                .build();

        // 获取之更新之前密码
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("password")
                .eq("id", id);

        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        // 更新之前密码
        String oldPassword = userInfo.getPassword();
        if (userInfoMapper.updateById(updateUser) == 1) {
            UserUpdateRecord record = userUpdateRecordService.setUserUpdateRecordMapper(
                    id,
                    "user.user_info",
                    "password",
                    oldPassword,
                    newPassword,
                    loginUserUtil.getLoginUserID(),
                    "修改密码"
            );
            // 目前只有管理员能更新权限
            record.setUpdateBy(loginUserUtil.getLoginUserID());
            if (userUpdateRecordMapper.insert(record) == 1) {
                return new ResponseResult<>(1, "更新密码成功", null);
            }
        }
        return new ResponseResult<>(0, "操作异常", null);

    }

    @Override
    public ResponseResult<?> updatePersonalPassword(@NotNull UpdatePasswordForm updatePasswordForm) {
        Long id = loginUserUtil.getLoginUserID();

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);

        // 数据库用户旧密码
        String oldPassword = userInfoMapper.selectOne(queryWrapper).getPassword();

        String formOldPassword = updatePasswordForm.getOldPassword();
        String formNewPassword = updatePasswordForm.getNewPassword();
        if (!passwordEncoder.matches(formOldPassword, oldPassword)) {
            return new ResponseResult<>(0, "原密码错误", null);
        } else if (passwordEncoder.matches(formNewPassword, oldPassword)) {
            return new ResponseResult<>(0, "新密码不能跟原密码相同", null);
        } else {
            UserInfo updateUser = UserInfo.builder()
                    .id(id)
                    .password(passwordEncoder.encode(formNewPassword))
                    .build();

            userInfoMapper.updateById(updateUser);
            redisUtil.deleteObject("LOGIN_ID_" + id);
            return new ResponseResult<>(1, "修改密码成功", null);
        }
    }

    @Transactional
    @Override
    public ResponseResult<?> updateUserInfo(UserInfo userInfo) {
        Long id = loginUserUtil.getLoginUserID();
        return updateUserInfoById(userInfo, id);
    }

    @Transactional
    @Override
    public ResponseResult<?> updateUserInfoById(@NotNull UserInfo userInfo, Long id) {
        UserInfo updateUser = UserInfo.builder()
                .id(id)
                .name(userInfo.getName())
                .gender(userInfo.getGender())
                .phoneNumber(userInfo.getPhoneNumber())
                .birthday(userInfo.getBirthday())
                .collegeId(userInfo.getCollegeId())
                .email(userInfo.getEmail())
                .build();

        if (loginUserUtil.getLoginUser().getAuthority() == 0) {
            Integer isLocked = userInfo.getIsLocked();
            updateUser.setAuthority(userInfo.getAuthority());
            updateUser.setIsLocked(isLocked);
            if (isLocked == 1) {
                redisUtil.deleteObject("LOGIN_ID_" + id);
            }
        }
        // 获取之更新之前信息
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("name", "gender", "is_locked", "authority", "phone_number", "birthday", "college_id", "email")
                .eq("id", id);
        // 更新之前信息
        UserInfo userInfo1 = userInfoMapper.selectOne(queryWrapper);

        if (userInfoMapper.updateById(updateUser) == 1) {
            UserUpdateRecord record = userUpdateRecordService.setUserUpdateRecordMapper(
                    id,
                    "user.user_info",
                    "用户信息",
                    JSONUtil.toJsonStr(userInfo1),
                    JSONUtil.toJsonStr(updateUser),
                    loginUserUtil.getLoginUserID(),
                    "修改个人信息"
            );
            if (userUpdateRecordMapper.insert(record) == 1) {
                return new ResponseResult<>(1, "修改个人信息成功", null);
            }
        }
        return new ResponseResult<>(0, "操作异常", null);
    }

    @Transactional
    @Override
    public ResponseResult<?> deleteUserById(Long id) {
        if (userInfoMapper.deleteById(id) == 1) {
            UserUpdateRecord record = userUpdateRecordService.setUserUpdateRecordMapper(
                    id,
                    "user.user_info",
                    "用户信息",
                    null,
                    null,
                    loginUserUtil.getLoginUserID(),
                    "删除账号"
            );
            if (userUpdateRecordMapper.insert(record) == 1) {
                return new ResponseResult<>(1, "删除成功", null);
            }
        }
        return new ResponseResult<>(0, "操作异常", null);
    }

    @Override
    public ResponseResult<?> uploadAvatar(MultipartFile file) {
        Long id = loginUserUtil.getLoginUserID();
        // 获取上传临时文件夹的路径
        String filePath = FILE_DIR + id;
        System.out.println(filePath);

        File fileDir = new File(filePath);

        // 如果没有文件夹则建一个
        if (!fileDir.getParentFile().exists() && fileDir.getParentFile().mkdirs()) {
            log.info("创建文件夹成功：" + fileDir.getAbsolutePath());
        }

        try {
            // 把文件写入到上传的路径
            FileUtil.writeBytes(file.getBytes(), filePath);
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            return new ResponseResult<>(0, "头像上传失败", null);
        }

        return new ResponseResult<>(1, "头像上传成功", null);
    }

    @Override
    public ResponseResult<?> getAvatar() {
        Long id = loginUserUtil.getLoginUserID();
        String filePath = FILE_DIR + id;
        byte[] data = null;

        // 读取图片字节数组
        try (InputStream in = new FileInputStream(filePath)) {
            data = new byte[in.available()];
            int counts = in.read(data);
            log.info("I/O字节：" + counts);
        } catch (IOException e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        }

        return new ResponseResult<>(0, null, Base64Encoder.encode(data));
    }

}




