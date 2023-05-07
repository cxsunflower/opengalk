package com.opengalk.server.数据访问层;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opengalk.server.实体类.CollegeInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cx
 * @description 针对表【学校信息】的数据库操作Mapper
 * @createDate 2023-03-13 16:14:36
 * @Entity com.opengalk.server.实体类.CollegeInfo
 */
@Mapper
public interface CollegeInfoMapper extends BaseMapper<CollegeInfo> {

}




