package ru.practicum.shareit.validation;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface ValidationService {

    void validateUserDtoCreate(UserDto user);

    void validateUserDtoUpdate(UserDto user);

    void validateItemDtoCreate(ItemDto itemDto);

}
