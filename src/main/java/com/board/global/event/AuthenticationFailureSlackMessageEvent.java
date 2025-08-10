package com.board.global.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationFailureSlackMessageEvent {

    private String serverName;
    private String className;
    private String errorTitle;
    private String errorMessage;

    @Builder(access = AccessLevel.PRIVATE)
    private AuthenticationFailureSlackMessageEvent(String serverName, String className, String errorTitle, String errorMessage) {
        this.serverName = serverName;
        this.className = className;
        this.errorTitle = errorTitle;
        this.errorMessage = errorMessage;
    }

    public static AuthenticationFailureSlackMessageEvent create(String serverName, String className, String errorTitle, String errorMessage) {
        return AuthenticationFailureSlackMessageEvent.builder()
                .serverName(serverName)
                .className(className)
                .errorTitle(errorTitle)
                .errorMessage(errorMessage)
                .build();
    }

    @Override
    public String toString() {
        return "[" + getCurrentDateTime() + "] " + errorTitle + "\n" +
                "- 에러 메시지 : " + errorMessage + "\n" +
                "- 서버 : " + serverName + "\n" +
                "- 클래스 : " + className + "\n";
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
