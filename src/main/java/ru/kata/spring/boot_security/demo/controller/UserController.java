package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.CustomUserDetailsService;
import java.security.Principal;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    UserDao daoUserService;

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/user")
    public String getUserPage(Principal principal, Model model) {
        String username = principal.getName();
        User user = customUserDetailsService.findByUsername(username);
        model.addAttribute("currentUser", daoUserService.getUserById(user.getId()));
        return "user";
    }
}
