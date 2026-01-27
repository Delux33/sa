package com.sa.service.impl;

import com.sa.entity.Pet;
import com.sa.repos.PetRepository;
import com.sa.service.IDefaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService implements IDefaultService<Pet> {

    private final PetRepository petRepo;

    @Override
    public ResponseEntity<List<Pet>> getAll() {
        List<Pet> pets = (List<Pet>) petRepo.findAll();
        return ResponseEntity.ok(pets);
    }

    @Override
    public ResponseEntity<Pet> getById(Long id) {
        return petRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Pet>> getByName(String petName) {
        List<Pet> pets = petRepo.findByName(petName);
        return ResponseEntity.ok(pets);
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
            return ResponseEntity.ok(savedPet);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public void deleteById(Long id) {
        petRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        petRepo.deleteAll();
    }

    public ResponseEntity<Page<Pet>> searchPetsByOwner(
            Long ownerId,
            int page,
            int size,
            String sortBy,
            String sortDir,
            String name,
            String nameContains
    ) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(direction, sortBy);

        List<Pet> baseList;
        if (name != null && !name.isBlank()) {
            baseList = petRepo.findByOwner_IdAndName(ownerId, name);
        } else if (nameContains != null && !nameContains.isBlank()) {
            baseList = petRepo.findByOwner_IdAndNameContainingIgnoreCase(ownerId, nameContains);
        } else {
            baseList = petRepo.findByOwner_Id(ownerId);
        }

        Comparator<Pet> comparator;
        switch (sortBy) {
            case "name" -> comparator = Comparator.comparing(Pet::getName, String.CASE_INSENSITIVE_ORDER);
            default -> comparator = Comparator.comparing(Pet::getId, Comparator.nullsLast(Long::compareTo));
        }
        if (direction == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        baseList.sort(comparator);

        int fromIndex = Math.min(page * size, baseList.size());
        int toIndex = Math.min(fromIndex + size, baseList.size());
        List<Pet> content = baseList.subList(fromIndex, toIndex);

        Page<Pet> result = new PageImpl<>(content, PageRequest.of(page, size, sort), baseList.size());
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Page<Pet>> getAllPets(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String name,
            String nameContains
    ) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(direction, sortBy);

        List<Pet> baseList;
        if (name != null && !name.isBlank()) {
            baseList = petRepo.findByName(name);
        } else if (nameContains != null && !nameContains.isBlank()) {
            baseList = petRepo.findByNameContainingIgnoreCase(nameContains);
        } else {
            baseList = (List<Pet>) petRepo.findAll();
        }

        Comparator<Pet> comparator;
        switch (sortBy) {
            case "name" -> comparator = Comparator.comparing(Pet::getName, String.CASE_INSENSITIVE_ORDER);
            case "id" -> comparator = Comparator.comparing(Pet::getId, Comparator.nullsLast(Long::compareTo));
            case "ownerId" -> comparator = Comparator.comparing(
                    p -> p.getOwner() != null ? p.getOwner().getId() : null,
                    Comparator.nullsLast(Long::compareTo)
            );
            default -> comparator = Comparator.comparing(Pet::getId, Comparator.nullsLast(Long::compareTo));
        }
        if (direction == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        baseList.sort(comparator);

        int fromIndex = Math.min(page * size, baseList.size());
        int toIndex = Math.min(fromIndex + size, baseList.size());
        List<Pet> content = baseList.subList(fromIndex, toIndex);

        Page<Pet> result = new PageImpl<>(content, PageRequest.of(page, size, sort), baseList.size());
        return ResponseEntity.ok(result);
    }
}
