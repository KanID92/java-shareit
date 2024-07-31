package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserOutputDto create(@Valid UserCreateDto userCreateDto);

    UserOutputDto update(@Valid UserUpdateDto userUpdateDto);

    UserOutputDto getById(long userId);

    void delete(long userId);

    List<UserOutputDto> getAll();


}
