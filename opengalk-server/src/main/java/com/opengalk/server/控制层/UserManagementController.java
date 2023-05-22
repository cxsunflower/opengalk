package com.opengalk.server.控制层;

import com.opengalk.server.业务逻辑层.CollegeInfoService;
import com.opengalk.server.业务逻辑层.UserInfoService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.UserInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/userManagement")
@PreAuthorize("hasAuthority('0')")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserInfoService userInfoService;

    private final CollegeInfoService collegeInfoService;

    @GetMapping("/getUserList")
    public ResponseResult<?> getUserList(@Min(1) Integer currentPage, @Min(1) @Max(8) Integer pageSize, String condition, @Size(max = 12) String keyword) {
        log.info(condition, keyword);
        if (!("account".equals(condition) || "name".equals(condition) || "id".equals(condition))) {
            return new ResponseResult<>(0, "异常参数", null);
        }

        return userInfoService.getUserList(currentPage, pageSize, condition, keyword);
    }

    @GetMapping("/getCollegeList")
    public ResponseResult<?> getCollegeList() {
        return collegeInfoService.getCollegeList();
    }

    @GetMapping("/getUserInfo/{id}")
    public ResponseResult<?> getUserInfo(@PathVariable @Min(1) Long id) {
        return userInfoService.getUserInfoById(id);
    }

    @PostMapping("/addUser")
    public ResponseResult<?> addUser(@Valid @RequestBody UserInfo userInfo) {
        return userInfoService.addUser(userInfo);
    }

    @PutMapping("/updateUserInfo")
    public ResponseResult<?> updateUserInfoByAdmin(@Valid @RequestBody UserInfo userInfo) {
        log.info(userInfo.toString());
        Integer authority = userInfo.getAuthority();
        Integer isLocked = userInfo.getIsLocked();
        if (ObjectUtils.isEmpty(authority) || ObjectUtils.isEmpty(isLocked)) {
            return new ResponseResult<>(0, "参数为空", null);
        }

        if ((authority >= 1 && authority <= 2) && (isLocked >= 0 && isLocked <= 1)) {
            return userInfoService.updateUserInfoById(userInfo, userInfo.getId());
        }

        return new ResponseResult<>(0, "异常参数", null);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseResult<?> deleteUserById(@Min(5) @PathVariable Long id) {
        return userInfoService.deleteUserById(id);
    }

    @PutMapping("/updateUserPassword/{id}")
    public ResponseResult<?> updateUserPassword(@PathVariable @Min(1) Long id, @RequestBody @Size(min = 6, max = 30) String newPassword) {
        log.info("新密码" + newPassword);
        return userInfoService.updateUserPassword(id, newPassword);
    }

}
