package com.board.service.mail;

import com.board.service.mail.request.MailServiceRequest;
import com.board.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MailServiceTest extends IntegrationTestSupport {

    @Autowired
    private MailService mailService;

    @Test
    @DisplayName("메일을 정상 발송한다.")
    void sendMail() {
        // given
        String email = "khghouse@naver.com";
        MailServiceRequest request = MailServiceRequest.of(email);

        // when
        mailService.sendSignupMail(request);
    }

    @Test
    @DisplayName("메일 템플릿 안, 변수가 바인딩 되지 않더라도 디폴트 값을 출력한다.")
    void sendMailNotVariables() {
        // given
        String email = "khghouse@naver.com";
        MailServiceRequest request = MailServiceRequest.of(email);

        // when
        mailService.sendSignupMail(request);
    }

    @Test
    @DisplayName("메일을 정상 발송한다.")
    void sendLeaveMail() {
        // given
        String email = "khghouse@naver.com";
        MailServiceRequest request = MailServiceRequest.of(email);

        // when
        mailService.sendLeaveMail(request);
    }

}