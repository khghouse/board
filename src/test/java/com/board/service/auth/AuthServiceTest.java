package com.board.service.auth;

import com.board.IntegrationTestSupport;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exception.BusinessException;
import com.board.service.auth.request.LoginServiceRequest;
import com.board.service.auth.response.LoginResponse;
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
    void postLogin() {
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
        LoginResponse result = authService.postLogin(request);

        // then
        assertThat(result.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("로그인 시, 존재하지 않는 회원일 경우 예외가 발생한다.")
    void postLoginNotExistMember() {
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
        assertThatThrownBy(() -> authService.postLogin(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("존재하지 않는 계정입니다.");
    }

    @Test
    @DisplayName("로그인 시, 아이디 혹은 비밀번호가 틀렸을 경우 예외가 발생한다.")
    void postLoginInvalidData() {
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
        assertThatThrownBy(() -> authService.postLogin(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("아이디와 비밀번호를 다시 확인해 주세요.");
    }

}