package com.ms.email_service.mapper;

import com.ms.email_service.dto.NotificationMsgDto;
import com.ms.email_service.model.Email;

public class EmailMapper {

    public static Email mapToEmail(NotificationMsgDto dto, Email email){
        email.setUserId(dto.getUserId());
        email.setEmailFrom(dto.getEmailFrom());
        email.setEmailTo(dto.getEmailTo());
        email.setText(dto.getMessage());
        return email;
    }
}
