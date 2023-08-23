package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDao;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.RoleService;
import ru.kata.spring.boot_security.demo.repository.UserRepository;


@Controller
@RequestMapping( "/admin")
public class AdminsController {
    private final UserDao userService;
    private final RoleService roleService;
    private final UserRepository userRepository;

    public AdminsController(UserDao userService, RoleService roleService, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }


    @GetMapping
    public String showUsersList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("currentUser", userRepository.findByUsername(userDetails.getUsername()));
        model.addAttribute("roles", roleService.findAll());
        return "admin";
    }

    @GetMapping("/new")
    public String showNewUserForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("currentUser", userRepository.findByUsername(userDetails.getUsername()));
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("user", new User());
        return "redirect:/new";
    }

    @PostMapping ("/new")
    public String saveNewUser(@ModelAttribute("user") User user) {

        roleService.setUserRoles(user);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PatchMapping ("/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user) {
        roleService.setUserRoles(user);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
