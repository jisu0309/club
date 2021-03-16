package com.example.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class Securityconfig extends WebSecurityConfigurerAdapter {

    // 패스워드 암호화
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 특정 리소스(URL)에 접근제한
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");
        http.formLogin();   //인증인가 문제시 로그인 화면으로
        http.csrf().disable();  //csrf토큰 발행 안하기 : 외부에서 REST로 이용하는 보안 설정 다루기 위해
        http.logout();
    }


    // UserDetailsService가 등록되었으므로 이 config는 사용X ///////////////////
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        //사용자 걔정은 user1
//        auth.inMemoryAuthentication().withUser("user1")
//                .password("$2a$10$peWNFhHYYNxumCj31BiF2e/MrI.w57nRDpXMKIZ8rNXVxVBrRUgem")
//                .roles("USER");
//    }

}
