package com.board.global.infrastructure.mail;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public enum MailTemplate {

    JOIN("회원 가입을 축하합니다.", "가입 축하 메일", "mail/join", List.of("/static/attachments/mail/join.txt", "/static/attachments/mail/join.pdf")),
    LEAVE("회원 탈퇴가 정상 처리되었습니다.", "회원 탈퇴 안내 메일", "mail/leave", Collections.emptyList());

    private final String subject;
    private final String description;
    private final String template; // thymeleaf
    private final List<String> attachments;

    MailTemplate(String subject, String description, String template, List<String> attachments) {
        this.subject = subject;
        this.description = description;
        this.template = template;
        this.attachments = attachments;
    }

}
