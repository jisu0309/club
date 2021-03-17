package com.example.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO  extends User implements OAuth2User {  //UserDetails 인터페이스인 User 클래스 상속

    private String email;

    private String name;

    private boolean fromSocial;

    // password는 부모 클래스를 사용하므로 별도 선언 x
    // ---> OAuth2User 타입을 이 DTO 타입으로 처리하려고 OAuth2User 임플

    private String password;

    private Map<String, Object> attr;   // OAuth2User는 Map 타입으로 모든 인증결과를 attributes로 가지고 있음


    // OAuth2
    public ClubAuthMemberDTO(String username,
                             String password,
                             boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> attr){

        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    public ClubAuthMemberDTO(String username,
                             String password,
                             boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }
}
