package com.board.service.member;

import com.board.IntegrationTestSupport;
import com.board.component.Hashing;
import com.board.domain.member.Member;
import com.board.domain.member.MemberRepository;
import com.board.exceptions.BusinessException;
import com.board.service.member.request.MemberServiceRequest;
import com.board.service.member.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 정보를 등록하고 검증한다.")
    void postMember() {
        // given
        MemberServiceRequest request = MemberServiceRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when
        MemberResponse result = memberService.postMember(request);

        // then
        Member member = memberRepository.findById(result.getId())
                .get();

        assertThat(result).extracting("email", "password")
                .contains("khghouse@daum.net", Hashing.hash("Khghouse12!@", member.getSalt()));
    }

    @Test
    @DisplayName("이미 회원 가입된 이메일로 가입하면 예외가 발생한다.")
    void postMemberAlreadyJoined() {
        // given
        MemberServiceRequest request = MemberServiceRequest.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        memberService.postMember(request);

        // when, then
        assertThatThrownBy(() -> memberService.postMember(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }

}