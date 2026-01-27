package com.sa.service.impl;

import com.sa.entity.User;
import com.sa.repos.UserRepository;
import com.sa.service.IDefaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IDefaultService<User> {

    private final UserRepository userRepo;

    @Override
    public ResponseEntity<List<User>> getAll() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public ResponseEntity<User> getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<List<User>> getByName(String userName) {
        return userRepo.findByName(userName);
    }


    @Override
    public void create(User product) {
        userRepo.save(product);
    }

    @Override
    public ResponseEntity<User> update(Long id, User user) {
        if (userRepo.existsById(id)) {
            user.setId(id);
            return userRepo.save(user);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userRepo.deleteAll();
    }
}
