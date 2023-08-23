package ru.kata.spring.boot_security.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminsRestController {

    private final UserDaoImp userService;

    public AdminsRestController(UserDaoImp userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsersList() {
        System.out.println("Приложение начало работу метода getUsersList");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        System.out.println("Приложение начало работу метода getUser");
        User user = userService.getUserById(id);
        System.out.println(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("Приложение начало работу метода createUser");
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        System.out.println("Приложение начало работу метода saveUser");
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        System.out.println("Приложение начало работу метода deleteUser");
        String username = userService.getUserById(id).getUsername();
        userService.removeUserById(id);
        return new ResponseEntity<>(username, HttpStatus.ACCEPTED);
    }
}
