package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usersEmails = new HashSet<>();
    private long idUserCounter = 0;

    @Override
    public User create(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        usersEmails.add(user.getEmail());

        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
            delete(user.getId());
            users.put(user.getId(), user);
            usersEmails.add(user.getEmail());

            return users.get(user.getId());
    }

    @Override
    public Optional<User> getById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void delete(long userId) {
        usersEmails.remove(users.get(userId).getEmail());
        users.remove(userId);
        log.info("Deleting: {}", "user with id: " + userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean checkForEmailExisting(User user) {
        return usersEmails.contains(user.getEmail());
    }

    private Long getId() {
        return ++idUserCounter;
    }



}
