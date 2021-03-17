package com.example.club.security.service;

import com.example.club.entity.ClubMember;
import com.example.club.entity.ClubMemberRole;
import com.example.club.repository.ClubMemeberRepository;
import com.example.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemeberRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        log.info("---------------------------------------");
        log.info("userRequest: "+userRequest);  // OAuth2UserRequest 객체

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: "+clientName);    // Google로 출력
        log.info(userRequest.getAdditionalParameters());

        // 최종 결과 가지는 객체
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==============================");
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k +":"+ v); // sub, picture, email, email_verified, EMAIL 등 출력
        });

        String email = null;

        // 구글 이용하는 경우
        if(clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");
        }
        log.info("EMAIL: " + email);

        // 회원정보 내 db에 저장
//        ClubMember member = saveSocialMember(email);
//
//        return oAuth2User;

        // 회원정보 내 db에 저장
        ClubMember member = saveSocialMember(email);

        // 프론트로 전달해주려고 DTO로 저장
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,   //fromSocial
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_"+role.name()) )
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        clubAuthMember.setName(member.getName());

        return clubAuthMember;
    }



    // OAuth로 받아온 유저 정보 내 db에 저장하기
    private ClubMember saveSocialMember(String email){

        // 기존에 동일한 이메일로 가입한 회원 있는 경우에는 조회만
        Optional<ClubMember> result = repository.findByEmail(email, true);

        if(result.isPresent()){
            return result.get();
        }

        // 기존에 없는 메일이면 회원 추가 (이름은 걍 메일주소, 패스워드는 1111)
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);

        repository.save(clubMember);

        return clubMember;
    }

}
