package net.wanho.aouth_jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AouthMomeryApplicationTests {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void contextLoads() {
        //对明码进行 随机值加密，通一个明码 加密结果不一样
        String admin = bCryptPasswordEncoder.encode("app");
        System.out.println(admin);
    }

}
