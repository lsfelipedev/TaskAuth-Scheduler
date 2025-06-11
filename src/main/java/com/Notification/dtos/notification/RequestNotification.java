package com.Notification.dtos.notification;

import com.Notification.domain.Channel;
import com.Notification.domain.Status;

import java.time.LocalDateTime;

public record RequestNotification(LocalDateTime dateTime,
                                  String message,
                                  String loginSender,
                                  String loginDestination,
                                  Status status,
                                  Channel channel) {
}
