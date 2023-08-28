package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void saveUser(User user);

    void updateUser(User user);

    void removeUserById(long id);

    List<User> getAllUsers();

    User getUserById(long id);
    User findByUsername(String username);
    public UserDetails loadUserByUsername(String username);
}
