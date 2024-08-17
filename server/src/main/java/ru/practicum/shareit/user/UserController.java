package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserOutputDto create(@RequestBody UserCreateDto userCreateDto) {
        log.info("==> POST. Creating new user: {}", userCreateDto.toString());
        UserOutputDto newUserDto = userService.create(userCreateDto);
        log.info("<== POST. Created new user: {}", newUserDto);

        return newUserDto;
    }

    @PatchMapping("/{userId}")
    public UserOutputDto update(@PathVariable long userId,
                                @RequestBody UserUpdateDto userUpdateDto) {
        log.info("==> PATCH /{userId}. Updating user: {} with id = {}", userUpdateDto.toString(), userId);
        userUpdateDto.setId(userId);
        UserOutputDto updatedUserDto = userService.update(userUpdateDto);
        log.info("<== PATCH /{userId}. Updated user: {} with id = {}", updatedUserDto, updatedUserDto.id());
        return updatedUserDto;
    }

    @GetMapping("/{userId}")
    public UserOutputDto getById(@PathVariable long userId) {
        log.info("==> GET /{userId}. Getting user with id = {}", userId);
        UserOutputDto userOutputDto = userService.getById(userId);
        log.info("<== GET /{userId}. Returning user {} with id = {}", userOutputDto, userOutputDto.id());
        return userOutputDto;
    }

    @GetMapping
    public List<UserOutputDto> getAll() {
        log.info("==> GET. Getting all users");
        List<UserOutputDto> userOutputDtos = userService.getAll();
        log.info("<== GET. Returning userList. Size: {}", userOutputDtos.size());
        return userOutputDtos;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("==> DELETE /{userId}. Deleting user with id = {}", userId);
        userService.delete(userId);
        log.info("<== DELETE /{userId}. User with id = {} deleted", userId);
    }
}
