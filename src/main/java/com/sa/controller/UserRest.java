package com.sa.controller;

import com.sa.entity.User;
import com.sa.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRest {

    @Autowired
    private UserRepository userRepos;

    @GetMapping
    public List<User> getAllUsers() {
        return (List<User>) userRepos.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepos.findById(id).orElse(null);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepos.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        if (userRepos.existsById(id)) {
            user.setId(id);
            return userRepos.save(user);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepos.deleteById(id);
    }
}
