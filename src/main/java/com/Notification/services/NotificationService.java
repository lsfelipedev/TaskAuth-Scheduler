package com.Notification.services;

import com.Notification.domain.Notification;
import com.Notification.domain.Status;
import com.Notification.domain.User;
import com.Notification.dtos.notification.NotificationResponse;
import com.Notification.dtos.notification.NotificationUserResponse;
import com.Notification.dtos.notification.RequestNotification;
import com.Notification.repositories.NotificationRepository;
import com.Notification.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final StreamBridge streamBridge;

    @Transactional
    public void registerNotification(RequestNotification requestNotification){
        User userLocalSender = userRepository.findByLogin(requestNotification.userSender());
        User userLocalDestination = userRepository.findByLogin(requestNotification.userDestination());


        if(Objects.isNull(userLocalSender))
            throw new BadCredentialsException("Login Sender is invalid");

        if(Objects.isNull(userLocalDestination))
            throw new BadCredentialsException("Login Destination is invalid");

        var notificationToSave = new Notification(
                requestNotification,
                userLocalSender,
                userLocalDestination,
                Status.PENDING);

        if(notificationToSave.getDate().isBefore(LocalDateTime.now()))
            notificationToSave.setStatus(Status.SUCCESS);

        notificationRepository.save(notificationToSave);

        streamBridge.send("sendCommunication-out-0", requestNotification);

    }

    @Transactional
    public NotificationResponse findNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new NoSuchElementException("There is no Notification with this ID."));

        var userSender = new NotificationUserResponse(
                localNotification.getUserSender().getId(),
                localNotification.getUserSender().getLogin());

        var userDestination = new NotificationUserResponse(
                localNotification.getUserDestination().getId(),
                localNotification.getUserDestination().getLogin());

        return new NotificationResponse(
                localNotification.getId(),
                localNotification.getMessage(),
                localNotification.getDate(),
                userSender,
                userDestination,
                localNotification.getStatus(),
                localNotification.getChannel());
    }

    @Transactional
    public void deleteNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new NoSuchElementException("Delete Failed! There is no Notification with this ID."));
        notificationRepository.delete(localNotification);
    }

    @Transactional
    public void checkAndSend(LocalDateTime dateTime) {
        var notifications = notificationRepository.findByStatus(Status.PENDING);

        List<Notification> notificationsToUpdate = notifications.stream()
                .filter(notification -> !notification.getDate().isAfter(dateTime))
                .peek(notification -> notification.setStatus(Status.SUCCESS))
                .collect(Collectors.toList());

        if (!notificationsToUpdate.isEmpty())
            notificationRepository.saveAll(notificationsToUpdate);
    }

}