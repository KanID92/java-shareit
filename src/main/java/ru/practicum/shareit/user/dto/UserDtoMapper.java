package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDtoMapper {

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User fromDto(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        return user;
    }

    public static List<UserDto> toDtos(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

}
