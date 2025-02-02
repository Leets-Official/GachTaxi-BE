package com.gachtaxi.domain.notification.service;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.notification.dto.response.NotificationInfoResponse;
import com.gachtaxi.domain.notification.dto.response.NotificationListResponse;
import com.gachtaxi.domain.notification.dto.response.NotificationPageableResponse;
import com.gachtaxi.domain.notification.dto.response.NotificationResponse;
import com.gachtaxi.domain.notification.entity.Notification;
import com.gachtaxi.domain.notification.entity.enums.NotificationType;
import com.gachtaxi.domain.notification.entity.payload.NotificationPayload;
import com.gachtaxi.domain.notification.exception.MemberNotMatchException;
import com.gachtaxi.domain.notification.exception.NotificationNotFoundException;
import com.gachtaxi.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gachtaxi.domain.notification.entity.enums.NotificationStatus.UNREAD;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    public NotificationListResponse getNotifications(Long receiverId, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createDate"));

        Slice<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId, pageable);

        notifications.stream()
                .filter(notification -> notification.getStatus() == UNREAD)
                .forEach(Notification::read);

        notificationRepository.saveAll(notifications);

        List<NotificationResponse> responses = notifications.stream()
                .map(NotificationResponse::from)
                .toList();

        NotificationPageableResponse pageableResponse = NotificationPageableResponse.from(notifications);

        return NotificationListResponse.of(responses, pageableResponse);
    }

    public NotificationInfoResponse getInfo(Long receiverId) {
        Integer count = notificationRepository.countAllByReceiverIdAndStatus(receiverId, UNREAD);

        if (count > 0) {
            return NotificationInfoResponse.of(count, true);
        }

        return NotificationInfoResponse.of(count, false);
    }

    public void sendWithPush(Long senderId, Members receiver, NotificationType type, String title, String content, NotificationPayload payload) {
        Notification notification = Notification.of(senderId, type, content, payload);

        notificationRepository.save(notification);
        fcmService.sendNotification(receiver.getFcmToken(), title, notification.getContent());
    }

    public void sendWithOutPush(Long senderId, Members receiver, NotificationType type, String content, NotificationPayload payload) {
        Notification notification = Notification.of(senderId, type, content, payload);

        notificationRepository.save(notification);
    }

    public void delete(Long receiverId, Long notificationId) {
        validateMember(receiverId, notificationId);

        notificationRepository.deleteById(notificationId);
    }

    private Notification find(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
    }

    private void validateMember(long receiverId, long notificationId) {
        Notification notification = find(notificationId);

        if (!notification.getReceiverId().equals(receiverId)) {
            throw new MemberNotMatchException();
        }
    }

    public boolean hasReceivedMatchingInvite(Long userId) {
        return notificationRepository.existsByReceiverIdAndType(
                userId, NotificationType.MATCH_INVITE
        );
    }
}
