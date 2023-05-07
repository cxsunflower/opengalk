package com.opengalk.server.业务逻辑层.实现;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opengalk.server.实体类.UserInfo;
import com.opengalk.server.数据访问层.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserInfoMapper userInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String account) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        UserInfo queryUser = userInfoMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(queryUser)) {
            log.info("账号为空");
            return null;
        }
        log.info(queryUser.toString());
        return queryUser;
    }

}
