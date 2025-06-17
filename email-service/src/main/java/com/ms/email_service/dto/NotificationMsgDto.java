package com.ms.email_service.dto;

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
