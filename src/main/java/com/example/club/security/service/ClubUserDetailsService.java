package com.example.club.security.service;

import com.example.club.entity.ClubMember;
import com.example.club.repository.ClubMemeberRepository;
import com.example.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemeberRepository clubMemeberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername "+username);

        // repo에서 email로 멤버 찾아오기
        Optional<ClubMember> result = clubMemeberRepository.findByEmail(username, false);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check Email or Social ");
        }

        ClubMember clubMember = result.get();

        // 찾아온 멤버 확인
        log.info("---------------------------");
        log.info(clubMember);


        // 멤버를 DTO로 변환 (UserDetails 타입으로 처리하려고)
        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream().   //role을 스프링 시큐리티에서 사용하는 SimpleGrantedAuthority로 변환
                        map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }
}
