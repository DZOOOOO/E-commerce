package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import com.commerce.domain.product.entity.Product;
import com.commerce.domain.product.repository.ProductRepository;
import com.commerce.util.SecurityUtil;
import com.commerce.web.exception.member.MemberException;
import com.commerce.web.exception.member.MemberExceptionCode;
import com.commerce.web.exception.product.ProductException;
import com.commerce.web.exception.product.ProductExceptionCode;
import com.commerce.web.member.dto.request.MemberInfoUpdateRequestDto;
import com.commerce.web.member.dto.request.MemberJoinRequestDto;
import com.commerce.web.member.dto.request.MemberPasswordUpdateRequestDto;
import com.commerce.web.member.dto.response.MemberInfoResponse;
import com.commerce.web.order.dto.response.WishListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일 회원 조회
    @Transactional(readOnly = true)
    public Member findById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
    }

    // 회원가입
    @Transactional
    public void join(MemberJoinRequestDto dto) {
        String email = dto.getEmail();
        String encodePassword = passwordEncoder.encode(dto.getPassword());

        // 회원 중복 확인
        memberRepository.findByEmail(email).ifPresent(member
                -> {
            throw new MemberException(MemberExceptionCode.MEMBER_ALREADY);
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
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));

        // 마이페이지 위시리스트
        List<WishListResponseDto> result = member.getWishList().stream().map(w -> {
            Product product = productRepository.findById(w.getProductId())
                    .orElseThrow(() -> new ProductException(ProductExceptionCode.NOT_FOUND));
            return WishListResponseDto.builder()
                    .productId(w.getProductId())
                    .productName(product.getProductName())
                    .price(product.getProductPrice())
                    .build();
        }).toList();

        // 주문, wish List 조회 추가 예정.
        return MemberInfoResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .address(member.getAddress())
                .role(member.getRole().getAuthority())
                .wishList(result)
                .build();
    }

    // 전화번호, 주소 업데이트
    @Transactional
    public void updateMemberInfo(String email, MemberInfoUpdateRequestDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
        member.updateAddressPhone(dto.getAddress(), dto.getPhone());
        memberRepository.save(member);
    }

    // 비밀번호 업데이트
    @Transactional
    public void updateMemberPassword(String email, MemberPasswordUpdateRequestDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));

        String currentPassword = dto.getCurrentPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (currentPassword.equals(confirmPassword)) {
            if (passwordEncoder.matches(currentPassword, member.getPassword())) {
                String newPassword = passwordEncoder.encode(dto.getNewPassword());
                member.updatePassword(newPassword);
                memberRepository.save(member);
            } else {
                throw new MemberException(MemberExceptionCode.PASSWORD_ERROR);
            }
        } else {
            throw new MemberException(MemberExceptionCode.NOT_VALID);
        }
    }

    // Security Context 저장된 유저 권한 정보 추출
    @Transactional(readOnly = true)
    public Member getMyMemberWithAuthority() {
        return SecurityUtil.getCurrentUsername().flatMap(memberRepository::findByEmail)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.NOT_FOUND));
    }


}
