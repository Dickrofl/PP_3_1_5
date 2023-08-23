package ru.kata.spring.boot_security.demo.repository;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    void save(Role role);

    void setUserRoles(User user);

    Role getRole(Role role);

    Role findByName(String name);
}
