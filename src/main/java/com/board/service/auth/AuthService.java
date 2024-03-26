package com.board.service.auth;

import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.provider.JwtTokenProvider;
import com.board.service.auth.request.LoginServiceRequest;
import com.board.service.auth.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;

    @Transactional
    public LoginResponse postLogin(LoginServiceRequest request) {
        Member member = memberRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new BusinessException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException("아이디와 비밀번호를 다시 확인해 주세요.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createAccessToken(authenticate);

        return LoginResponse.of(accessToken);
    }

}
