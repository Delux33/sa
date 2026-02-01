package com.sa.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity Tests")
class UserTest {

    @Test
    @DisplayName("Should create User with default constructor")
    void shouldCreateUserWithDefaultConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getLastname());
        assertNull(user.getSurname());
    }

    @Test
    @DisplayName("Should create User with name, lastname, surname constructor")
    void shouldCreateUserWithNameLastnameSurname() {
        User user = new User("John", "Doe", "Smith");

        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastname());
        assertEquals("Smith", user.getSurname());
    }

    @Test
    @DisplayName("Should create User with all args constructor")
    void shouldCreateUserWithAllArgsConstructor() {
        List<Pet> pets = new ArrayList<>();
        User user = new User(1L, "John", "Doe", "Smith", pets);

        assertEquals(1L, user.getId());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastname());
        assertEquals("Smith", user.getSurname());
        assertEquals(pets, user.getPets());
    }

    @Test
    @DisplayName("Should use Builder pattern")
    void shouldUseBuilder() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .lastname("Doe")
                .surname("Smith")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastname());
        assertEquals("Smith", user.getSurname());
    }

    @Test
    @DisplayName("Should have default pets list in builder")
    void shouldHaveDefaultPetsListInBuilder() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .build();

        assertNotNull(user.getPets());
        assertTrue(user.getPets().isEmpty());
    }

    @Test
    @DisplayName("Should test equals and hashCode based on id")
    void shouldTestEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("Jane");

        User user3 = new User();
        user3.setId(2L);
        user3.setName("John");

        assertEquals(user1, user2); // Same ID
        assertNotEquals(user1, user3); // Different ID
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Should test toString excludes pets")
    void shouldTestToString() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .lastname("Doe")
                .surname("Smith")
                .build();

        Pet pet = new Pet("Buddy", user);
        user.getPets().add(pet);

        String toString = user.toString();
        assertTrue(toString.contains("John"));
        assertFalse(toString.contains("Buddy")); // Pets should be excluded
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        User user = new User();
        List<Pet> pets = new ArrayList<>();

        user.setId(10L);
        user.setName("John");
        user.setLastname("Doe");
        user.setSurname("Smith");
        user.setPets(pets);

        assertEquals(10L, user.getId());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastname());
        assertEquals("Smith", user.getSurname());
        assertEquals(pets, user.getPets());
    }

    @Test
    @DisplayName("Should manage pets list")
    void shouldManagePetsList() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .build();

        Pet pet1 = new Pet("Buddy", user);
        Pet pet2 = new Pet("Max", user);

        user.getPets().add(pet1);
        user.getPets().add(pet2);

        assertEquals(2, user.getPets().size());
        assertTrue(user.getPets().contains(pet1));
        assertTrue(user.getPets().contains(pet2));
    }
}
