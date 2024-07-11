package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ValidationService validationService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("==> POST. Creating new user: {}", userDto.toString());
        validationService.validateUserDtoCreate(userDto);
        User user = UserDtoMapper.fromDto(userDto);
        UserDto newUserDto = UserDtoMapper.toDto(userService.create(user));
        log.info("<== POST. Created new user: {}", newUserDto);

        return newUserDto;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId,
                          @RequestBody UserDto userDto) {
        log.info("==> PATCH /{userId}. Updating user: {} with id = {}", userDto.toString(), userId);
        validationService.validateUserDtoUpdate(userDto);
        User user = UserDtoMapper.fromDto(userDto);
        user.setId(userId);
        UserDto updatedUserDto = UserDtoMapper.toDto(userService.update(user));
        log.info("<== PATCH /{userId}. Updated user: {} with id = {}", updatedUserDto, updatedUserDto.id());
        return updatedUserDto;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) {
        log.info("==> GET /{userId}. Getting user with id = {}", userId);
        UserDto userDto = UserDtoMapper.toDto(userService.getById(userId));
        log.info("<== GET /{userId}. Returning user {} with id = {}", userDto, userDto.id());
        log.info("Current userList after: {}", userService.getAll());
        return userDto;
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("==> GET. Getting all users");
        List<UserDto> userDtos = UserDtoMapper.toDtos(userService.getAll());
        log.info("<== GET. Returning userList. Size: {}", userDtos.size());
        return userDtos;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("==> DELETE /{userId}. Deleting user with id = {}", userId);
        userService.delete(userId);
        log.info("<== DELETE /{userId}. User with id = {} deleted", userId);
    }
}
