package ru.practicum.shareit.validation;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public void validateUserDtoCreate(UserDto user) {

        if (user.name() == null) {
            throw new ValidateException("Empty name. Must be filled in");
        }

        if (user.email() == null) {
            throw new ValidateException("Empty email address. Must be filled in");
        }

        if (!user.email().contains("@")) {
            throw new ValidateException("Invalid email address. Must contain @");
        }

    }

    @Override
    public void validateUserDtoUpdate(UserDto user) {

        if (Optional.ofNullable(user.email()).isPresent()) {
            if (!user.email().contains("@")) {
                throw new ValidateException("Invalid email address. Must contain @");
            }
        }

    }

    @Override
    public void validateItemDtoCreate(ItemDto itemDto) {
        if (itemDto.name() == null || itemDto.name().isEmpty()) {
            throw new ValidateException("Empty item name. Must be filled in");
        }

        if (itemDto.description() == null || itemDto.description().isEmpty()) {
            throw new ValidateException("Empty item description. Must be filled in");
        }

        if (itemDto.available() == null) {
            throw new ValidateException("Empty item available. Must be filled in");
        }

    }

}
