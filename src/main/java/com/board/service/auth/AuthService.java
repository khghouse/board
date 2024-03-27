package com.board.service.auth;

import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.provider.JwtTokenProvider;
import com.board.service.auth.request.LoginServiceRequest;
import com.board.service.auth.response.LoginResponse;
import com.board.service.auth.request.SignupServiceRequest;
import com.board.service.auth.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;

    @Transactional
    public LoginResponse login(LoginServiceRequest request) {
        Member member = memberRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new BusinessException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException("아이디와 비밀번호를 다시 확인해 주세요.");
        }

        return LoginResponse.of(generateAccessToken(member));
    }

    @Transactional
    public SignupResponse signup(SignupServiceRequest request) {
        validateAlreadyJoinedMember(request.getEmail());

        Member member = memberRepository.save(request.toEntity());

        return SignupResponse.of(member, generateAccessToken(member));
    }

    /**
     * 인증 정보를 담고 있는 JWT 액세스 토큰을 생성한다.
     */
    private String generateAccessToken(Member member) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
        String accessToken = jwtTokenProvider.createAccessToken(authenticate);
        return accessToken;
    }

    /**
     * 이미 가입된 회원인지 체크 by email
     */
    private void validateAlreadyJoinedMember(String email) {
        Optional<Member> optMember = memberRepository.findByEmailAndDeletedFalse(email);

        if (optMember.isPresent()) {
            throw new BusinessException("이미 가입된 이메일입니다.");
        }
    }

}
