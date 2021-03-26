package com.example.club.security.filter;

import com.example.club.security.dto.ClubAuthMemberDTO;
import com.example.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    // 생성자로 JWTUtil 주입
    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil){

        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("---------------------------------------------------------");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

//        if(email == null){
//            throw new BadCredentialsException("email cannot be null");
//        }
//
//        return null;

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pw);

        // AuthenticationManager 사용
        return getAuthenticationManager().authenticate(authToken);
    }


    // 인증 성공처리
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException{

        log.info("------------------------ApiLoginFilter-----------------------------");
        log.info("successfulAuthentication: "+authResult);  //authResult : 인증 성공한 사용자 정보

        log.info(authResult.getPrincipal());

        // email address
        String email = ((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();
        String token = null;

        try{
            token = jwtUtil.generateToken(email);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);
        } catch (Exception e){
            e.printStackTrace();
        }

    }



}
