package com.opengalk.server.业务逻辑层;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.PaperCollect;

/**
* @author cx
* @description 针对表【paper_collect】的数据库操作Service
* @createDate 2023-05-04 20:48:44
*/
public interface PaperCollectService extends IService<PaperCollect> {

    ResponseResult<?> collect(PaperCollect paperCollect);

    ResponseResult<?> getCollectList();
}
