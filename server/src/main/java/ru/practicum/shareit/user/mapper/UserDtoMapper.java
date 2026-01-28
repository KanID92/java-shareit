package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserDtoMapper {

    public static UserOutputDto toDto(User user) {

        return new UserOutputDto(user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User fromCreateDto(UserCreateDto userCreateDto) {
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setEmail(userCreateDto.getEmail());
        return user;
    }

    public static User fromUpdateDto(UserUpdateDto userUpdateDto) {
        User user = new User();
        user.setId(userUpdateDto.getId());
        user.setName(userUpdateDto.getName());
        user.setEmail(userUpdateDto.getEmail());
        return user;
    }



}
