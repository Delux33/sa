package com.sa.controller;

import com.sa.constant.Constants;
import com.sa.entity.Pet;
import com.sa.entity.User;
import com.sa.service.impl.PetService;
import com.sa.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Pet API", description = "API по управлению питомцами")
public class PetController {

    private final UserService userService;
    private final PetService petService;

    @Operation(summary = "Создание нового питомца для пользователя")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.CREATED_201_STR,
                            description = "Питомец успешно создан",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = Constants.StatusCode.BAD_REQUEST_400_STR,
                            description = "Некорректный запрос",
                            content = @Content
                    )
            }
    )
    @PostMapping(
            value = "api/users/{userId}/pets",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Void> createPetForUser(@PathVariable Long userId,
                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody
                                                 Pet pet) {
        ResponseEntity<User> userResponse = userService.getById(userId);
        if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
            return ResponseEntity.notFound().build();
        }

        Pet newPet = new Pet(pet.getName(), userResponse.getBody());
        petService.create(newPet);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Получить питомца пользователя по его ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR,
                            description = "Питомец найден",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = Constants.StatusCode.BAD_REQUEST_400_STR,
                            description = "Некорректный запрос",
                            content = @Content
                    )
            }
    )
    @GetMapping("api/users/{userId}/pets/{petId}")
    public ResponseEntity<Pet> getPetForUser(@PathVariable Long userId, @PathVariable Long petId) {
        ResponseEntity<Pet> petResponse = petService.getById(petId);
        if (!petResponse.getStatusCode().is2xxSuccessful() || petResponse.getBody() == null) {
            return ResponseEntity.notFound().build();
        }

        Pet pet = petResponse.getBody();
        if (pet.getOwner() == null || pet.getOwner().getId() == null || !pet.getOwner().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(pet);
    }

    @Operation(summary = "Получить список питомцев пользователя с фильтрами и пагинацией")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR,
                            description = "Питомцы найдены",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = Constants.StatusCode.BAD_REQUEST_400_STR,
                            description = "Некорректный запрос",
                            content = @Content
                    )
            }
    )
    @GetMapping("api/users/{userId}/pets")
    public ResponseEntity<?> getUserPets(
            @PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "ASC") String sortDir,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "nameContains", required = false) String nameContains
    ) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return petService.searchPetsByOwner(userId, page, size, sortBy, sortDir, name, nameContains);
    }

    @Operation(summary = "Получить список всех питомцев в системе с фильтрами и пагинацией")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR,
                            description = "Питомцы найдены",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = Constants.StatusCode.BAD_REQUEST_400_STR,
                            description = "Некорректный запрос",
                            content = @Content
                    )
            }
    )
    @GetMapping("api/pets")
    public ResponseEntity<?> getAllPets(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "ASC") String sortDir,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "nameContains", required = false) String nameContains
    ) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return petService.getAllPets(page, size, sortBy, sortDir, name, nameContains);
    }
}
