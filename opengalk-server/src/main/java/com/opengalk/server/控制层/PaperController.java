package com.opengalk.server.控制层;

import com.opengalk.server.业务逻辑层.PaperCollectService;
import com.opengalk.server.业务逻辑层.PaperCorrectService;
import com.opengalk.server.业务逻辑层.PaperService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.PaperCollect;
import com.opengalk.server.实体类.PaperCorrect;
import com.opengalk.server.实体类.PaperRecord;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/paper")
@PreAuthorize("hasAnyAuthority('0','1','2')")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    private final PaperCollectService paperCollectService;

    private final PaperCorrectService paperCorrectService;

    @GetMapping("/getPaperList")
    public ResponseResult<?> getPaperList(@Min(0) @Max(1) Integer type) {
        return paperService.getPaperList(type);
    }

    @GetMapping("/getPaper")
    public ResponseResult<?> getPaperWithNoAnswer(String uuid, @Min(0) @Max(1) Integer type) {
        if (type == 0) {
            return paperService.getGZPaperById(uuid, 0);
        }
        return new ResponseResult<>(0, null, null);
    }

    @GetMapping("/getPaperImgs")
    public ResponseResult<?> getPaperImgs(String uuid, @Min(0) @Max(99) Integer id) {
        return paperService.getPaperImgs(uuid, id);
    }

    @PostMapping("/submitGZPaper")
    public ResponseResult<?> submitGZPaper(@RequestBody @Valid PaperRecord paperRecord) {
        return paperService.submitPaper(paperRecord);
    }

    @PostMapping("/collect")
    public ResponseResult<?> collect(@RequestBody @Valid PaperCollect paperCollect) {
        return paperCollectService.collect(paperCollect);
    }

    @PostMapping("/correct")
    public ResponseResult<?> correct(@RequestBody @Valid PaperCorrect paperCorrect) {
        return paperCorrectService.correct(paperCorrect);
    }

}
