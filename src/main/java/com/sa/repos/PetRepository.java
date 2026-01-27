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
}
