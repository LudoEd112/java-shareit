package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    User create(User user);

    User update(User updatedUser);

    User getUserById(Long userId);

    Collection<User> getAllUsers();

    void delete(Long userId);
}
