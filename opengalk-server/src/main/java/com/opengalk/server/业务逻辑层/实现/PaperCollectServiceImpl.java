package com.opengalk.server.业务逻辑层.实现;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.业务逻辑层.PaperCollectService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.PaperCollect;
import com.opengalk.server.工具类.LoginUserUtil;
import com.opengalk.server.数据访问层.PaperCollectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cx
 * @description 针对表【paper_collect】的数据库操作Service实现
 * @createDate 2023-05-04 20:48:44
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaperCollectServiceImpl extends ServiceImpl<PaperCollectMapper, PaperCollect>
        implements PaperCollectService {

    private final PaperCollectMapper paperCollectMapper;

    private final LoginUserUtil loginUserUtil;

    @Override
    public ResponseResult<?> collect(@NotNull PaperCollect paperCollect) {
        String uuid = paperCollect.getUuid();
        Long userId = loginUserUtil.getLoginUserID();
        Integer subjectId = paperCollect.getSubjectId();

        String id = userId + "_" + uuid + "_" + subjectId;
        if (paperCollectMapper.selectById(id) != null) {
            return new ResponseResult<>(1, "该题目已收藏", null);
        }
        PaperCollect newPaperCollect = PaperCollect.builder()
                .uuid(uuid)
                .id(id)
                .subjectId(subjectId)
                .userId(userId)
                .build();

        return paperCollectMapper.insert(newPaperCollect) == 1
                ? new ResponseResult<>(1, "收藏成功", null)
                : new ResponseResult<>(0, "收藏失败", null);
    }

    @Override
    public ResponseResult<?> getCollectList() {
        List<PaperCollect> list = new ArrayList<>();
        Long id = loginUserUtil.getLoginUserID();
        String[] uuid = paperCollectMapper.getCollectPaperId(id);
        for (String tmp : uuid) {
            Collections.addAll(list, paperCollectMapper.getCollectList(tmp, id));
        }
        return new ResponseResult<>(1, null, list);
    }
}




