package com.gachtaxi.domain.notification.repository;

import com.gachtaxi.domain.notification.entity.Notification;
import com.gachtaxi.domain.notification.entity.enums.NotificationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Integer countAllByReceiverIdAndStatusAndStatus(Long receiverId, NotificationStatus status, NotificationStatus newStatus);

    Slice<Notification> findAllByReceiverId(Long receiverId, Pageable pageable);
}
