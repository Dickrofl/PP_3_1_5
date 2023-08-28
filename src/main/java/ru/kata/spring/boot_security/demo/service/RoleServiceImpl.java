package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Role> getListRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void setUserRoles(User user, Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null) {
            user.getRoles().add(role);
        }
    }
}
