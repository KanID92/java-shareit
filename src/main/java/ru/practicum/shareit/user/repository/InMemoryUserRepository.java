package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long idUserCounter = 0;

    @Override
    public User create(User user) {
        checkForEmailExisting(user);
        user.setId(getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        User savedUser = getById(user.getId()).orElseThrow(
                () -> new NotFoundException("User with id " + user.getId() + " not found"));
        if (Optional.ofNullable(user.getName()).isEmpty()) {
            user.setName(savedUser.getName());
        }
        if (Optional.ofNullable(user.getEmail()).isEmpty()) {
            user.setEmail(savedUser.getEmail());
        }

            checkForEmailExisting(user);
            users.get(user.getId());
            users.put(user.getId(), user);
            return users.get(user.getId());

    }

    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void delete(long userId) {
        log.info("Deleting: {}", Optional.ofNullable(users.remove(userId)).orElseThrow());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private Long getId() {
        return ++idUserCounter;
    }

    private void checkForEmailExisting(User user) {
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail()) && (long) u.getId() != user.getId()) {
                throw new ConflictException("User with email " + user.getEmail() + " already exists");
            }
        }
    }


}
