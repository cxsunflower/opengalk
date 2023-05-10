package com.opengalk.server.业务逻辑层.实现;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opengalk.server.实体类.UserLoginAndLogoutRecord;
import com.opengalk.server.业务逻辑层.UserLoginAndLogoutRecordService;
import com.opengalk.server.数据访问层.UserLoginAndLogoutRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author cx
 * @description 针对表【用户登陆注销记录】的数据库操作Service实现
 * @createDate 2023-03-15 19:02:33
 */
@Service
public class UserLoginAndLogoutRecordServiceImpl extends ServiceImpl<UserLoginAndLogoutRecordMapper, UserLoginAndLogoutRecord>
        implements UserLoginAndLogoutRecordService {

}




