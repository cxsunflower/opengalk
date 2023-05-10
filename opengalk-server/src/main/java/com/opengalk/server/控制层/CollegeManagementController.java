package com.opengalk.server.控制层;

import com.opengalk.server.业务逻辑层.实现.CollegeInfoServiceImpl;
import com.opengalk.server.实体类.CollegeInfo;
import com.opengalk.server.响应类.ResponseResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/collegeManagement")
@PreAuthorize("hasAuthority('0')")
@RequiredArgsConstructor
public class CollegeManagementController {

    private final CollegeInfoServiceImpl collegeInfoService;

    @GetMapping("/getCollegeList")
    public ResponseResult<?> getCollegeList(@Min(1) Integer currentPage, @Min(1) @Max(4) Integer pageSize, String condition, @Size(max = 12) String keyword) {
        log.info(condition, keyword);
        if (!("college_name".equals(condition) || "id".equals(condition))) {
            return new ResponseResult<>(0, "异常参数", null);
        }

        return collegeInfoService.getCollegeList(currentPage, pageSize, condition, keyword);
    }

    @GetMapping("/getCollegeInfo/{id}")
    public ResponseResult<?> getCollegeInfoById(@Min(1L) @PathVariable Long id) {
        return collegeInfoService.getCollegeInfoById(id);
    }


    @PostMapping("/addCollege")
    public ResponseResult<?> addCollege(@RequestBody @Valid CollegeInfo collegeInfo) {
        return collegeInfoService.addCollege(collegeInfo);
    }

    @PutMapping("/updateCollegeInfo")
    public ResponseResult<?> updateCollegeInfo(@RequestBody @Valid CollegeInfo collegeInfo) {
        return collegeInfoService.updateCollegeInfo(collegeInfo);

    }

    @DeleteMapping("/deleteCollege/{id}")
    public ResponseResult<?> deleteCollegeById(@Min(1L) @PathVariable Long id) {
        return collegeInfoService.deleteCollegeById(id);
    }
}
