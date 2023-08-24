package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceIml implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceIml(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void setUserRoles(User user) {
        user.setRoles(user.getRoles().stream()
                .map(this::getRole).collect(Collectors.toSet()));
    }

    @Override
    public Role getRole(Role role) {
        return roleRepository.getById(role.getId());
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
