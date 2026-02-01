package com.sa.service.impl;

import com.sa.entity.Pet;
import com.sa.entity.User;
import com.sa.repos.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PetService Tests")
class PetServiceTest {

    @Mock
    private PetRepository petRepo;

    @InjectMocks
    private PetService petService;

    private User owner;
    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        owner = new User("John", "Doe", "Smith");
        owner.setId(1L);

        pet1 = new Pet("Buddy", owner);
        pet1.setId(1L);

        pet2 = new Pet("Max", owner);
        pet2.setId(2L);
    }

    @Test
    @DisplayName("Should get all pets")
    void shouldGetAllPets() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<List<Pet>> response = petService.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(petRepo).findAll();
    }

    @Test
    @DisplayName("Should get pet by ID when exists")
    void shouldGetPetByIdWhenExists() {
        when(petRepo.findById(1L)).thenReturn(Optional.of(pet1));

        ResponseEntity<Pet> response = petService.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Buddy", response.getBody().getName());
        verify(petRepo).findById(1L);
    }

    @Test
    @DisplayName("Should return not found when pet does not exist")
    void shouldReturnNotFoundWhenPetDoesNotExist() {
        when(petRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Pet> response = petService.getById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(petRepo).findById(99L);
    }

    @Test
    @DisplayName("Should get pets by name")
    void shouldGetPetsByName() {
        when(petRepo.findByName("Buddy")).thenReturn(Collections.singletonList(pet1));

        ResponseEntity<List<Pet>> response = petService.getByName("Buddy");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Buddy", response.getBody().get(0).getName());
        verify(petRepo).findByName("Buddy");
    }

    @Test
    @DisplayName("Should create pet")
    void shouldCreatePet() {
        petService.create(pet1);

        verify(petRepo).save(pet1);
    }

    @Test
    @DisplayName("Should update pet when exists")
    void shouldUpdatePetWhenExists() {
        when(petRepo.existsById(1L)).thenReturn(true);
        when(petRepo.save(any(Pet.class))).thenReturn(pet1);

        ResponseEntity<Pet> response = petService.update(1L, pet1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(petRepo).existsById(1L);
        verify(petRepo).save(pet1);
    }

    @Test
    @DisplayName("Should return not found when updating non-existent pet")
    void shouldReturnNotFoundWhenUpdatingNonExistentPet() {
        when(petRepo.existsById(99L)).thenReturn(false);

        ResponseEntity<Pet> response = petService.update(99L, pet1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(petRepo).existsById(99L);
        verify(petRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should delete pet by ID")
    void shouldDeletePetById() {
        petService.deleteById(1L);

        verify(petRepo).deleteById(1L);
    }

    @Test
    @DisplayName("Should delete all pets")
    void shouldDeleteAllPets() {
        petService.deleteAll();

        verify(petRepo).deleteAll();
    }

    // Tests for searchPetsByOwner
    @Test
    @DisplayName("Should search pets by owner with no filters")
    void shouldSearchPetsByOwnerNoFilters() {
        when(petRepo.findByOwner_Id(1L)).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "id", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should search pets by owner with name filter")
    void shouldSearchPetsByOwnerWithNameFilter() {
        when(petRepo.findByOwner_IdAndName(1L, "Buddy")).thenReturn(Collections.singletonList(pet1));

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "id", "ASC", "Buddy", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should search pets by owner with nameContains filter")
    void shouldSearchPetsByOwnerWithNameContainsFilter() {
        when(petRepo.findByOwner_IdAndNameContainingIgnoreCase(1L, "Bud")).thenReturn(Collections.singletonList(pet1));

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "id", "ASC", null, "Bud");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should sort pets by name ASC")
    void shouldSortPetsByNameAsc() {
        when(petRepo.findByOwner_Id(1L)).thenReturn(Arrays.asList(pet2, pet1)); // Max, Buddy

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "name", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Buddy", response.getBody().getContent().get(0).getName());
    }

    @Test
    @DisplayName("Should sort pets by name DESC")
    void shouldSortPetsByNameDesc() {
        when(petRepo.findByOwner_Id(1L)).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "name", "DESC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Max", response.getBody().getContent().get(0).getName());
    }

    // Tests for getAllPets
    @Test
    @DisplayName("Should get all pets with no filters")
    void shouldGetAllPetsNoFilters() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "id", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should get all pets with name filter")
    void shouldGetAllPetsWithNameFilter() {
        when(petRepo.findByName("Buddy")).thenReturn(Collections.singletonList(pet1));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "id", "ASC", "Buddy", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should get all pets with nameContains filter")
    void shouldGetAllPetsWithNameContainsFilter() {
        when(petRepo.findByNameContainingIgnoreCase("Bud")).thenReturn(Collections.singletonList(pet1));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "id", "ASC", null, "Bud");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should sort all pets by name ASC")
    void shouldSortAllPetsByNameAsc() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet2, pet1));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "name", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Buddy", response.getBody().getContent().get(0).getName());
    }

    @Test
    @DisplayName("Should sort all pets by id ASC")
    void shouldSortAllPetsByIdAsc() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet2, pet1));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "id", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getContent().get(0).getId());
    }

    @Test
    @DisplayName("Should sort all pets by ownerId")
    void shouldSortAllPetsByOwnerId() {
        User owner2 = new User("Jane", "Doe", "Smith");
        owner2.setId(2L);
        Pet pet3 = new Pet("Rex", owner2);
        pet3.setId(3L);

        when(petRepo.findAll()).thenReturn(Arrays.asList(pet3, pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "ownerId", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getContent().get(0).getOwner().getId());
    }

    @Test
    @DisplayName("Should sort all pets by default field when unknown sortBy")
    void shouldSortByDefaultWhenUnknownField() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet2, pet1));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "unknown", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getContent().get(0).getId());
    }

    @Test
    @DisplayName("Should sort all pets DESC")
    void shouldSortAllPetsDesc() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "id", "DESC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, response.getBody().getContent().get(0).getId());
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void shouldHandlePaginationCorrectly() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 1, "id", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(2, response.getBody().getTotalElements());
        assertEquals(2, response.getBody().getTotalPages());
    }

    @Test
    @DisplayName("Should handle page beyond data range")
    void shouldHandlePageBeyondDataRange() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                10, 10, "id", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getContent().size());
    }

    @Test
    @DisplayName("Should handle empty name filter")
    void shouldHandleEmptyNameFilter() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "id", "ASC", "   ", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should handle empty nameContains filter")
    void shouldHandleEmptyNameContainsFilter() {
        when(petRepo.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "id", "ASC", null, "   ");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should handle pet with null owner for ownerId sort")
    void shouldHandlePetWithNullOwnerForOwnerIdSort() {
        Pet petWithNullOwner = new Pet();
        petWithNullOwner.setId(3L);
        petWithNullOwner.setName("Orphan");
        petWithNullOwner.setOwner(null);

        when(petRepo.findAll()).thenReturn(Arrays.asList(petWithNullOwner, pet1));

        ResponseEntity<Page<Pet>> response = petService.getAllPets(
                0, 10, "ownerId", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Pet with null owner should be last due to nullsLast comparator
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should handle searchPetsByOwner default sort")
    void shouldHandleSearchPetsByOwnerDefaultSort() {
        when(petRepo.findByOwner_Id(1L)).thenReturn(Arrays.asList(pet2, pet1));

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "unknown", "ASC", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getContent().get(0).getId());
    }

    @Test
    @DisplayName("Should handle searchPetsByOwner with empty name")
    void shouldHandleSearchPetsByOwnerEmptyName() {
        when(petRepo.findByOwner_Id(1L)).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "id", "ASC", "   ", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Should handle searchPetsByOwner with empty nameContains")
    void shouldHandleSearchPetsByOwnerEmptyNameContains() {
        when(petRepo.findByOwner_Id(1L)).thenReturn(Arrays.asList(pet1, pet2));

        ResponseEntity<Page<Pet>> response = petService.searchPetsByOwner(
                1L, 0, 10, "id", "ASC", null, "   ");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
    }
}
