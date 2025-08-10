package com.board.domain.member;

import com.board.global.security.SecurityEncoder;
import com.board.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @Test
    @DisplayName("이메일 형식을 검증한다.")
    void validateEmail() {
        // when
        Member member = toEntityByEmail("khghouse@daum.net");

        // then
        assertThat(member.getEmail()).isEqualTo("khghouse@daum.net");
    }

    @Test
    @DisplayName("이메일 도메인 부분에서 서브 도메인을 허용한다.")
    void validateEmailCase2() {
        // when
        Member member = toEntityByEmail("khghouse@mail.daum.net");

        // then
        assertThat(member.getEmail()).isEqualTo("khghouse@mail.daum.net");
    }

    @Test
    @DisplayName("이메일 도메인 부분에서 co.kr과 같은 케이스를 허용한다.")
    void validateEmailCase3() {
        // when
        Member member = toEntityByEmail("khghouse@mail.daum.coo.krrr");

        // then
        assertThat(member.getEmail()).isEqualTo("khghouse@mail.daum.coo.krrr");
    }

    @Test
    @DisplayName("이메일 로컬 부분에 공백이 포함되면 예외가 발생한다.")
    void invalidateEmail() {
        // when, then
        assertThatThrownBy(() -> toEntityByEmail("khg house@daum.net"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 이메일 형식입니다.");
    }

    @Test
    @DisplayName("이메일 로컬 부분에 허용되지 않은 특수문자가 포함되면 예외가 발생한다.")
    void invalidateEmailCase2() {
        // when, then
        assertThatThrownBy(() -> toEntityByEmail("khghouse*@daum.net"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 이메일 형식입니다.");
    }

    @Test
    @DisplayName("@가 1개를 초과하면 예외가 발생한다.")
    void invalidateEmailCase3() {
        // when, then
        assertThatThrownBy(() -> toEntityByEmail("khghouse@@daum.net"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 이메일 형식입니다.");
    }

    @Test
    @DisplayName("비밀번호가 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상의 문자열인지 확인한다.")
    void validatePassword() {
        // when
        Member member = toEntityByPassword("Khghouse12!@");

        // then
        assertThat(member.getPassword()).isNotNull();
    }

    @Test
    @DisplayName("비밀번호가 12자리 미만이면 예외가 발생한다.")
    void invalidatePasswordLessLength() {
        // when, then
        assertThatThrownBy(() -> toEntityByPassword("Khghouse12!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상 문자열이어야 합니다.");
    }

    @Test
    @DisplayName("비밀번호에 소문자가 없다면 예외가 발생한다.")
    void invalidatePasswordNoLowercase() {
        // when, then
        assertThatThrownBy(() -> toEntityByPassword("KHGHOUSE12!@"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상 문자열이어야 합니다.");
    }

    @Test
    @DisplayName("비밀번호에 대문자가 없다면 예외가 발생한다.")
    void invalidatePasswordNoUppercase() {
        // when, then
        assertThatThrownBy(() -> toEntityByPassword("khghouse12!@"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상 문자열이어야 합니다.");
    }

    @Test
    @DisplayName("비밀번호에 숫자가 없다면 예외가 발생한다.")
    void invalidatePasswordNoNumber() {
        // when, then
        assertThatThrownBy(() -> toEntityByPassword("Khghouse!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상 문자열이어야 합니다.");
    }

    @Test
    @DisplayName("비밀번호에 특수문자 없다면 예외가 발생한다.")
    void invalidatePasswordNoSpecialCharacter() {
        // when, then
        assertThatThrownBy(() -> toEntityByPassword("Khghouse1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상 문자열이어야 합니다.");
    }

    @Test
    @DisplayName("해싱된 비밀번호를 검증한다.")
    void hashPassword() {
        // when
        Member member = toEntityByPassword("Khghouse12!@");

        // then
        assertThat(SecurityEncoder.passwordEncoder().matches("Khghouse12!@", member.getPassword())).isTrue();
    }

    private static Member toEntityByEmail(String email) {
        return Member.builder()
                .email(email)
                .password("Khghouse12!@")
                .build();
    }

    private static Member toEntityByPassword(String password) {
        return Member.builder()
                .email("khghouse@daum.net")
                .password(password)
                .build();
    }

}