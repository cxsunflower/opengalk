package com.opengalk.server.业务逻辑层.实现;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.业务逻辑层.PaperService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.GZSubjectObject;
import com.opengalk.server.实体类.PaperInfo;
import com.opengalk.server.实体类.PaperRecord;
import com.opengalk.server.实体类.Subject;
import com.opengalk.server.工具类.LoginUserUtil;
import com.opengalk.server.数据访问层.PaperInfoMapper;
import com.opengalk.server.数据访问层.PaperRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaperServiceImpl extends ServiceImpl<PaperInfoMapper, PaperInfo>
        implements PaperService {

    private final PaperInfoMapper paperInfoMapper;

    private final PaperRecordMapper paperRecordMapper;

    private final LoginUserUtil loginUserUtil;

    @Override
    public ResponseResult<?> getPaperList(Integer currentPage, Integer pageSize, String condition, String keyword) {
        Page<PaperInfo> page = new Page<>(currentPage, pageSize);
        QueryWrapper<PaperInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.like(condition, keyword);
        if (loginUserUtil.getLoginUser().getAuthority() > 0) {
            queryWrapper.eq("create_by", loginUserUtil.getLoginUserID());
        }
        return new ResponseResult<>(1, null, paperInfoMapper.selectPage(page, queryWrapper));
    }

    @Override
    public ResponseResult<?> getPaperList(Integer type) {
        return new ResponseResult<>(1, null, paperInfoMapper.getPaperList(loginUserUtil.getLoginUserID(), type));
    }

    @Override
    public ResponseResult<?> getGZPaperById(String uuid, Integer containAnswer) {
        PaperInfo paperInfo = paperInfoMapper.selectById(uuid);

        if (ObjectUtils.isEmpty(paperInfo)) {
            return new ResponseResult<>(0, null, null);
        }

        GZSubjectObject[] GZSubjectObjects;

        if (containAnswer == 0) {
            GZSubjectObjects = paperInfoMapper.getGZPaperByIdWithNoAnswer(uuid);
        } else {
            GZSubjectObjects = paperInfoMapper.getGZPaperById(uuid);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", paperInfo.getName());
        map.put("remark", paperInfo.getRemark());
        map.put("subjectArray", GZSubjectObjects);

        log.info(Arrays.toString(GZSubjectObjects));
        return new ResponseResult<>(1, null, map);
    }

    @Override
    public ResponseResult<?> getXCPaperById(String uuid) {
        PaperInfo paperInfo = paperInfoMapper.selectById(uuid);

        if (ObjectUtils.isEmpty(paperInfo)) {
            return new ResponseResult<>(0, null, null);
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseResult<?> addGZPaper(PaperInfo paperInfo) {
        log.info(paperInfo.toString());
        String uuid = UUID.fastUUID().toString().replaceAll("-", "");
        paperInfoMapper.addGZPaper(uuid);

        Subject[] subjectArray = paperInfo.getSubjectArray();
        for (int i = 0; i < subjectArray.length; i++) {
            GZSubjectObject gzSubjectObject = GZSubjectObject.builder()
                    .uuid(uuid)
                    .id(i)
                    .type(subjectArray[i].getType())
                    .subject(subjectArray[i].getSubject())
                    .optionA(String.join("", subjectArray[i].getItems()[0].getText()))
                    .optionB(String.join("", subjectArray[i].getItems()[1].getText()))
                    .optionC(String.join("", subjectArray[i].getItems()[2].getText()))
                    .optionD(String.join("", subjectArray[i].getItems()[3].getText()))
                    .answer(String.join("", subjectArray[i].getAnswer()))
                    .build();

            paperInfoMapper.insertGZSubject(gzSubjectObject);
        }

        paperInfo.setId(uuid);
        paperInfo.setCreateBy(loginUserUtil.getLoginUserID());
        paperInfoMapper.insert(paperInfo);
        return new ResponseResult<>(1, "上传成功", null);
    }

    @Override
    public ResponseResult<?> submitPaper(PaperRecord paperRecord) {
        int score = 0;
        String id = paperRecord.getId();
        Long userId = loginUserUtil.getLoginUserID();
        String[] rightAnswerArray = paperInfoMapper.getAnswerById(id);
        String[] answerArray = paperRecord.getAnswer().split(",");
        for (int i = 0; i < rightAnswerArray.length; i++) {
            if (rightAnswerArray[i].equals(answerArray[i])) {
                score++;
            }
        }

        paperRecord.setUserId(userId);
        paperRecord.setScore(score);
        paperRecordMapper.insert(paperRecord);
        return new ResponseResult<>(1, "提交成功", score);
    }

    @Transactional
    @Override
    public ResponseResult<?> updateGZPaper(String uuid, PaperInfo paperInfo) {
        paperInfoMapper.deleteGZPaper(uuid);
        paperInfoMapper.addGZPaper(uuid);
        paperInfo.setId(uuid);
        paperInfoMapper.updateById(paperInfo);

        Subject[] subjectArray = paperInfo.getSubjectArray();
        for (int i = 0; i < subjectArray.length; i++) {
            GZSubjectObject gzSubjectObject = GZSubjectObject.builder()
                    .uuid(uuid)
                    .id(i)
                    .type(subjectArray[i].getType())
                    .subject(subjectArray[i].getSubject())
                    .optionA(String.join("", subjectArray[i].getItems()[0].getText()))
                    .optionB(String.join("", subjectArray[i].getItems()[1].getText()))
                    .optionC(String.join("", subjectArray[i].getItems()[2].getText()))
                    .optionD(String.join("", subjectArray[i].getItems()[3].getText()))
                    .answer(String.join("", subjectArray[i].getAnswer()))
                    .build();

            paperInfoMapper.insertGZSubject(gzSubjectObject);
        }

        return new ResponseResult<>(1, "更新成功", null);
    }

    @Override
    public ResponseResult<?> deletePaperById(String uuid) {
        return paperInfoMapper.deleteById(uuid) == 1
                ? new ResponseResult<>(1, "删除成功", null)
                : new ResponseResult<>(0, "删除失败", null);
    }
}
