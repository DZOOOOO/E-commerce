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
import com.commerce.web.member.dto.response.MemberResponseDto;
import com.commerce.web.member.dto.response.message.MemberMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<MemberResponseDto> validEmail(@Valid @RequestBody EmailSendDto dto) {

        Verification target = verificationService
                .findValidToken(dto.getTo(), false, false, false);
        // 이미 인증 메일이 존재하는 경우 -> 인증 토큰 만료 -> 새로운 메일 발송
        if (shouldExpireExistingToken(target)) {
            emailService.updateExpiredToken(target);
        }

        // 이메일 발송
        emailService.sendMail(dto);

        MemberResponseDto response = MemberResponseDto.builder()
                .message(MemberMessage.MAIL_SEND_OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 이미 존재하는 유효한 토큰을 만료시킬 필요가 있는지 확인합니다.
     *
     * @param target 검증할 대상 CacheVerification 객체
     * @return 만료시킬 필요가 있으면 true, 그렇지 않으면 false
     */
    private boolean shouldExpireExistingToken(Verification target) {
        return target != null
                && !target.isEmailVerification()
                && !target.isTokenExpire()
                && !target.isDeleteToken();
    }

    // 이메일 인증 API
    @GetMapping("/confirm-email")
    public ResponseEntity<MemberResponseDto> confirmEmail(@RequestParam String email,
                                                          @RequestParam String token,
                                                          @RequestParam boolean valid) {
        boolean isEmailValid = verificationService.checkEmail(email, token, valid);
        MemberMessage memberMessage = isEmailValid ? MemberMessage.MAIL_OK_AUTH
                : MemberMessage.MAIL_ALREADY_AUTH;
        HttpStatus status = isEmailValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        MemberResponseDto response = MemberResponseDto.builder()
                .message(memberMessage)
                .build();

        return new ResponseEntity<>(response, status);
    }

    // 회원가입 API
    @PostMapping
    public ResponseEntity<MemberResponseDto> memberJoin(@Valid @RequestBody MemberJoinRequestDto dto) {

        // 이메일 인증 토큰 유효성 체크
        boolean valid = verificationService.findVeri(dto.getEmail());
        if (valid) {
            Verification token = verificationService
                    .findValidToken(dto.getEmail(), true, true, false);
            if (token != null) {
                // 회원가입
                memberService.join(dto);
                verificationService.updateTokenKill(token);
                return new ResponseEntity<>(MemberResponseDto.builder()
                        .message(MemberMessage.MEMBER_JOIN_OK)
                        .build(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(MemberResponseDto.builder()
                .message(MemberMessage.MAIL_NOT_AUTH)
                .build(), HttpStatus.BAD_REQUEST);
    }

    // 마이페이지 조회 API -- 관리자, 유저 권한만 접근 가능
    @GetMapping("/mypage")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MemberInfoResponse> memberMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        MemberInfoResponse myPage = memberService.getMyPage(userDetails.getUsername());
        return new ResponseEntity<>(myPage, HttpStatus.OK);
    }

    // 주소, 전화번호 업데이트 API
    @PutMapping("/mypage/info")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MemberResponseDto> memberInfoUpdate(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody MemberInfoUpdateRequestDto dto) {
        memberService.updateMemberInfo(userDetails.getUsername(), dto);
        return new ResponseEntity<>(MemberResponseDto.builder()
                .message(MemberMessage.MEMBER_UPDATE_OK)
                .build(), HttpStatus.OK);
    }

    // 비밀번호 변경 API
    @PutMapping("/password")
    @PreAuthorize(value = "hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MemberResponseDto> memberPasswordUpdate(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestBody MemberPasswordUpdateRequestDto dto) {
        memberService.updateMemberPassword(userDetails.getUsername(), dto);
        return new ResponseEntity<>(MemberResponseDto.builder()
                .message(MemberMessage.MEMBER_UPDATE_OK)
                .build(), HttpStatus.OK);
    }

}
