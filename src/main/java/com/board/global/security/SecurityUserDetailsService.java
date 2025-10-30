package com.board.global.security;

import com.board.domain.member.entity.Member;
import com.board.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Member member = memberService.getMemberByEmail(email);
            return new SecurityUser(member);
        } catch (Exception e) {
            throw new UsernameNotFoundException(email + "을 찾을 수 없습니다.");
        }
    }

}
