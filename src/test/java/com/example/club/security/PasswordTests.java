package com.example.club.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 패스워드 인코더로 나오는 값 확인 (매번 다르지만 matches()는 true)
    @Test
    public void testEncode(){

        String password = "1111";
        String enPw = passwordEncoder.encode(password);

        System.out.println("enpw: "+enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);

        System.out.println("matchResult: "+matchResult);

    }

}
