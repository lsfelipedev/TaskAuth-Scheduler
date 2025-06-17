package com.Notification.dtos.notification;

import com.Notification.domain.Channel;
import com.Notification.domain.Notification;
import com.Notification.domain.Status;

import java.time.LocalDateTime;

public record NotificationResponse (Long id,
                                    String message,
                                    LocalDateTime dateTime,
                                    NotificationUserResponse userSender,
                                    NotificationUserResponse userDestination,
                                    Status status,
                                    Channel channel){

}
