package com.board.domain.member.entity;

import com.board.global.security.SecurityEncoder;
import com.board.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    private Boolean deleted;

    @Builder
    private Member(Long id, String email, String password, Boolean deleted) {
        validatePassword(password);

        this.id = id;
        this.email = validateEmail(email);
        this.password = hashPassword(password);
        this.deleted = deleted;
    }

    private String validateEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,})+$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
        }

        return email;
    }

    private void validatePassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상 문자열이어야 합니다.");
        }
    }

    private String hashPassword(String password) {
        return SecurityEncoder.passwordEncoder()
                .encode(password);
    }

}
