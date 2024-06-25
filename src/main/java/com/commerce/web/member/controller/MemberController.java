package com.commerce.web.member.controller;

import com.commerce.domain.member.entity.Verification;
import com.commerce.domain.member.service.EmailService;
import com.commerce.domain.member.service.MemberService;
import com.commerce.domain.member.service.VerificationService;
import com.commerce.web.member.dto.request.EmailSendDto;
import com.commerce.web.member.dto.request.MemberInfoUpdateRequestDto;
import com.commerce.web.member.dto.request.MemberJoinRequestDto;
import com.commerce.web.member.dto.request.MemberPasswordUpdateRequestDto;
import com.commerce.web.member.dto.response.MemberInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "MemberController")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final VerificationService verificationService;

    // 이메일 발송 API
    @PostMapping("/send-email")
    public ResponseEntity<?> validEmail(@RequestBody EmailSendDto dto) {

        Verification target = verificationService
                .findValidToken(dto.getTo(), false, false, false);
        // 이미 인증 메일이 존재하는 경우 -> 인증 토큰 만료 -> 새로운 메일 발송
        if (target != null
                && !target.isEmailVerification()
                && !target.isTokenExpire()
                && !target.isDelete()) {
            emailService.updateExpiredToken(target);
        }

        // 이메일 발송
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
    public ResponseEntity<?> memberJoin(@RequestBody @Valid MemberJoinRequestDto dto,
                                        BindingResult bindingResult) {
        // DTO 예외처리
        if (bindingResult.hasErrors()) {
            log.error("bindingResult = {}", bindingResult.getFieldErrors());
            return new ResponseEntity<>("오류", HttpStatus.BAD_REQUEST);
        }

        // 이메일 인증 토큰 유효성 체크
        boolean valid = verificationService.findVeri(dto.getEmail());
        if (valid) {
            Verification token = verificationService
                    .findValidToken(dto.getEmail(), true, true, false);
            if (token != null) {
                // 회원가입
                memberService.join(dto);
                verificationService.updateTokenKill(token);
                return new ResponseEntity<>("회원가입 성공.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("이메일 인증 후 다시 시도해주세요.", HttpStatus.BAD_REQUEST);
    }

    // 마이페이지 조회 API -- 관리자, 유저 권한만 접근 가능
    @GetMapping("/mypage")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> memberMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        MemberInfoResponse myPage = memberService.getMyPage(userDetails.getUsername());
        return new ResponseEntity<>(myPage, HttpStatus.OK);
    }

    // 주소, 전화번호 업데이트 API
    @PutMapping("/mypage/info")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> memberInfoUpdate(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody MemberInfoUpdateRequestDto dto) {
        memberService.updateMemberInfo(userDetails.getUsername(), dto);
        return new ResponseEntity<>("업데이트 완료..!", HttpStatus.OK);
    }

    // 비밀번호 변경 API
    @PutMapping("/mypage/password")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> memberPasswordUpdate(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody MemberPasswordUpdateRequestDto dto) {
        memberService.updateMemberPassword(userDetails.getUsername(), dto);
        return new ResponseEntity<>("업데이트 완료..!", HttpStatus.OK);
    }
}
