package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(@Valid UserDto userDto);

    UserDto update(@Valid UserDto userDto);

    UserDto getById(long userId);

    void delete(long userId);

    List<UserDto> getAll();


}
