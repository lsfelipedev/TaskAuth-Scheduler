package com.Notification.dtos.notification;

import com.Notification.domain.Channel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RequestNotification(

        @NotNull(message = "dateTime cannot be empty.")
        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
        LocalDateTime dateTime,

        @NotEmpty(message = "message can not be a null or empty")
        String message,

        @NotEmpty(message = "loginSender can not be a null or empty")
        String loginSender,

        @NotEmpty(message = "loginDestination can not be a null or empty")
        String loginDestination,

        Channel channel) {
}
