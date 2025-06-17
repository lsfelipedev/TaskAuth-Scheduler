package com.Notification.repositories;

import com.Notification.domain.Notification;
import com.Notification.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByStatusAndDateBefore(Status status, LocalDateTime localDateTime);

}
