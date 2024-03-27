package com.board.service.auth;

import com.board.IntegrationTestSupport;
import com.board.component.SecurityEncoder;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.service.auth.request.LoginServiceRequest;
import com.board.service.auth.response.LoginResponse;
import com.board.service.auth.request.SignupServiceRequest;
import com.board.service.auth.response.SignupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class AuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("로그인 시, 존재하는 회원임을 확인한다.")
    void login() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .deleted(false)
                .build();

        memberRepository.save(member);

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when
        LoginResponse result = authService.login(request);

        // then
        assertThat(result.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("로그인 시, 존재하지 않는 회원일 경우 예외가 발생한다.")
    void loginNotExistMember() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .deleted(false)
                .build();

        memberRepository.save(member);

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email("khghouse@naver.com")
                .password("Khghouse12!@")
                .build();

        // when, then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("존재하지 않는 계정입니다.");
    }

    @Test
    @DisplayName("로그인 시, 아이디 혹은 비밀번호가 틀렸을 경우 예외가 발생한다.")
    void loginInvalidData() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .deleted(false)
                .build();

        memberRepository.save(member);

        LoginServiceRequest request = LoginServiceRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@@")
                .build();

        // when, then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("아이디와 비밀번호를 다시 확인해 주세요.");
    }

    @Test
    @DisplayName("회원 정보를 등록하고 검증한다.")
    void signup() {
        // given
        SignupServiceRequest request = SignupServiceRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when
        SignupResponse result = authService.signup(request);

        // then
        Member member = memberRepository.findById(result.getId())
                .get();

        assertThat(result.getEmail()).isEqualTo("khghouse@daum.net");
        assertThat(SecurityEncoder.passwordEncoder().matches("Khghouse12!@", member.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이미 회원 가입된 이메일로 가입하면 예외가 발생한다.")
    void signupAlreadyJoined() {
        // given
        SignupServiceRequest request = SignupServiceRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        authService.signup(request);

        // when, then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }

}