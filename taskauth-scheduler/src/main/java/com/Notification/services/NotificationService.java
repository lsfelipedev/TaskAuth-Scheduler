package com.Notification.services;

import com.Notification.domain.Notification;
import com.Notification.domain.Status;
import com.Notification.domain.User;
import com.Notification.dtos.notification.NotificationMsgDto;
import com.Notification.dtos.notification.NotificationResponse;
import com.Notification.dtos.notification.NotificationUserResponse;
import com.Notification.dtos.notification.RequestNotification;
import com.Notification.producer.EmailProducer;
import com.Notification.repositories.NotificationRepository;
import com.Notification.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailProducer emailProducer;


    @Transactional
    public void registerNotification(RequestNotification requestNotification){

        String login = SecurityContextHolder.getContext().getAuthentication().getName();

        User userLocalSender = userRepository.findByLogin(login);
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

        notificationRepository.save(notificationToSave);
    }

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
                localNotification.getSendAt(),
                userSender,
                userDestination,
                localNotification.getStatus(),
                localNotification.getChannel());
    }

    public void deleteNotificationById(Long idNotification) {
        var localNotification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new NoSuchElementException("Delete Failed! There is no Notification with this ID."));
        notificationRepository.delete(localNotification);
    }

    public void checkAndSend() {
        List<Notification> notificationList = notificationRepository
                .findByStatusAndSendAtBefore(Status.PENDING, LocalDateTime.now());

        if (!notificationList.isEmpty()) {

            List<Notification> notificationsToUpdate = notificationList.stream()
                    .peek(notification -> notification.setStatus(Status.PROCESS))
                    .toList();

            notificationRepository.saveAll(notificationsToUpdate);
            notificationsToUpdate.forEach(emailProducer::publishMessageEmail);
        }
    }

    public void updateStatusNotification(NotificationMsgDto dto){

        Notification notificationFound = notificationRepository
                .findById(dto.getNotificationId()).get();

        Status status = dto.getEmailSent() ? Status.SUCCESS : Status.FAILED;

        notificationFound.setStatus(status);
        notificationRepository.save(notificationFound);
    }
}