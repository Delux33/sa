package com.sa.repos;

import com.sa.entity.Pet;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Hidden
@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {

    List<Pet> findByName(String petName);

    List<Pet> findByNameContainingIgnoreCase(String name);

    List<Pet> findByOwner_Id(Long ownerId);

    List<Pet> findByOwner_IdAndName(Long ownerId, String name);

    List<Pet> findByOwner_IdAndNameContainingIgnoreCase(Long ownerId, String name);
}
