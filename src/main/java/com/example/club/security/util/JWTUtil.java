package com.example.club.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private String secretKey = "1q2w3e4r";  // 이 키를 이용해 Signature 생성

    private long expire = 60*24*30; // 만료기간 1달

    // jwt 토큰 생성
    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    // JWT 문자열 검증 ...및 인코딩된 문자열에서 원하는 값 추출
    public String validateAndExtract(String tokenStr) throws Exception {
        String contentValue = null;

        try{
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);

            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("----------------------------");

            contentValue = claims.getSubject();

        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }

        return contentValue;
    }



}
