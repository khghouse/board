package com.board.event;

import com.board.service.slack.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureSlackMessageListener {

    private final SlackService slackService;

    @Async
    @EventListener
    public void onSmtpAuthenticationFailure(AuthenticationFailureSlackMessageEvent event) {
        slackService.send(event.toString());
    }

}
