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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users/{userId}/pets")
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
            value = "{userId}/pet",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPetForUser(@PathVariable Long userId,
                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody
                                                 Pet pet) {
        User user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Pet newPet = new Pet(pet.getName(), user);
        petService.create(newPet);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
