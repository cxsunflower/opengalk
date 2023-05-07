package com.opengalk.server.业务逻辑层;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.PaperInfo;
import com.opengalk.server.实体类.PaperRecord;
import org.springframework.transaction.annotation.Transactional;

public interface PaperService extends IService<PaperInfo> {

    ResponseResult<?> getPaperList(Integer currentPage, Integer pageSize, String condition, String keyword);

    ResponseResult<?> getPaperList(Integer type);

    ResponseResult<?> getGZPaperById(String uuid, Integer containAnswer);

    ResponseResult<?> getXCPaperById(String uuid);

    @Transactional
    ResponseResult<?> addGZPaper(PaperInfo paperInfo);

    ResponseResult<?> submitPaper(PaperRecord paperRecord);

    @Transactional
    ResponseResult<?> updateGZPaper(String uuid, PaperInfo paperInfo);

    ResponseResult<?> deletePaperById(String id);

}
