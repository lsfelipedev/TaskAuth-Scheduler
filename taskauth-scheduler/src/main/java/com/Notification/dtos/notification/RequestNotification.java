package com.Notification.dtos.notification;

import com.Notification.domain.Channel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record RequestNotification(

        @Future(message = "dateTime must be in future.")
        @NotNull(message = "dateTime cannot be empty.")
        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss")
        LocalDateTime dateTime,

        @NotEmpty(message = "message can not be a null or empty")
        String message,

        @NotEmpty(message = "userDestination can not be a null or empty")
        String userDestination,

        Channel channel) {
}
