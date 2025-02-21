package com.board.enumeration;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ARTICLE_NOT_FOUND("게시글 정보가 존재하지 않습니다."),
    COMMENT_NOT_FOUND("댓글 정보가 존재하지 않습니다."),
    MEMBER_NOT_FOUND("존재하지 않는 계정입니다."),

    INVALID_CREDENTIALS("아이디와 비밀번호를 다시 확인해 주세요."),
    INVALID_AUTHENTICATION("인증 정보가 유효하지 않습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    INVALID_WRITER("작성자가 아닙니다."),

    EMAIL_ALREADY_REGISTERED("이미 가입된 이메일입니다."),
    ALREADY_DELETED("이미 삭제되었습니다."),
    LENGTH_EXCEEDED("글자 수 제한을 초과하였습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}
