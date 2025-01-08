package com.gachtaxi.global.common.mail.service;

import com.gachtaxi.global.common.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.SendTemplatedEmailRequest;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String CODE_LENGTH = "%06d";
    private static final String CODE_FORMAT = "{\"code\":\"%s\"}";
    private static final int BOUND = 888888;
    private static final int OFFSET = 111111;

    private final SesClient sesClient;
    private final RedisUtil redisUtil;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${aws.ses.templateName}")
    private String emailTemplateName;
    @Value("${aws.ses.from}")
    private String senderEmail;

    public void sendEmail(String recipientEmail) {
        String code = generateCode();
        redisUtil.setEmailAuthCode(recipientEmail, code);

        sendAuthCodeEmail(recipientEmail, code);
        log.info(" Email: " + recipientEmail + "\n Code: " + code + "\n 전달");
    }

    // 인증 코드 검증

    /*
    * refactoring
    * */

    private String generateCode() {
        return String.format(CODE_LENGTH, secureRandom.nextInt(BOUND) + OFFSET);
    }

    // @Async 비동기 처리??
    private void sendAuthCodeEmail(String recipientEmail, String code) {
        String templateData = String.format(CODE_FORMAT, code);

        SendTemplatedEmailRequest request = SendTemplatedEmailRequest.builder()
                .destination(Destination.builder().toAddresses(recipientEmail).build())
                .template(emailTemplateName)
                .templateData(templateData)
                .source(senderEmail)
                .build();

        sesClient.sendTemplatedEmail(request);
    }


}
