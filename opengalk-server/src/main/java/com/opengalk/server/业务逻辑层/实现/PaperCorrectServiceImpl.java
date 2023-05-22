package com.opengalk.server.业务逻辑层.实现;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.业务逻辑层.PaperCorrectService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.PaperCorrect;
import com.opengalk.server.工具类.LoginUserUtil;
import com.opengalk.server.数据访问层.PaperCorrectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author cx
 * @description 针对表【paper_correct】的数据库操作Service实现
 * @createDate 2023-05-04 20:48:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaperCorrectServiceImpl extends ServiceImpl<PaperCorrectMapper, PaperCorrect>
        implements PaperCorrectService {

    private final PaperCorrectMapper paperCorrectMapper;

    private final LoginUserUtil loginUserUtil;

    @Override
    public ResponseResult<?> correct(@NotNull PaperCorrect paperCorrect) {
        String uuid = paperCorrect.getUuid();
        Long userId = loginUserUtil.getLoginUserID();
        Integer subjectId = paperCorrect.getSubjectId();

        String id = userId + "_" + uuid + "_" + subjectId;
        if (paperCorrectMapper.selectById(id) != null) {
            return new ResponseResult<>(1, "该题目已提交过纠错", null);
        }
        PaperCorrect newPaperCorrect = PaperCorrect.builder()
                .uuid(uuid)
                .id(id)
                .subjectId(subjectId)
                .userId(userId)
                .correctText(paperCorrect.getCorrectText())
                .build();

        return paperCorrectMapper.insert(newPaperCorrect) == 1
                ? new ResponseResult<>(1, "提交成功", null)
                : new ResponseResult<>(0, "提交失败", null);
    }
}




