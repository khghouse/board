package com.board.support.security;

import com.board.global.security.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class CustomSecurityUserFactory implements WithSecurityContextFactory<WithCustomSecurityUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomSecurityUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        SecurityUser securityUser = new SecurityUser(annotation.memberId(), annotation.username(), List.of(new SimpleGrantedAuthority("ROLE" + annotation.role())));

        Authentication auth = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }

}
