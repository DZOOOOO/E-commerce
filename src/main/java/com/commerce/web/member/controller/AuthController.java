package com.commerce.web.member.controller;

import com.commerce.jwt.JwtFilter;
import com.commerce.jwt.TokenProvider;
import com.commerce.web.member.dto.request.MemberLoginRequestDto;
import com.commerce.web.member.dto.response.TokenResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "AuthController")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 로그인.
    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponseDto> authorize(@Valid @RequestBody MemberLoginRequestDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        log.info("authenticationToken.getName() = {}", authenticationToken.getName());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        // 응답 객체 만들어 주기.
        return new ResponseEntity<>(new TokenResponseDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
