package ru.practicum.shareit.user.dao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public User create(User user) {
        if (users.values().stream().anyMatch(rndmUser -> rndmUser.getEmail().equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Такой пользователь уже зарегестрирован");
        }
        user.setId(generatedId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User update(User updatedUser) {
        User user = users.get(updatedUser.getId());
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            if (users.values().stream().anyMatch(rndmUser -> rndmUser.getEmail().equals(updatedUser.getEmail()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Такой email уже зарегестрирован");
            }
            user.setEmail(updatedUser.getEmail());
        }
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    private Long generatedId() {
        return id++;
    }
}
