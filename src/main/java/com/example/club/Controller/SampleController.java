package com.example.club.Controller;

import com.example.club.security.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    // 로그인 안해도 접근가능
    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public  void exAll(){
        log.info("exAll..................");
    }

    // 로그인 해야만 접근 가능
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/member")  // Authent~~Princpial : 로그인한 사용자 정보 파라메터로 받기
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
        log.info("exMember................");

        log.info("---------------------------------");
        log.info(clubAuthMemberDTO);

    }

    // 관리자 권한 있어야만 접근 가능
    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \"user2@example.com\"")   //특정 username만 접근가능
    @GetMapping("/admin")
    public String exAdmin(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){
        log.info("exAdmin.................");
        log.info(clubAuthMember);

        return "/sample/admin";
    }

}
