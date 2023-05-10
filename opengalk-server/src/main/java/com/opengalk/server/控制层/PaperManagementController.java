package com.opengalk.server.控制层;

import com.opengalk.server.业务逻辑层.PaperService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.PaperInfo;
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
@RequestMapping("/paperManagement")
@PreAuthorize("hasAnyAuthority('0','1')")
@RequiredArgsConstructor
public class PaperManagementController {

    private final PaperService paperService;

    @GetMapping("/getPaperList")
    public ResponseResult<?> getPaperList(@Min(1) Integer currentPage, @Min(1) @Max(8) Integer pageSize, String condition, @Size(max = 12) String keyword) {
        log.info(condition, keyword);
        if (!("name".equals(condition) || "teacher_name".equals(condition))) {
            return new ResponseResult<>(0, "异常参数", null);
        }

        return paperService.getPaperList(currentPage, pageSize, condition, keyword);
    }

    @GetMapping("/getPaper/{uuid}")
    public ResponseResult<?> getPaperById(@PathVariable String uuid) {
        return paperService.getGZPaperById(uuid, 1);
    }

    @PostMapping("/addGZPaper")
    public ResponseResult<?> addGZPaper(@RequestBody @Valid PaperInfo paperInfo) {
        return paperService.addGZPaper(paperInfo);
    }

    @PutMapping("/updateGZPaper/{uuid}")
    public ResponseResult<?> updateGZPaper(@PathVariable String uuid, @RequestBody @Valid PaperInfo paperInfo) {
        return paperService.updateGZPaper(uuid, paperInfo);
    }

    @DeleteMapping("/deletePaper/{uuid}")
    public ResponseResult<?> deletePaper(@PathVariable String uuid) {
        return paperService.deletePaperById(uuid);
    }

}
