package cn.edu.dlut.mail.wuchen2020.signinserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import cn.edu.dlut.mail.wuchen2020.signinserver.service.AuthService;

@SpringBootTest
class SignInServerApplicationTests {

    @Test
    void contextLoads() {}
    
    @Test
    void encodePasswordTest() {
        String md5Password = AuthService.encodeMD5("123456");
        System.out.println(md5Password);
    }

}
