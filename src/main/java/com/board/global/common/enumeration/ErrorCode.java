package com.board.global.common.enumeration;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;

@Getter
public enum ErrorCode {

    // 응답 상태 코드 디폴트
    UNAUTHORIZED("인증 정보가 올바르지 않습니다."),
    BUSINESS_ERROR("비즈니스 정책 위반"),

    // 401
    INVALID_CREDENTIALS("아이디와 비밀번호를 다시 확인해 주세요."),

    // 403
    INVALID_WRITER("작성자가 아닙니다."),

    // 404
    ARTICLE_NOT_FOUND("게시글 정보가 존재하지 않습니다."),
    COMMENT_NOT_FOUND("댓글 정보가 존재하지 않습니다."),
    MEMBER_NOT_FOUND("존재하지 않는 계정입니다."),

    // 409
    EMAIL_ALREADY_REGISTERED("이미 가입된 이메일입니다."),
    ALREADY_DELETED("이미 삭제되었습니다."),

    // 422
    LENGTH_EXCEEDED("글자 수 제한을 초과하였습니다. [최대 {0}자]"),
    INVALID_EMAIL_FORMAT("잘못된 이메일 형식입니다."),
    INVALID_PASSWORD_FORMAT("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자가 최소 1개 이상 포함된 12자리 이상 문자열이어야 합니다.");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        if (ObjectUtils.isEmpty(args)) {
            return this.message;
        }
        this.message = MessageFormat.format(message, args);
        return this.message;
    }

}
