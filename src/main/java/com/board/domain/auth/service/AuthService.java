package com.board.domain.auth.service;

import com.board.domain.auth.dto.request.AuthServiceRequest;
import com.board.domain.auth.dto.request.ReissueServiceRequest;
import com.board.domain.member.entity.Member;
import com.board.domain.member.repository.MemberRepository;
import com.board.global.common.exception.ConflictException;
import com.board.global.common.exception.UnauthorizedException;
import com.board.global.event.SignupCompletionMailEvent;
import com.board.global.infrastructure.redis.Redis;
import com.board.global.security.JwtException;
import com.board.global.security.JwtToken;
import com.board.global.security.JwtTokenProvider;
import com.board.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.board.global.common.enumeration.ErrorCode.EMAIL_ALREADY_REGISTERED;
import static com.board.global.common.enumeration.ErrorCode.INVALID_CREDENTIALS;
import static com.board.global.security.JwtErrorCode.INVALID_TOKEN_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final Redis redis;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 회원 가입
     */
    @Transactional
    public void signup(AuthServiceRequest request) {
        validateAlreadyJoinedMember(request.getEmail());
        memberRepository.save(request.toEntity());
        eventPublisher.publishEvent(SignupCompletionMailEvent.create(request.getEmail()));
    }

    /**
     * 로그인
     */
    public JwtToken login(AuthServiceRequest request) {
        // 회원 인증 : 아이디에 해당하는 회원이 존재하는지 체크
        Member member = memberRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException(INVALID_CREDENTIALS));

        // 회원 인증 : 비밀번호가 일치하는지 체크
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }

        // JWT 생성
        JwtToken jwtToken = generateToken(member);

        // 리프레쉬 토큰을 레디스에 저장
        redis.setRefreshToken(member.getId(), jwtToken.getRefreshToken());

        return jwtToken;
    }

    /**
     * 토큰 재발행
     */
    public JwtToken reissueToken(ReissueServiceRequest request) {
        String refreshToken = request.getRefreshToken();

        // 리프레쉬 토큰 JWT 유효성 체크
        try {
            jwtTokenProvider.validateRefreshToken(refreshToken);
        } catch (JwtException e) {
            throw new JwtException(e.getJwtErrorCode());
        }

        // 만료된 액세스 토큰에서 회원을 식별할 수 있는 정보 추출
        Long memberId = jwtTokenProvider.getMemberIdByAccessToken(request.getAccessToken());

        // 회원 정보 조회
        Member member = memberRepository.findByIdAndDeletedFalse(memberId)
                .orElseThrow(() -> new JwtException(INVALID_TOKEN_USER));

        // 리프레쉬 토큰 비교
        redis.compareRefreshToken(memberId, refreshToken);

        // JWT 생성
        JwtToken jwtToken = generateToken(member);

        // 리프레쉬 토큰을 레디스에 저장
        redis.setRefreshToken(memberId, jwtToken.getRefreshToken());

        return jwtToken;
    }

    /**
     * 로그아웃
     */
    public void logout(String accessToken) {
        // 액세스 토큰 JWT 유효성 체크
        try {
            jwtTokenProvider.validateAccessToken(accessToken);
        } catch (JwtException e) {
            throw new JwtException(e.getJwtErrorCode());
        }

        // 액세스 토큰의 클레임 정보를 추출하여 인증 객체 생성
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // 인증 객체에서 회원 정보 추출
        SecurityUser member = (SecurityUser) authentication.getPrincipal();

        // 해당 회원의 리프레쉬 토큰을 레디스에서 조회 및 삭제
        redis.deleteRefreshToken(member.getMemberId());

        // 액세스 토큰을 레디스에 저장, 블랙 리스트 처리
        redis.logoutAccessToken(accessToken);
    }

    /**
     * 인증 정보를 담고 있는 JWT를 생성한다.
     */
    private JwtToken generateToken(Member member) {
        // UsernamePasswordAuthenticationToken 초기 인증 객체 생성 (권한 정보 없음)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), null);

        // SecurityAuthenticationProvider에 인증을 요청
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);

        // 토큰 생성
        return jwtTokenProvider.generateToken(authenticate);
    }

    /**
     * 이미 가입된 회원인지 체크 by email
     */
    private void validateAlreadyJoinedMember(String email) {
        Optional<Member> optMember = memberRepository.findByEmailAndDeletedFalse(email);

        if (optMember.isPresent()) {
            throw new ConflictException(EMAIL_ALREADY_REGISTERED);
        }
    }

}
