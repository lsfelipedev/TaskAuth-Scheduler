package com.Notification.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMsgDto{

    String userId;
    String emailFrom;
    String emailTo;
    String message;
}
