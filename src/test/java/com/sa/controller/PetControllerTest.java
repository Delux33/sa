package com.sa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sa.entity.Pet;
import com.sa.entity.User;
import com.sa.service.impl.PetService;
import com.sa.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@DisplayName("PetController Tests")
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PetService petService;

    private User owner;
    private Pet pet;

    @BeforeEach
    void setUp() {
        owner = new User("John", "Doe", "Smith");
        owner.setId(1L);

        pet = new Pet("Buddy", owner);
        pet.setId(1L);
    }

    @Test
    @DisplayName("Should create pet for user successfully")
    void shouldCreatePetForUserSuccessfully() throws Exception {
        when(userService.getById(1L)).thenReturn(ResponseEntity.ok(owner));

        mockMvc.perform(post("/api/users/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Buddy\"}"))
                .andExpect(status().isCreated());

        verify(petService).create(any(Pet.class));
    }

    @Test
    @DisplayName("Should return not found when creating pet for non-existent user")
    void shouldReturnNotFoundWhenCreatingPetForNonExistentUser() throws Exception {
        when(userService.getById(99L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(post("/api/users/99/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Buddy\"}"))
                .andExpect(status().isNotFound());

        verify(petService, never()).create(any(Pet.class));
    }

    @Test
    @DisplayName("Should get pet for user successfully")
    void shouldGetPetForUserSuccessfully() throws Exception {
        when(petService.getById(1L)).thenReturn(ResponseEntity.ok(pet));

        mockMvc.perform(get("/api/users/1/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Buddy"));
    }

    @Test
    @DisplayName("Should return not found when pet does not exist")
    void shouldReturnNotFoundWhenPetDoesNotExist() throws Exception {
        when(petService.getById(99L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/users/1/pets/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return forbidden when pet owner does not match user")
    void shouldReturnForbiddenWhenPetOwnerDoesNotMatchUser() throws Exception {
        when(petService.getById(1L)).thenReturn(ResponseEntity.ok(pet));

        mockMvc.perform(get("/api/users/2/pets/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return forbidden when pet has no owner")
    void shouldReturnForbiddenWhenPetHasNoOwner() throws Exception {
        Pet orphanPet = new Pet();
        orphanPet.setId(1L);
        orphanPet.setName("Orphan");
        orphanPet.setOwner(null);

        when(petService.getById(1L)).thenReturn(ResponseEntity.ok(orphanPet));

        mockMvc.perform(get("/api/users/1/pets/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return forbidden when owner has null id")
    void shouldReturnForbiddenWhenOwnerHasNullId() throws Exception {
        User ownerWithNullId = new User("John", "Doe", "Smith");
        ownerWithNullId.setId(null);
        Pet petWithOwnerNullId = new Pet("Buddy", ownerWithNullId);
        petWithOwnerNullId.setId(1L);

        when(petService.getById(1L)).thenReturn(ResponseEntity.ok(petWithOwnerNullId));

        mockMvc.perform(get("/api/users/1/pets/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should get user pets with pagination")
    void shouldGetUserPetsWithPagination() throws Exception {
        Page<Pet> page = new PageImpl<>(Collections.singletonList(pet), PageRequest.of(0, 10), 1);
        when(petService.searchPetsByOwner(eq(1L), anyInt(), anyInt(), anyString(), anyString(), isNull(), isNull()))
                .thenReturn(ResponseEntity.ok(page));

        mockMvc.perform(get("/api/users/1/pets")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return bad request for negative page")
    void shouldReturnBadRequestForNegativePage() throws Exception {
        mockMvc.perform(get("/api/users/1/pets")
                .param("page", "-1")
                .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request for zero size")
    void shouldReturnBadRequestForZeroSize() throws Exception {
        mockMvc.perform(get("/api/users/1/pets")
                .param("page", "0")
                .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request for negative size")
    void shouldReturnBadRequestForNegativeSize() throws Exception {
        mockMvc.perform(get("/api/users/1/pets")
                .param("page", "0")
                .param("size", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get all pets with pagination")
    void shouldGetAllPetsWithPagination() throws Exception {
        Page<Pet> page = new PageImpl<>(Collections.singletonList(pet), PageRequest.of(0, 10), 1);
        when(petService.getAllPets(anyInt(), anyInt(), anyString(), anyString(), isNull(), isNull()))
                .thenReturn(ResponseEntity.ok(page));

        mockMvc.perform(get("/api/pets")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return bad request for all pets with negative page")
    void shouldReturnBadRequestForAllPetsWithNegativePage() throws Exception {
        mockMvc.perform(get("/api/pets")
                .param("page", "-1")
                .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request for all pets with zero size")
    void shouldReturnBadRequestForAllPetsWithZeroSize() throws Exception {
        mockMvc.perform(get("/api/pets")
                .param("page", "0")
                .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get user pets with filters")
    void shouldGetUserPetsWithFilters() throws Exception {
        Page<Pet> page = new PageImpl<>(Collections.singletonList(pet), PageRequest.of(0, 10), 1);
        when(petService.searchPetsByOwner(eq(1L), anyInt(), anyInt(), anyString(), anyString(), eq("Buddy"), isNull()))
                .thenReturn(ResponseEntity.ok(page));

        mockMvc.perform(get("/api/users/1/pets")
                .param("name", "Buddy"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should get all pets with nameContains filter")
    void shouldGetAllPetsWithNameContainsFilter() throws Exception {
        Page<Pet> page = new PageImpl<>(Collections.singletonList(pet), PageRequest.of(0, 10), 1);
        when(petService.getAllPets(anyInt(), anyInt(), anyString(), anyString(), isNull(), eq("Bud")))
                .thenReturn(ResponseEntity.ok(page));

        mockMvc.perform(get("/api/pets")
                .param("nameContains", "Bud"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should accept XML content type for create pet")
    void shouldAcceptXmlContentTypeForCreatePet() throws Exception {
        when(userService.getById(1L)).thenReturn(ResponseEntity.ok(owner));

        mockMvc.perform(post("/api/users/1/pets")
                .contentType(MediaType.APPLICATION_XML)
                .content("<pet><name>Buddy</name></pet>"))
                .andExpect(status().isCreated());
    }
}
