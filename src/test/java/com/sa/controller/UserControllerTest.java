package com.sa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.entity.User;
import com.sa.service.impl.PetService;
import com.sa.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

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
    void shouldGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAll()).thenReturn(ResponseEntity.ok(users));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"));

        verify(userService).getAll();
    }

    @Test
    @DisplayName("Should get user by ID")
    void shouldGetUserById() throws Exception {
        when(userService.getById(1L)).thenReturn(ResponseEntity.ok(user1));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));

        verify(userService).getById(1L);
    }

    @Test
    @DisplayName("Should return not found for non-existent user")
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        when(userService.getById(99L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());

        verify(userService).getById(99L);
    }

    @Test
    @DisplayName("Should get users by name")
    void shouldGetUsersByName() throws Exception {
        when(userService.getByName("John")).thenReturn(ResponseEntity.ok(Collections.singletonList(user1)));

        mockMvc.perform(get("/api/users/name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));

        verify(userService).getByName("John");
    }

    @Test
    @DisplayName("Should create user with JSON")
    void shouldCreateUserWithJson() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());

        verify(userService).create(any(User.class));
    }

    @Test
    @DisplayName("Should create user with XML")
    void shouldCreateUserWithXml() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_XML)
                .content("<user><name>John</name><lastname>Doe</lastname><surname>Smith</surname></user>"))
                .andExpect(status().isCreated());

        verify(userService).create(any(User.class));
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() throws Exception {
        when(userService.update(eq(1L), any(User.class))).thenReturn(ResponseEntity.ok(user1));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));

        verify(userService).update(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("Should update user with XML")
    void shouldUpdateUserWithXml() throws Exception {
        when(userService.update(eq(1L), any(User.class))).thenReturn(ResponseEntity.ok(user1));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_XML)
                .content("<user><name>John</name><lastname>Doe</lastname><surname>Smith</surname></user>"))
                .andExpect(status().isOk());

        verify(userService).update(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("Should return not found when updating non-existent user")
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
        when(userService.update(eq(99L), any(User.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put("/api/users/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isNotFound());

        verify(userService).update(eq(99L), any(User.class));
    }

    @Test
    @DisplayName("Should delete user by ID")
    void shouldDeleteUserById() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService).deleteById(1L);
    }

    @Test
    @DisplayName("Should delete all users")
    void shouldDeleteAllUsers() throws Exception {
        mockMvc.perform(delete("/api/users"))
                .andExpect(status().isOk());

        verify(userService).deleteAll();
    }
}
