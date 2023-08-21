package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.CustomUserDetailsService;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import java.security.Principal;



@Controller
public class AdminController {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    UserDao daoUserService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @GetMapping("/admin")
    public String getUsersPage(Principal principal, @ModelAttribute("user") User user,Model model) {
        String username = principal.getName();
        User userPrincipal = customUserDetailsService.findByUsername(username);
        model.addAttribute("principal", daoUserService.getUserById(userPrincipal.getId()));
        model.addAttribute("newUser", new User());
        model.addAttribute("user", daoUserService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin";
    }

    @PostMapping("/admin")
    public String addUser(@ModelAttribute("user") User user) {
        daoUserService.saveUser(user);
        return "redirect:/admin";
    }
    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        daoUserService.updateUser(user);
        return "redirect:/admin";
    }
    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") int id) {
        daoUserService.removeUserById(id);
        return "redirect:/admin";
    }


}