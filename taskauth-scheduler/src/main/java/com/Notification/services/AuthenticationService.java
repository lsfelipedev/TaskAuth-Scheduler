package com.Notification.services;

import com.Notification.domain.User;
import com.Notification.dtos.user.AuthenticationDTO;
import com.Notification.dtos.user.LoginResponseDTO;
import com.Notification.dtos.user.RegisterDTO;
import com.Notification.repositories.UserRepository;
import com.Notification.security.TokenService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationService(UserRepository repository,
                                 @Lazy AuthenticationManager authenticationManager,
                                 TokenService tokenService) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);
    }

    public LoginResponseDTO loginUser(AuthenticationDTO data){

        loginPasswordException(data);
        try{
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(),data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generate((User) auth.getPrincipal());
            return new LoginResponseDTO(token);
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("password invalid.");
        }
    }

    public User registerUser(RegisterDTO data) {
        loginPasswordException(data);
        String passwordBcrypt = new BCryptPasswordEncoder().encode(data.password());
        User userRegister = new User(data.login(), passwordBcrypt, data.email(), data.role());
        return this.repository.save(userRegister);
    }

    private void loginPasswordException(AuthenticationDTO data){
        if(data.login().isEmpty() || data.password().isEmpty())
            throw new NullPointerException("All fields must be filled in.");

        if (loadUserByUsername(data.login()) == null)
            throw new UsernameNotFoundException("login invalid.");

    } private void loginPasswordException(RegisterDTO data){

        if(data.login().isEmpty() || data.password().isEmpty()|| data.role().getRole().isEmpty())
            throw new NullPointerException("All fields must be filled in.");

        if (loadUserByUsername(data.login()) != null)
            throw new UsernameNotFoundException("This login is unavailable.");
    }

}