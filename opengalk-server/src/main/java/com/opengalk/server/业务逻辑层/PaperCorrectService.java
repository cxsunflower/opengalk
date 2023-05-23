package com.opengalk.server.业务逻辑层;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opengalk.server.响应类.ResponseResult;
import com.opengalk.server.实体类.PaperCorrect;

/**
 * @author cx
 * @description 针对表【paper_correct】的数据库操作Service
 * @createDate 2023-05-04 20:48:44
 */
public interface PaperCorrectService extends IService<PaperCorrect> {

    ResponseResult<?> correct(PaperCorrect paperCorrect);
}
