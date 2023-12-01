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

    @Override
    public List<User> getAll() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public List<User> getByName(String userName) {
        return userRepo.findByName(userName).stream().toList();
    }

    @Override
    public void create(User product) {
        userRepo.save(product);
    }

    @Override
    public User update(Long id, User product) {
        if (userRepo.existsById(id)) {
            product.setId(id);
            return userRepo.save(product);
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
