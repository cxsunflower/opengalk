package com.opengalk.server.数据访问层;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opengalk.server.实体类.UserUpdateRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cx
 * @description 针对表【用户操作历史】的数据库操作Mapper
 * @createDate 2023-03-13 15:40:36
 * @Entity com.opengalk.server.数据层.UserOperateHistory
 */
@Mapper
public interface UserUpdateRecordMapper extends BaseMapper<UserUpdateRecord> {

}




