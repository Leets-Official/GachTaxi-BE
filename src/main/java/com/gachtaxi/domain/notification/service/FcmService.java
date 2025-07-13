package com.gachtaxi.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    public void sendNotification(String targetToken, String title, String body) {
        try{
            Message message = Message.builder()
                    .setToken(targetToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            handleException(e);
        } catch (Exception e) {
            log.error("푸시 알림 전송 중 문제 발생: {}", e.getMessage());
        }
    }

    private void handleException(FirebaseMessagingException exception) {
        int statusCode = exception.getHttpResponse().getStatusCode();
        String errorCode = exception.getErrorCode().toString();

        if (statusCode == 404) {
//            throw new FcmTokenNotFoundException(statusCode, errorCode);
            log.error("푸시 알림 전송 중 문제 발생: fcmToken is null");
        } else if (statusCode == 400) {
//            throw new InvalidFcmTokenException(statusCode, errorCode);
            log.error("푸시 알림 전송 중 문제 발생: fcmToken is invalid");
        }
    }
}
