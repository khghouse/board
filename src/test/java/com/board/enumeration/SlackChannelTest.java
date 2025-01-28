package com.board.enumeration;

import com.board.service.slack.response.SlackResponse;
import com.board.service.slack.SlackService;
import com.board.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SlackChannelTest extends IntegrationTestSupport {

    @Autowired
    private SlackService slackService;

    @Test
    @DisplayName("enum에 정의된 슬랙 채널이 실제 존재하는지 체크한다.")
    void existSlackChannel() {
        Stream.of(SlackChannel.values())
                .map(SlackChannel::getChannelId)
                .forEach(channel -> {
                    SlackResponse response = slackService.checkSlackChannel(channel);
                    System.out.println("Checking Slack Channel : " + channel + " -> Exists : " + response.isOk());
                    assertThat(response.isOk()).isTrue();
                });
    }

}