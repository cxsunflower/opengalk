package com.opengalk.server.数据访问层;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opengalk.server.实体类.PaperCorrect;
import org.apache.ibatis.annotations.Mapper;

/**
* @author cx
* @description 针对表【paper_correct】的数据库操作Mapper
* @createDate 2023-05-04 20:48:44
* @Entity com.opengalk.server.实体类.PaperCorrect
*/
@Mapper
public interface PaperCorrectMapper extends BaseMapper<PaperCorrect> {

}




