package com.board.domain.member;

import com.board.global.security.SecurityEncoder;
import com.board.domain.member.entity.Member;
import com.board.domain.member.repository.MemberRepository;
import com.board.support.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 정보를 등록하고 검증한다.")
    void save() {
        // given
        Member member = Member.builder()
                .email("khghouse@daum.net")
                .password("Khghouse12!@")
                .build();

        // when
        Member result = memberRepository.save(member);

        // then
        assertThat(result.getEmail()).isEqualTo("khghouse@daum.net");
        assertThat(SecurityEncoder.passwordEncoder().matches("Khghouse12!@", result.getPassword())).isTrue();
    }

}