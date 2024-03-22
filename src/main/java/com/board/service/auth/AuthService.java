package com.board.service.auth;

import com.board.component.Hashing;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.provider.JwtTokenProvider;
import com.board.service.auth.request.LoginServiceRequest;
import com.board.service.auth.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse postLogin(LoginServiceRequest request) {
        Member member = memberRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new BusinessException("존재하지 않는 계정입니다."));

        String encryptedPassword = Hashing.hash(request.getPassword(), member.getSalt());
        if (!encryptedPassword.equals(member.getPassword())) {
            throw new BusinessException("아이디와 비밀번호를 다시 확인해 주세요.");
        }

        return LoginResponse.of(jwtTokenProvider.createAccessToken(member.getId()));
    }

}
