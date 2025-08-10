package com.board.global.infrastructure.slack;

import com.board.global.infrastructure.slack.dto.SlackResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class SlackService {

    private static final String HEADER_PREFIX = "Bearer ";

    @Value("${slack.api.oauth-token}")
    private String oauthToken;

    @Value("${slack.api.chat-post-url}")
    private String chatPostUrl;

    @Value("${slack.api.conversations-info-url}")
    private String conversationsInfoUrl;

    public SlackResponse send(String message) {
        try {
            return WebClient.builder()
                    .baseUrl(chatPostUrl)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + oauthToken)
                    .build()
                    .post()
                    .bodyValue(Map.of(
                            "channel", SlackChannel.NOTIFICATION.getChannelId(),
                            "text", message
                    ))
                    .retrieve()
                    .bodyToMono(SlackResponse.class)
                    .doOnNext(response -> {
                        if (!response.ok()) {
                            log.error("Slack API Error : " + response);
                        }
                    }).block();
        } catch (Exception e) {
            log.error("Slack API Error : " + e.getMessage());
            return null;
        }
    }

    public SlackResponse checkSlackChannel(String channel) {
        return WebClient.builder()
                .baseUrl(conversationsInfoUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, HEADER_PREFIX + oauthToken)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("channel", channel)
                        .build()
                )
                .retrieve()
                .bodyToMono(SlackResponse.class)
                .doOnNext(response -> {
                    if (!response.ok()) {
                        log.error("Slack API Error : " + response);
                    }
                }).block();
    }

}
