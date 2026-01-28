package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserOutputDto create(UserCreateDto userCreateDto);

    UserOutputDto update(UserUpdateDto userUpdateDto);

    UserOutputDto getById(long userId);

    void delete(long userId);

    List<UserOutputDto> getAll();


}
