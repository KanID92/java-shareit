package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(User user);

    User getById(long userId);

    void delete(long userId);

    List<User> getAll();


}
