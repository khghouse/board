package com.board.dto.security;

import com.board.domain.member.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class SecurityUser extends User {

    private final Long memberId;

    public SecurityUser(Member member) {
        super(member.getEmail(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.memberId = member.getId();
    }

}
