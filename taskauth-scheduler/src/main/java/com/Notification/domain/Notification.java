package com.Notification.domain;

import com.Notification.dtos.notification.RequestNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private LocalDateTime sendAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private User userSender;

    @ManyToOne(fetch = FetchType.EAGER)
    private User userDestination;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    public Notification(LocalDateTime sendAt, String message, User userSender, User userDestination, Status status, Channel channel) {
        this.sendAt = sendAt;
        this.message = message;
        this.userSender = userSender;
        this.userDestination = userDestination;
        this.status = status;
        this.channel = channel;
    }

    public Notification(RequestNotification requestNotification, User userSender, User userDestination, Status status){
        this.sendAt = requestNotification.dateTime();
        this.message = requestNotification.message();
        this.userSender = userSender;
        this.userDestination = userDestination;
        this.status = status;
        this.channel = requestNotification.channel();
    }
}

