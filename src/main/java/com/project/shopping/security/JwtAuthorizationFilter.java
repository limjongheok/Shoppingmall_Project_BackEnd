package com.project.shopping.security;

import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private  Tokenprovider tokenprovider;
    private UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,Tokenprovider tokenprovider, UserRepository userRepository) {
        super(authenticationManager);
        this.tokenprovider = tokenprovider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소 요청이 됨");
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader:"+ jwtHeader);
        // wjt 토큰을 검증을 해서 정상적인 사용자인지 확인 하면 됨
        // header가 있는지 확인
        if(jwtHeader==null){
            System.out.println("null이여서 들어갈수 없습니다.");
        }
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer") || jwtHeader == "Bearer null"){
            chain.doFilter(request,response);
            return;
        }

        //헤더가 있으면
        String token = request.getHeader("Authorization").replace("Bearer ","");




        String userEmail = tokenprovider.validateAndGetUserEmail(token);

        System.out.println(userEmail);


        // 서명이 정상적으로 됨
        if(userEmail != null){
            System.out.println("username정상");
            User userEntity = userRepository.findByEmail(userEmail);
            System.out.println(userEntity.getRoles());

            // user 인증 객체 생성
            // 인증 객체는 서명을 통해서 만든는거 로그인 요청으로 처리한것은 아님 서명을 토큰 서명을 통한 객체
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null,principalDetails.getAuthorities());

            //강제로 시큐리티의 세션에 접근하여 Authentication객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response);


            // 만약 토큰이 만료시간 이 지났을시
            //refresh 토큰이 있을시 access토큰 재발급
            boolean booltoken = tokenprovider.booleanexp(token);
            if(!booltoken){
                token = tokenprovider.verifyToken(token,userEntity);
                response.addHeader("Authorization","Bearer "+token);


            }

        }



    }
}
