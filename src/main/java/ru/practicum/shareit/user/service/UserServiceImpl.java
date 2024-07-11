package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    public User getById(long userId) {
        return userRepository.getById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }
}
