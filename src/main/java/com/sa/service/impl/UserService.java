package com.sa.service.impl;

import com.sa.entity.User;
import com.sa.repos.UserRepository;
import com.sa.service.IDefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IDefaultService<User> {

    @Autowired
    private UserRepository userRepo;

    public List<User> getAll() {
        return (List<User>) userRepo.findAll();
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public void create(User product) {
        userRepo.save(product);
    }

    public User update(Long id, User product) {
        if (userRepo.existsById(id)) {
            product.setId(id);
            return userRepo.save(product);
        }
        return null;
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public void deleteAll() {
        userRepo.deleteAll();
    }
}
