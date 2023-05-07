package com.opengalk.server.数据访问层;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opengalk.server.实体类.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cx
 * @description 针对表【用户信息】的数据库操作Mapper
 * @createDate 2023-03-14 14:32:58
 * @Entity com.opengalk.server.实体类.UserInfo
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo getUserInfo(Long id);

}




