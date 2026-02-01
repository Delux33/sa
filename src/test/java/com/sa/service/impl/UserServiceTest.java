package com.sa.service.impl;

import com.sa.entity.User;
import com.sa.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("John", "Doe", "Smith");
        user1.setId(1L);

        user2 = new User("Jane", "Doe", "Johnson");
        user2.setId(2L);
    }

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response = userService.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userRepo).findAll();
    }

    @Test
    @DisplayName("Should get user by ID when exists")
    void shouldGetUserByIdWhenExists() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user1));

        ResponseEntity<User> response = userService.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John", response.getBody().getName());
        verify(userRepo).findById(1L);
    }

    @Test
    @DisplayName("Should return not found when user does not exist")
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userService.getById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepo).findById(99L);
    }

    @Test
    @DisplayName("Should get users by name")
    void shouldGetUsersByName() {
        when(userRepo.findByName("John")).thenReturn(Collections.singletonList(user1));

        ResponseEntity<List<User>> response = userService.getByName("John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getName());
        verify(userRepo).findByName("John");
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        userService.create(user1);

        verify(userRepo).save(user1);
    }

    @Test
    @DisplayName("Should update user when exists")
    void shouldUpdateUserWhenExists() {
        when(userRepo.existsById(1L)).thenReturn(true);
        when(userRepo.save(any(User.class))).thenReturn(user1);

        ResponseEntity<User> response = userService.update(1L, user1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(userRepo).existsById(1L);
        verify(userRepo).save(user1);
    }

    @Test
    @DisplayName("Should return not found when updating non-existent user")
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() {
        when(userRepo.existsById(99L)).thenReturn(false);

        ResponseEntity<User> response = userService.update(99L, user1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepo).existsById(99L);
        verify(userRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user by ID")
    void shouldDeleteUserById() {
        userService.deleteById(1L);

        verify(userRepo).deleteById(1L);
    }

    @Test
    @DisplayName("Should delete all users")
    void shouldDeleteAllUsers() {
        userService.deleteAll();

        verify(userRepo).deleteAll();
    }
}
