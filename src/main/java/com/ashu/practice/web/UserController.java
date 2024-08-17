package com.ashu.practice.web;

import com.ashu.practice.entity.User;
import com.ashu.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return this.userService.getUsers();
    }

    @GetMapping(path = "/{userId}")
    public User getUser(@PathVariable Long userId) {
        return this.userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    public User updatePersonByID(@PathVariable Long userId, @RequestBody User user) {
        return this.userService.updatePersonByID(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserByID(@PathVariable Long userId) {
        this.userService.deleteUserByID(userId);
    }
}
