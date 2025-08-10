package com.board.global.infrastructure.mail;

import com.board.global.event.AuthenticationFailureSlackMessageEvent;
import com.board.global.infrastructure.mail.dto.MailServiceRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.application.name}")
    private String applicationName;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final ApplicationEventPublisher eventPublisher;

    public void sendSignupMail(MailServiceRequest mailServiceRequest) {
        this.send(MailTemplate.JOIN, mailServiceRequest.getEmail(), null);
    }

    public void sendLeaveMail(MailServiceRequest mailServiceRequest) {
        this.send(MailTemplate.LEAVE, mailServiceRequest.getEmail(), null);
    }

    private void send(MailTemplate mailTemplate, String email, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process(mailTemplate.getTemplate(), context);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject(mailTemplate.getSubject());
            helper.setText(htmlContent, true);

            mailTemplate.getAttachments()
                    .stream()
                    .map(ClassPathResource::new)
                    .filter(Resource::exists)
                    .filter(Resource::isFile)
                    .forEach(resource -> {
                        try {
                            helper.addAttachment(resource.getFilename(), resource);
                        } catch (MessagingException e) {
                            log.error("메일에 첨부파일을 추가하지 못했습니다. 파일명 : {}", resource.getFilename());
                        }
                    });
        } catch (MessagingException e) {
            log.error("[errorMessage] : {}, [to] : {}, [from] : {}", e.getMessage(), email, from);
        }

        try {
            javaMailSender.send(message);
        } catch (MailAuthenticationException e) { // SMTP 서버 인증 실패
            // 관리자한테 알림
            eventPublisher.publishEvent(AuthenticationFailureSlackMessageEvent.create(applicationName, this.getClass().getName(), "SMTP 서버 인증에 실패했습니다.", e.getMessage()));
            e.printStackTrace();
        } catch (MailException e) {
            log.error("메일 발송에 실패했습니다.");
            e.printStackTrace();
        }
    }

    private Map<String, Object> createMapFromNonNullValues(Object... variables) {
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < variables.length; i += 2) {
            String key = (String) variables[i];
            Object value = variables[i + 1];
            if (value != null) {
                map.put(key, value);
            }
        }

        return map;
    }

}
