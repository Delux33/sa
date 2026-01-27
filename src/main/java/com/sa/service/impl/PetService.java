package com.sa.service.impl;

import com.sa.entity.Pet;
import com.sa.repos.PetRepository;
import com.sa.service.IDefaultService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService implements IDefaultService<Pet> {

    private final PetRepository petRepo;


    @Override
    public List<Pet> getAll() {
        return (List<Pet>) petRepo.findAll();
    }

    @Override
    public Pet getById(Long id) {
        return petRepo.findById(id).orElse(null);
    }

    @Override
    public List<Pet> getByName(String petName) {
        return petRepo.findByName(petName);
    }

    @Override
    public void create(Pet pet) {
        petRepo.save(pet);
    }

    @Override
    public ResponseEntity<Pet> update(Long id, Pet pet) {
        if (petRepo.existsById(id)) {
            pet.setId(id);
            Pet savedPet = petRepo.save(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public void deleteById(Long id) {
        petRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        petRepo.deleteAll();
    }
}
