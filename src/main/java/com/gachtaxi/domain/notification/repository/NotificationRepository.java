package com.gachtaxi.domain.notification.repository;

import com.gachtaxi.domain.notification.entity.Notification;
import com.gachtaxi.domain.notification.entity.enums.NotificationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Integer countAllByReceiverIdAndStatus(Long receiverId, NotificationStatus status);

    Slice<Notification> findAllByReceiverId(Long receiverId, Pageable pageable);
}
