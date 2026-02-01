package com.sa.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pet Entity Tests")
class PetTest {

    @Test
    @DisplayName("Should create Pet with default constructor")
    void shouldCreatePetWithDefaultConstructor() {
        Pet pet = new Pet();
        assertNull(pet.getId());
        assertNull(pet.getName());
        assertNull(pet.getOwner());
    }

    @Test
    @DisplayName("Should create Pet with name and owner constructor")
    void shouldCreatePetWithNameAndOwner() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("John");

        Pet pet = new Pet("Buddy", owner);

        assertEquals("Buddy", pet.getName());
        assertEquals(owner, pet.getOwner());
    }

    @Test
    @DisplayName("Should create Pet with all args constructor")
    void shouldCreatePetWithAllArgsConstructor() {
        User owner = new User();
        owner.setId(1L);

        Pet pet = new Pet(1L, "Buddy", owner);

        assertEquals(1L, pet.getId());
        assertEquals("Buddy", pet.getName());
        assertEquals(owner, pet.getOwner());
    }

    @Test
    @DisplayName("Should use Builder pattern")
    void shouldUseBuilder() {
        User owner = new User();
        owner.setId(1L);

        Pet pet = Pet.builder()
                .id(1L)
                .name("Buddy")
                .owner(owner)
                .build();

        assertEquals(1L, pet.getId());
        assertEquals("Buddy", pet.getName());
        assertEquals(owner, pet.getOwner());
    }

    @Test
    @DisplayName("Should get ownerId correctly")
    void shouldGetOwnerId() {
        User owner = new User();
        owner.setId(5L);

        Pet pet = new Pet("Buddy", owner);

        assertEquals(5L, pet.getOwnerId());
    }

    @Test
    @DisplayName("Should return null ownerId when owner is null")
    void shouldReturnNullOwnerIdWhenOwnerIsNull() {
        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setOwner(null);

        assertNull(pet.getOwnerId());
    }

    @Test
    @DisplayName("Should test equals and hashCode based on id")
    void shouldTestEqualsAndHashCode() {
        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Buddy");

        Pet pet2 = new Pet();
        pet2.setId(1L);
        pet2.setName("Max");

        Pet pet3 = new Pet();
        pet3.setId(2L);
        pet3.setName("Buddy");

        assertEquals(pet1, pet2); // Same ID
        assertNotEquals(pet1, pet3); // Different ID
        assertEquals(pet1.hashCode(), pet2.hashCode());
    }

    @Test
    @DisplayName("Should test toString excludes owner")
    void shouldTestToString() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("John");

        Pet pet = new Pet(1L, "Buddy", owner);

        String toString = pet.toString();
        assertTrue(toString.contains("Buddy"));
        assertFalse(toString.contains("John")); // Owner should be excluded
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        Pet pet = new Pet();
        User owner = new User();
        owner.setId(1L);

        pet.setId(10L);
        pet.setName("Rex");
        pet.setOwner(owner);

        assertEquals(10L, pet.getId());
        assertEquals("Rex", pet.getName());
        assertEquals(owner, pet.getOwner());
    }
}
