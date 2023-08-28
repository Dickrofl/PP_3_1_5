package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/user")
    public String getUserPage(Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("currentUser", userService.getUserById(user.getId()));
        return "user";
    }
}
