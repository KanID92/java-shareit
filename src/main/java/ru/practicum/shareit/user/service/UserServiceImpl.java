package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.Marker;

import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Validated({Marker.OnCreate.class})
    public UserDto create(@Valid UserDto userDto) {
        User user = UserDtoMapper.fromDto(userDto);

        return UserDtoMapper.toDto(userRepository.create(user));
    }

    @Override
    @Validated({Marker.OnUpdate.class})
    public UserDto update(@Valid UserDto userDto) {
        User user = UserDtoMapper.fromDto(userDto);
        user.setId(userDto.getId());

        return UserDtoMapper.toDto(userRepository.update(user));
    }

    @Override
    public UserDto getById(long userId) {
        User user = userRepository.getById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found"));
        return UserDtoMapper.toDto(user);
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserDtoMapper::toDto)
                .toList();
    }
}
