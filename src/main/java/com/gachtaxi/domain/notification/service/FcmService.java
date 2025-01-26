package com.gachtaxi.domain.notification.service;

import com.gachtaxi.domain.notification.exception.FcmTokenNotFoundException;
import com.gachtaxi.domain.notification.exception.InvalidFcmTokenException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        }
    }

    private void handleException(FirebaseMessagingException exception) {
        int statusCode = exception.getHttpResponse().getStatusCode();
        String errorCode = exception.getErrorCode().toString();

        if (statusCode == 404) {
            throw new FcmTokenNotFoundException(statusCode, errorCode);
        } else if (statusCode == 400) {
            throw new InvalidFcmTokenException(statusCode, errorCode);
        }
    }
}
