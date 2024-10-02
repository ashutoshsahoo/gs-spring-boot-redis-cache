package com.ashu.practice.service.impl;

import com.ashu.practice.entity.User;
import com.ashu.practice.repo.UserRepository;
import com.ashu.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String REDIS_CACHE_NAME_USERS = "users";

    private final UserRepository userRepository;

    @Cacheable(cacheNames = REDIS_CACHE_NAME_USERS)
    @Override
    public List<User> getUsers() {
        log.info("Getting users");
        var users = userRepository.findAll();
        log.info("users: {}", users);
        return users;
    }

    //    @Cacheable(cacheNames = REDIS_CACHE_NAME_USERS, key = "#userId", unless = "#result.followers < 12000")
    @Cacheable(cacheNames = REDIS_CACHE_NAME_USERS, key = "#userId")
    @Override
    public User getUser(Long userId) {
        log.info("Getting user with ID {}.", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @CachePut(cacheNames = REDIS_CACHE_NAME_USERS, key = "#userId")
    @Override
    public User updatePersonByID(Long userId, User user) {
        return userRepository.save(user);
    }

    //    @CacheEvict(cacheNames = "users", allEntries = true)
    @CacheEvict(cacheNames = REDIS_CACHE_NAME_USERS, key = "#userId")
    @Override
    public void deleteUserByID(Long userId) {
        log.info("deleting person with userId {}", userId);
        userRepository.deleteById(userId);
    }
}
