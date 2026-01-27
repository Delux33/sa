package com.sa.controller;

import com.sa.constant.Constants;
import com.sa.entity.Pet;
import com.sa.entity.User;
import com.sa.service.impl.PetService;
import com.sa.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API по управлению пользователями")
public class UserController {

    private final UserService userService;
    private final PetService petService;

    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR, description = "Пользователи найдены",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @Operation(summary = "Получить пользователя по его ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR, description = "Пользователь найден",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Получить пользователя(ей) по имени")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR, description = "Пользователь найден",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping("/name/{name}")
    public List<User> getUsersByName(@PathVariable @RequestParam String name) {
        return userService.getByName(name);
    }

    @Operation(summary = "Создание нового пользователя")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.CREATED_201_STR,
                            description = "Пользователь успешно создан"
                    ),
                    @ApiResponse(
                            responseCode = Constants.StatusCode.BAD_REQUEST_400_STR,
                            description = "Некорректный запрос"
                    )
            }
    )
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody User user) {
        userService.create(user);
    }

    @Operation(summary = "Обновление пользователя по его ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR,
                            description = "Пользователь успешно обновлен",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            schema = @Schema(implementation = User.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = Constants.StatusCode.BAD_REQUEST_400_STR,
                            description = "Некорректный запрос",
                            content = @Content
                    )
            }
    )
    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public User updateUser(@PathVariable Long id,
                           @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody User user) {
        return userService.update(id, user);
    }

    @Operation(summary = "Удаление пользователя по ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = Constants.StatusCode.OK_200_STR,
                            description = "Пользователь успешно удален"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @Operation(summary = "Удаление всех пользователей")
    @DeleteMapping()
    public void deleteAllUsers() {
        userService.deleteAll();
    }
}
