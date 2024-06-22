package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import com.commerce.util.SecurityUtil;
import com.commerce.web.member.dto.MemberJoinRequestDto;
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
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

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

    // Security Context 저장된 유저 권한 정보 추출
    @Transactional(readOnly = true)
    public Member getMyMemberWithAuthority() {
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findByEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
    }
}
