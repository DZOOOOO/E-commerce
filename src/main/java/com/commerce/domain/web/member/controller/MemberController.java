package com.commerce.domain.web.member.controller;

import com.commerce.domain.member.entity.Verification;
import com.commerce.domain.member.service.EmailService;
import com.commerce.domain.member.service.MemberService;
import com.commerce.domain.member.service.VerificationService;
import com.commerce.domain.web.member.dto.EmailSendDto;
import com.commerce.domain.web.member.dto.MemberJoinDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    // 이메일 발송 API
    @PostMapping("/send-email")
    public ResponseEntity<?> validEmail(@RequestBody EmailSendDto dto) {
        boolean valid = verificationService.findVeri(dto.getTo());
        // 이미 인증된 메일인 경우
        if (valid) {
            return new ResponseEntity<>("이미 인증된 메일입니다.", HttpStatus.OK);
        }
        emailService.sendMail(dto);
        return new ResponseEntity<>("메일 발송..!", HttpStatus.OK);
    }

    // 이메일 인증 API
    @GetMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestParam String email,
                                          @RequestParam String token,
                                          @RequestParam boolean valid) {
        boolean checkEmail = verificationService.checkEmail(email, token, valid);
        if (checkEmail) {
            return new ResponseEntity<>("메일이 인증되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<>("이미 인증된 메일입니다.", HttpStatus.BAD_REQUEST);
    }

    // 회원가입 API
    @PostMapping("/join")
    public ResponseEntity<?> memberJoin(@RequestBody @Valid MemberJoinDto dto,
                                        BindingResult bindingResult) {
        // DTO 예외처리
        if (bindingResult.hasErrors()) {
            log.error("bindingResult = {}", bindingResult.getFieldErrors());
            return new ResponseEntity<>("오류", HttpStatus.BAD_REQUEST);
        }

        // 토큰 유효성 체크
        boolean valid = verificationService.findVeri(dto.getEmail());
        Verification token = verificationService.findByEmail(dto.getEmail());
        log.info("토큰 만료 = {}", token.isTokenExpire());
        if (valid) {
            // 토큰 만료
            token.updateTokenExpire(true);
            // 회원가입
            memberService.join(dto);
            return new ResponseEntity<>("회원가입 성공.", HttpStatus.OK);
        }

        // 토큰 만료
        token.updateTokenExpire(true);
        return new ResponseEntity<>("이메일 인증 후 다시 시도해주세요.", HttpStatus.BAD_REQUEST);
    }
}
