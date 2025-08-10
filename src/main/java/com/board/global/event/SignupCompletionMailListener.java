package com.board.global.event;

import com.board.global.infrastructure.mail.MailService;
import com.board.global.infrastructure.mail.dto.MailServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignupCompletionMailListener {

    private final MailService mailService;

    // @Async
    @EventListener
    public void onSignupCompletionMailListener(SignupCompletionMailEvent event) {
        mailService.sendSignupMail(MailServiceRequest.of(event.getEmail()));
    }

}
