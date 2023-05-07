package com.opengalk.server.数据访问层;

import com.opengalk.server.实体类.UserLoginAndLogoutRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author cx
* @description 针对表【用户登陆注销历史】的数据库操作Mapper
* @createDate 2023-03-15 19:02:33
* @Entity com.opengalk.server.实体类.UserLoginAndLogoutHistory
*/
@Mapper
public interface UserLoginAndLogoutRecordMapper extends BaseMapper<UserLoginAndLogoutRecord> {

}




