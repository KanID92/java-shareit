package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserOutputDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserOutputDto create(UserCreateDto userCreateDto) {
        User user = UserDtoMapper.fromCreateDto(userCreateDto);
        if (userRepository.checkForEmailExisting(user.getEmail()) > 0) {
            throw new ConflictException("User with email " + user.getEmail() + " already exists");
        } else {
            return UserDtoMapper.toDto(userRepository.save(user));
        }
    }

    @Override
    @Transactional
    public UserOutputDto update(UserUpdateDto userUpdateDto) {
        User savedUser = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("User with id " + userUpdateDto.getId() + " not found"));

        User user = UserDtoMapper.fromUpdateDto(userUpdateDto);

        if (user.getName() == null) {
            user.setName(savedUser.getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(savedUser.getEmail());
        } else if (userRepository.checkForEmailExisting(user.getEmail()) > 0 && (
                !savedUser.getEmail().equals(user.getEmail()))) {
            throw new ConflictException("User with email " + user.getEmail() + " already exists");
        }

        return UserDtoMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserOutputDto getById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        return UserDtoMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(long userId) {
        getById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOutputDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserDtoMapper::toDto)
                .toList();
    }
}
