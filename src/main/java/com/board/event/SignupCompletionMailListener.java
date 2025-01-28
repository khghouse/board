package com.board.event;

import com.board.service.mail.MailService;
import com.board.service.mail.request.MailServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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
