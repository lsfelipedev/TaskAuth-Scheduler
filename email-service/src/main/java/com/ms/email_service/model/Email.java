package com.ms.email_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_tb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Long notificationId;

    private String userId;

    private String emailFrom;

    private String emailTo;

    private String subject;

    private String text;

    private LocalDateTime dateSent;

}
