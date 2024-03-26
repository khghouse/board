package com.board.service.member;

import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.provider.JwtTokenProvider;
import com.board.service.member.request.MemberServiceRequest;
import com.board.service.member.response.MemberResponse;
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
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse postMember(MemberServiceRequest request) {
        validateJoinedMemberByEmail(request.getEmail());

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Member member = memberRepository.save(request.toEntity(encryptedPassword));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createAccessToken(authenticate);

        return MemberResponse.of(member, accessToken);
    }

    private void validateJoinedMemberByEmail(String email) {
        Optional<Member> optMember = memberRepository.findByEmailAndDeletedFalse(email);

        if (optMember.isPresent()) {
            throw new BusinessException("이미 가입된 이메일입니다.");
        }
    }

}
