package com.example.club.security;

import com.example.club.entity.ClubMember;
import com.example.club.entity.ClubMemberRole;
import com.example.club.repository.ClubMemeberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemeberTests {

    @Autowired
    private ClubMemeberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 더미데이터 넣기
    @Test
    public void insertDummies(){
        // 1-80까지는 USER만 지정
        // 81-90까지는 USER, MANAGER
        // 91-100까지는 USER, MANAGER, ADMIN

        IntStream.rangeClosed(1,100).forEach(i ->{
            ClubMember clubMember = ClubMember.builder()
                    .email("user"+i+"@example.com")
                    .name("사용자"+i)
                    .fromSocial(false)
                    .password( passwordEncoder.encode("1111") )
                    .build();

            //default role
            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i>80){
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }

            if(i>90){
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            repository.save(clubMember);
        });
    }


    // 이메일로 회원 조회
    @Test
    public void testRead(){
        Optional<ClubMember> result = repository.findByEmail("user85@example.com", false);
        ClubMember clubMember = result.get();
        System.out.println(clubMember);
    }

}
