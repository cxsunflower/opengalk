package com.opengalk.server;

import cn.hutool.core.lang.UUID;
import com.opengalk.server.业务逻辑层.UserInfoService;
import com.opengalk.server.数据访问层.PaperInfoMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;



@SpringBootTest
class OpengalkServerApplicationTests {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private PaperInfoMapper paperInfoMapper;


    @Test
    void testGetUserInfo() {
//       userInfoService.test();
    }

    @Test
    void addAdmin() {
//       userInfoService.register();
    }

    @Test
    void testPasswordEncoder() {
        String code = "123456";
        System.out.println(passwordEncoder.encode(code));
    }

    @Test
    void testPaper() {
        String id = UUID.fastUUID().toString().replaceAll("-", "");
        System.out.println(id);
        paperInfoMapper.addGZPaper(id);
    }

    @Test
    void testString(){
        System.out.println(String.join("",new String[]{"2","2"}));
    }

}
