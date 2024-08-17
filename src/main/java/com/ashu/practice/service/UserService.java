package com.ashu.practice.service;

import com.ashu.practice.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUser(Long userId);

    User updatePersonByID(Long userId, User user);

    void deleteUserByID(Long userId);
}
