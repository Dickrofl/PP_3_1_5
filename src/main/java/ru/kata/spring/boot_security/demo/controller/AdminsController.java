package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
       ;
    }

    @GetMapping
    public String showUsersList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("currentUser", userService.findByUsername(userDetails.getUsername()));
        model.addAttribute("roles", roleService.getListRoles());
        return "admin";
    }

    @GetMapping("/new")
    public String showNewUserForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("currentUser", userService.findByUsername(userDetails.getUsername()));
        model.addAttribute("roles", roleService.getListRoles());
        model.addAttribute("user", new User());
        return "redirect:/new";
    }

    @PostMapping("/new")
    public String saveNewUser(@ModelAttribute("user") User user, @RequestParam("roleId") Long roleId) {
        roleService.setUserRoles(user, roleId);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/edit/{userId}/{roleId}")
    public String updateUser(
            @ModelAttribute("user") User user,
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        roleService.setUserRoles(user, roleId);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
