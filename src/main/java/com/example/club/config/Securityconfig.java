package com.example.club.config;

import com.example.club.security.filter.ApiCheckFilter;
import com.example.club.security.filter.ApiLoginFilter;
import com.example.club.security.handler.ApiLoginFailHandler;
import com.example.club.security.handler.ClubLoginSuccessHandler;
import com.example.club.security.service.ClubUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class Securityconfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClubUserDetailsService userDetailsService;

    // 패스워드 암호화
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 특정 리소스(URL)에 접근제한
    @Override
    protected void configure(HttpSecurity http) throws Exception{
//        http.authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();   //인증인가 문제시 로그인 화면으로
        http.csrf().disable();  //csrf토큰 발행 안하기 : 외부에서 REST로 이용하는 보안 설정 다루기 위해
        http.logout();

        http.oauth2Login().successHandler(successHandler()); // 구글 로그인

        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(userDetailsService); // Remember me (7Days)

        //filterChain 사이에 내가 만든 filter 순서 지정해서 넣어주기
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    // 로그인 성공 이후 처리
    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }


    // UserDetailsService가 등록되었으므로 이 config는 사용X ///////////////////
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        //사용자 걔정은 user1
//        auth.inMemoryAuthentication().withUser("user1")
//                .password("$2a$10$peWNFhHYYNxumCj31BiF2e/MrI.w57nRDpXMKIZ8rNXVxVBrRUgem")
//                .roles("USER");
//    }

        @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{

        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");   //동작할 경로 지정
        apiLoginFilter.setAuthenticationManager(authenticationManager());   //AuthenticationManager

        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());  // 인증실패시 처리 핸들러

        return apiLoginFilter;
    }


    // OncePerRequestFilter
    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/notes/**/*");   // "/notes/.."로 시작하는 경우에만 동작하게 패턴 사용
    }
}
