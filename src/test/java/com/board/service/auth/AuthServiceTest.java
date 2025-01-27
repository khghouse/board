package com.board.service.auth;

import com.board.support.IntegrationTestSupport;
import com.board.component.Redis;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.dto.jwt.JwtToken;
import com.board.exception.BusinessException;
import com.board.exception.JwtException;
import com.board.provider.JwtTokenProvider;
import com.board.service.auth.request.AuthServiceRequest;
import com.board.service.auth.request.ReissueServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class AuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private Redis redis;

    @Test
    @DisplayName("회원 가입에 성공한다.")
    void signup() {
        // given
        AuthServiceRequest request = AuthServiceRequest.of("khghouse@daum.net", "Password12#$");

        // when
        authService.signup(request);

        // then
        Member result = memberRepository.findByEmail("khghouse@daum.net")
                .orElseThrow(() -> new BusinessException("존재하지 않는 계정입니다."));
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("회원 가입을 시도했지만 이미 가입된 회원으로 예외가 발생한다.")
    void signupAlreadyJoinedMember() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .deleted(false)
                .build();

        memberRepository.save(member);

        AuthServiceRequest request = AuthServiceRequest.of("khghouse@daum.net", "Password12#$");

        // when, then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }

    @Test
    @DisplayName("이미 가입된 회원이면 로그인에 성공한다.")
    void login() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Password12#$")
                .deleted(false)
                .build();

        memberRepository.save(member);

        AuthServiceRequest request = AuthServiceRequest.of("khghouse@daum.net", "Password12#$");

        // when
        JwtToken result = authService.login(request);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 계정으로 로그인하면 예외가 발생한다.")
    void loginNotExistMember() {
        // given
        AuthServiceRequest request = AuthServiceRequest.of("khghouse@daum.net", "Password12#$");

        // when, then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("존재하지 않는 계정입니다.");
    }

    @Test
    @DisplayName("이미 가입된 회원이지만 비밀번호가 틀려 예외가 발생한다.")
    void loginInvalidData() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password(passwordEncoder.encode("Password12#$"))
                .deleted(false)
                .build();

        memberRepository.save(member);

        AuthServiceRequest request = AuthServiceRequest.of("khghouse@daum.net", "password123#$");

        // when, then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("아이디와 비밀번호를 다시 확인해 주세요.");
    }

    @Test
    @DisplayName("토큰을 이용하여 토큰을 재발행한다.")
    void reissueToken() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password(passwordEncoder.encode("Password12#$"))
                .deleted(false)
                .build();

        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("khghouse@daum.net", null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticate);

        redis.setRefreshToken(member.getId(), jwtToken.getRefreshToken());

        ReissueServiceRequest request = ReissueServiceRequest.of(jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        // when
        JwtToken result = authService.reissueToken(request);

        // then
        assertThat(result.getAccessToken()).isNotNull();
        assertThat(result.getRefreshToken()).isNotNull();

        // tearDown
        redis.deleteRefreshToken(member.getId());
    }

    @Test
    @DisplayName("토큰을 재발행하지만 존재하지 않는 계정으로 예외가 발생한다.")
    void reissueTokenNotExistMember() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password(passwordEncoder.encode("Password12#$"))
                .deleted(false)
                .build();

        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("khghouse@daum.net", null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticate);

        memberRepository.delete(member);

        redis.setRefreshToken(member.getId(), jwtToken.getRefreshToken());

        ReissueServiceRequest request = ReissueServiceRequest.of(jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        // when, Then
        assertThatThrownBy(() -> authService.reissueToken(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("존재하지 않는 계정입니다.");

        // tearDown
        redis.deleteRefreshToken(member.getId());
    }

    @Test
    @DisplayName("토큰을 재발행하지만 유효하지 않은 토큰으로 예외가 발생한다.")
    void reissueTokenInvalid() {
        // given
        ReissueServiceRequest request = ReissueServiceRequest.of("json.web.token", "json.web.token");

        // when, Then
        assertThatThrownBy(() -> authService.reissueToken(request))
                .isInstanceOf(JwtException.class)
                .hasMessage("손상된 토큰입니다.");
    }

    @Test
    @DisplayName("로그아웃하면 레디스에 저장된 액세스 토큰은 logout 되고, 리프레쉬 토큰은 삭제된다.")
    void logout() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password(passwordEncoder.encode("Password12#$"))
                .deleted(false)
                .build();

        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("khghouse@daum.net", null);
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticate);

        redis.setRefreshToken(member.getId(), jwtToken.getRefreshToken());
        String accessToken = jwtToken.getAccessToken();

        // when
        authService.logout(accessToken);

        // then
        assertThat(redis.get(accessToken)).isEqualTo("logout");
        assertThat(redis.get("refreshToken:" + member.getId())).isNull();

        // tearDown
        redis.deleteRefreshToken(member.getId());
    }

    @Test
    @DisplayName("유효하지 않는 토큰으로 로그아웃을 시도하면 예외가 발생한다.")
    void logoutInvalidToken() {
        // given
        String accessToken = "json.web.token";

        // when, Then
        assertThatThrownBy(() -> authService.logout(accessToken))
                .isInstanceOf(JwtException.class)
                .hasMessage("손상된 토큰입니다.");
    }

}