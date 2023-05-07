package com.opengalk.server.业务逻辑层.实现;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.业务逻辑层.PaperRecordService;
import com.opengalk.server.实体类.PaperRecord;
import com.opengalk.server.数据访问层.PaperRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author cx
* @description 针对表【paper_record】的数据库操作Service实现
* @createDate 2023-04-29 11:22:05
*/
@Service
public class PaperRecordServiceImpl extends ServiceImpl<PaperRecordMapper, PaperRecord>
    implements PaperRecordService {

}




