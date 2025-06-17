package com.Notification.controllers;

import com.Notification.domain.User;
import com.Notification.dtos.user.AuthenticationDTO;
import com.Notification.dtos.user.RegisterDTO;
import com.Notification.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){

        return ResponseEntity.ok(service.loginUser(data));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterDTO registerData){

        service.registerUser(registerData);
        return ResponseEntity.ok().build();
    }
}
