package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import com.commerce.util.SecurityUtil;
import com.commerce.web.member.dto.request.MemberInfoUpdateRequestDto;
import com.commerce.web.member.dto.request.MemberPasswordUpdateRequestDto;
import com.commerce.web.member.dto.response.MemberInfoResponse;
import com.commerce.web.member.dto.request.MemberJoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일 회원 조회
    @Transactional(readOnly = true)
    public Member findById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    // 회원가입
    @Transactional
    public void join(MemberJoinRequestDto dto) {
        String email = dto.getEmail();
        String encodePassword = passwordEncoder.encode(dto.getPassword());

        // 회원 중복 확인
        memberRepository.findByEmail(email).ifPresent(member
                -> {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        });

        // 회원 가입.
        Member member = Member.builder()
                .email(dto.getEmail())
                .password(encodePassword)
                .name(dto.getName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .build();
        memberRepository.save(member);
    }

    // 마이 페이지 조회
    @Transactional(readOnly = true)
    public MemberInfoResponse getMyPage(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("없는 회원 입니다."));

        // 주문, wish List 조회 추가 예정.
        return MemberInfoResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .address(member.getAddress())
                .role(member.getRole().getAuthority())
                .build();
    }

    // 전화번호, 주소 업데이트
    @Transactional
    public void updateMemberInfo(String email, MemberInfoUpdateRequestDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));
        member.updateAddressPhone(dto.getAddress(), dto.getPhone());
        memberRepository.save(member);
    }

    // 비밀번호 업데이트
    @Transactional
    public void updateMemberPassword(String email, MemberPasswordUpdateRequestDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));

        String currentPassword = dto.getCurrentPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (currentPassword.equals(confirmPassword)) {
            if (passwordEncoder.matches(currentPassword, member.getPassword())) {
                String newPassword = passwordEncoder.encode(dto.getNewPassword());
                member.updatePassword(newPassword);
                memberRepository.save(member);
            } else {
                throw new IllegalArgumentException("입력한 비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("다시 입력해주세요.");
        }
    }

    // Security Context 저장된 유저 권한 정보 추출
    @Transactional(readOnly = true)
    public Member getMyMemberWithAuthority() {
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findByEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
    }


}
