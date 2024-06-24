package com.commerce.domain.member.service;

import com.commerce.domain.member.entity.Member;
import com.commerce.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(member -> createUser(username, member))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 찾을 수 없는 이메일입니다."));
    }

    // JWT -> 메일, 권한.
    private User createUser(String memberName, Member member) {
        List<GrantedAuthority> authority
                = new ArrayList<>(List.of(new SimpleGrantedAuthority(member.getRole().getAuthority())));
        return new User(member.getEmail(), member.getPassword(), authority);
    }

}
