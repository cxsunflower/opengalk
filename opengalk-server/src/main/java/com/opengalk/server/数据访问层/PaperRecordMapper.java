package com.opengalk.server.数据访问层;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opengalk.server.实体类.PaperRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cx
 * @description 针对表【paper_record】的数据库操作Mapper
 * @createDate 2023-04-29 11:22:05
 * @Entity PaperRecord
 */
@Mapper
public interface PaperRecordMapper extends BaseMapper<PaperRecord> {

}




