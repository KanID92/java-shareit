package ru.practicum.shareit.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Validated
    public UserOutputDto create(@Valid UserCreateDto userCreateDto) {
        User user = UserDtoMapper.fromCreateDto(userCreateDto);
        if (userRepository.checkForEmailExisting(user)) {
            throw new ConflictException("User with email " + user.getEmail() + " already exists");
        } else {
            return UserDtoMapper.toDto(userRepository.create(user));
        }
    }

    @Override
    @Validated
    public UserOutputDto update(@Valid UserUpdateDto userUpdateDto) {
        User savedUser = userRepository.getById(userUpdateDto.getId()).orElseThrow(
                () -> new NotFoundException("User with id " + userUpdateDto.getId() + " not found"));

        User user = UserDtoMapper.fromUpdateDto(userUpdateDto);

        if (user.getName() == null) {
            user.setName(savedUser.getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(savedUser.getEmail());
        } else if (userRepository.checkForEmailExisting(user) && (!savedUser.getEmail().equals(user.getEmail()))) {
            throw new ConflictException("User with email " + user.getEmail() + " already exists");
        }

        return UserDtoMapper.toDto(userRepository.update(user));
    }

    @Override
    public UserOutputDto getById(long userId) {
        User user = userRepository.getById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found"));
        return UserDtoMapper.toDto(user);
    }

    @Override
    public void delete(long userId) {
        getById(userId);
        userRepository.delete(userId);
    }

    @Override
    public List<UserOutputDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserDtoMapper::toDto)
                .toList();
    }
}
