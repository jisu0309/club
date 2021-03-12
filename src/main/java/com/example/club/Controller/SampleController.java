package com.example.club.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    // 로그인 안해도 접근가능
    @GetMapping("/all")
    public  void exAll(){
        log.info("exAll..................");
    }

    // 로그인 해야만 접근 가능
    @GetMapping("/member")
    public void exMember(){
        log.info("exMember................");
    }

    // 관리자 권한 있어야만 접근 가능
    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin.................");
    }

}
