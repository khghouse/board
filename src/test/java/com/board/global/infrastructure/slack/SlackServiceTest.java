package com.board.global.infrastructure.slack;

import com.board.global.infrastructure.slack.SlackService;
import com.board.global.infrastructure.slack.dto.SlackResponse;
import com.board.support.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class SlackServiceTest extends IntegrationTestSupport {

    @Autowired
    private SlackService slackService;

    private String originalOauthToken;
    private String originalChatPostUrl;

    @BeforeEach
    void setUp() {
        originalOauthToken = (String) ReflectionTestUtils.getField(slackService, "oauthToken");
        originalChatPostUrl = (String) ReflectionTestUtils.getField(slackService, "chatPostUrl");
    }

    @AfterEach
    void tearDown() {
        ReflectionTestUtils.setField(slackService, "oauthToken", originalOauthToken);
        ReflectionTestUtils.setField(slackService, "chatPostUrl", originalChatPostUrl);
    }

    @Test
    @DisplayName("슬랙 채널로 메시지를 발송한다.")
    void send() {
        // given
        String message = "슬랙 채널로 발송하는 메시지입니다.";

        // when
        SlackResponse response = slackService.send(message);

        // then
        assertThat(response.ok()).isTrue();
    }

    @Test
    @DisplayName("슬랙 oauthToken이 유효하지 않다면 예외가 발생한다.")
    void sendInavlidAuth() {
        // given
        String message = "슬랙 채널로 발송하는 메시지입니다.";
        ReflectionTestUtils.setField(slackService, "oauthToken", "invalidOauthToken");

        // when
        SlackResponse slackResponse = slackService.send(message);

        // then
        assertThat(slackResponse.ok()).isFalse();
    }

    @Test
    @DisplayName("api url이 유효하지 않다면 예외가 발생한다.")
    void sendInvalidChatPostUrl() {
        // given
        String message = "슬랙 채널로 발송하는 메시지입니다.";
        ReflectionTestUtils.setField(slackService, "chatPostUrl", "https://slack.com/invalid/url");

        // when
        SlackResponse slackResponse = slackService.send(message);

        // then
        assertThat(slackResponse).isNull();
    }

}