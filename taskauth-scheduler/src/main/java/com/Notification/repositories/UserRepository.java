package com.Notification.repositories;

import com.Notification.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {

    public User findByLogin(String login);

}
