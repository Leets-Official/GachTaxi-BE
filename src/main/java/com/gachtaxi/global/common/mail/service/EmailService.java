package com.gachtaxi.global.common.mail.service;

import com.gachtaxi.domain.members.exception.DuplicatedEmailException;
import com.gachtaxi.domain.members.exception.EmailFormInvalidException;
import com.gachtaxi.domain.members.repository.MemberRepository;
import com.gachtaxi.global.common.mail.exception.AuthCodeNotMatchException;
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
    private static final String GACHON_EMAIL_FORM = "@gachon.ac.kr";
    private static final int BOUND = 888888;
    private static final int OFFSET = 111111;

    private final SesClient sesClient;
    private final RedisUtil redisUtil;
    private final SecureRandom secureRandom = new SecureRandom();
    private final MemberRepository memberRepository;

    @Value("${aws.ses.templateName}")
    private String emailTemplateName;
    @Value("${aws.ses.from}")
    private String senderEmail;

    public void sendEmail(String recipientEmail) {
        checkGachonEmail(recipientEmail);
//        checkDuplicatedEmail(recipientEmail);

        /*
        * 1. 학교 이메일로 조회 시 유저가 존재하는 경우 + ACTIVE라면 다른 소셜로 로그인 했다는 의미
        * 따라서
        * 2. else -> 이메일로 인증 코드르 보낸다. (INACTIVE 유저여도 상관 X)
        * */

        String code = generateCode();
        redisUtil.setEmailAuthCode(recipientEmail, code);

        sendAuthCodeEmail(recipientEmail, code);
        log.info("\n Email: " + recipientEmail + "\n Code: " + code + "\n 전달");
    }

    public void checkEmailAuthCode(String recipientEmail, String inputCode) {
        String redisAuthCode = (String) redisUtil.getEmailAuthCode(recipientEmail);

        if(!redisAuthCode.equals(inputCode)) {
            throw new AuthCodeNotMatchException();
        }
    }

    // 인증 코드 검증

    /*
    * refactoring
    * */

    private void checkGachonEmail(String email){
        if(!email.endsWith(GACHON_EMAIL_FORM)){
            throw new EmailFormInvalidException();
        }
    }
    private void checkDuplicatedEmail(String email){
        // 여기서 member가 INACTIVE면 넘어가게 해야함. 무조건 중복 email이라고 넘기면 안됨.
        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new DuplicatedEmailException();
        });

    }

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
