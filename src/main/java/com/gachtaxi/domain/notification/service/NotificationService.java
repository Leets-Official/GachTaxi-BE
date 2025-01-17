package com.gachtaxi.domain.notification.service;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.domain.notification.dto.response.NotificationInfoResponse;
import com.gachtaxi.domain.notification.dto.response.NotificationResponse;
import com.gachtaxi.domain.notification.entity.Notification;
import com.gachtaxi.domain.notification.entity.enums.NotificationType;
import com.gachtaxi.domain.notification.exception.MemberNotMatchException;
import com.gachtaxi.domain.notification.exception.NotificationNotFoundException;
import com.gachtaxi.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gachtaxi.domain.notification.entity.enums.NotificationStatus.UNREAD;
import static com.gachtaxi.domain.notification.entity.enums.NotificationStatus.UNSENT;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    @Transactional
    public Slice<NotificationResponse> getNotifications(Long receiverId, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createDate"));

        Slice<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId, pageable);
        notifications.forEach(Notification::read);

        return notifications
                .map(NotificationResponse::from);
    }

    public NotificationInfoResponse getInfo(Long receiverId) {
        Integer count = notificationRepository.countAllByReceiverIdAndStatusAndStatus(receiverId, UNREAD, UNSENT);

        if (count > 0) {
            return new NotificationInfoResponse(count, true);
        }

        return new NotificationInfoResponse(count, false);
    }

    @Transactional
    public void sendWithPush(Long senderId, Members receiver, NotificationType type, String content) {
        Notification notification = Notification.of(senderId, receiver.getId(), type, content);

        notificationRepository.save(notification);
        fcmService.sendNotification(receiver.getFcmToken(), notification.getTitle(), notification.getContent());
    }

    @Transactional
    public void sendWithOutPush(Long senderId, Members receiver, NotificationType type, String content) {
        Notification notification = Notification.of(senderId, receiver.getId(), type, content);

        notificationRepository.save(notification);
    }

    @Transactional
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
}
