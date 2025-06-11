package com.Notification.dtos.user;

public record RegisterDTO(String login, String password, String email, UserRole role) {
}
