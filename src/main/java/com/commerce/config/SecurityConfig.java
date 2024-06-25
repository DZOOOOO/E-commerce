package com.commerce.config;

import com.commerce.jwt.JwtAccessDeniedHandler;
import com.commerce.jwt.JwtAuthenticationEntryPoint;
import com.commerce.jwt.JwtSecurityConfig;
import com.commerce.jwt.TokenProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(TokenProvider tokenProvider,
                          CorsFilter corsFilter,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // token 방식 사용 -> csrf disable
                .csrf((csrf) -> csrf.disable())

                // 세션 X -> stateless 방식.
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // enable h2-console
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                // Cors 처리
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // resources 접근 허용.
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // h2 db 접근 누구나 가능
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        // 회원관련 API, 상품관련 API 누구나 접근가능.
                        .requestMatchers(
                                "/api/member/send-email",  // 인증 이메일 발송
                                "/api/member/confirm-email", // 이메일 인증 확인
                                "/api/member/join", // 회원가입
                                "/api/member/mypage", // GET - 마이페이지 조회
                                "/api/member/mypage/info", // PUT - 주소, 전화번호 업데이트
                                "/api/member/mypage/password", // 비밀번호 업데이트
                                "/api/authenticate", // 로그인
                                "/api/product/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // jwt 예외 처리 추가.
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .with(new JwtSecurityConfig(tokenProvider), customizer -> {
                });

        return http.build();
    }
}