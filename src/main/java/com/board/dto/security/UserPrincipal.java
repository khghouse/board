package com.board.dto.security;

import com.board.domain.member.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {

    private final Long memberId;

    public UserPrincipal(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        this.memberId = member.getId();
    }

    public Long getMemberId() {
        return memberId;
    }

}
