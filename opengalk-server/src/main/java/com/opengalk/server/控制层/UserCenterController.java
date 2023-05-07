package com.opengalk.server.控制层;

import com.opengalk.server.业务逻辑层.CollegeInfoService;
import com.opengalk.server.业务逻辑层.UserInfoService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.UserInfo;
import com.opengalk.server.接受对象.UpdatePasswordForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/userCenter")
@PreAuthorize("hasAnyAuthority('0','1','2')")
@RequiredArgsConstructor
public class UserCenterController {

    private final UserInfoService userInfoService;

    private final CollegeInfoService collegeInfoService;

    @GetMapping
    public ResponseResult<?> getUserInfo() {
        return userInfoService.getUserInfo();
    }

    @PutMapping("/updateUserInfo")
    public ResponseResult<?> updateUserInfo(@RequestBody @Valid UserInfo userInfo) {
        return userInfoService.updateUserInfo(userInfo);
    }

    @PutMapping("/updatePersonalPassword")
    public ResponseResult<?> updatePersonalPassword(@RequestBody UpdatePasswordForm updatePasswordForm) {
        return userInfoService.updatePersonalPassword(updatePasswordForm);
    }

    @GetMapping("/getCollegeList")
    public ResponseResult<?> getCollegeList() {
        return collegeInfoService.getCollegeList();
    }

    @GetMapping("/getAvatar")
    public ResponseResult<?> getAvatar() {
        return userInfoService.getAvatar();
    }

    @PostMapping("/uploadAvatar")
    public ResponseResult<?> uploadAvatar(@RequestParam MultipartFile file) {

        return userInfoService.uploadAvatar(file);
    }

}
