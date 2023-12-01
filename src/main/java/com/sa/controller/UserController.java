package com.sa.controller;

import com.sa.entity.User;
import com.sa.entity.swagger.UserForSwagger;
import com.sa.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@Tag(name = "User API", description = "API по управлению пользователями")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Получить всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи найдены",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))))
    })
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @Operation(summary = "Получить пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Получить пользователя(ей) по имени")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))))
    })
    @GetMapping("/name/{name}")
    public List<User> getUsersByName(@PathVariable @RequestParam String userName) {
        return userService.getByName(userName);
    }

    @Operation(summary = "Создание нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    })
    @PostMapping
    public void createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(name = "Создать пользователя", value = "{\"name\": \"example\", \"lastname\": \"example\", \"surname\": \"example\"}")))
                               @RequestBody UserForSwagger userForSwagger) {
        userService.create(new User(userForSwagger.getName(), userForSwagger.getLastname(), userForSwagger.getSurname()));
    }

    @Operation(summary = "Обновление пользователя по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                   content = @Content(mediaType = "application/json",
                                           examples = @ExampleObject(name = "Обновить пользователя", value = "{\"name\": \"example\", \"lastname\": \"example\", \"surname\": \"example\"}")))
                           @RequestBody UserForSwagger userForSwagger) {
        return userService.update(id, new User(userForSwagger.getName(), userForSwagger.getLastname(), userForSwagger.getSurname()));
    }

    @Operation(summary = "Удаление пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален")
    })
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
