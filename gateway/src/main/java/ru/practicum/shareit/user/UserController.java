package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    @Validated
    public ResponseEntity<Object> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("==> POST. Creating new user: {}", userCreateDto.toString());
        ResponseEntity<Object> newUserDto = userClient.create(userCreateDto);
        log.info("<== POST. Created new user: {}", newUserDto);

        return newUserDto;
    }

    @PatchMapping("/{userId}")
    @Validated
    public ResponseEntity<Object> update(@PathVariable long userId,
                                @Valid @RequestBody UserUpdateDto userUpdateDto) {
        log.info("==> PATCH /{userId}. Updating user: {} with id = {}", userId, userUpdateDto);
        userUpdateDto.setId(userId);
        ResponseEntity<Object> updatedUserDto = userClient.update(userUpdateDto);
        log.info("<== PATCH /{userId}. Updated user: {} with id = {}", updatedUserDto, userId);
        return updatedUserDto;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable long userId) {
        log.info("==> GET /{userId}. Getting user with id = {}", userId);
        ResponseEntity<Object> userOutputDto = userClient.getById(userId);
        log.info("<== GET /{userId}. Returning user {} with id = {}", userOutputDto, userId);
        return userOutputDto;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("==> GET. Getting all users");
        ResponseEntity<Object> userOutputDtos = userClient.getAll();
        log.info("<== GET. Returning userList.");
        return userOutputDtos;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("==> DELETE /{userId}. Deleting user with id = {}", userId);
        userClient.delete(userId);
        log.info("<== DELETE /{userId}. User with id = {} deleted", userId);
    }
}
