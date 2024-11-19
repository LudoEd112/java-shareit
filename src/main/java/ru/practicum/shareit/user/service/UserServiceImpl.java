package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.Collection;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidation userValidation;

    @Override
    public UserDto create(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        return null;
    }

    @Override
    public UserDto getUserById(Long userId) {
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public void deleteUserById(Long userId) {

    }
}
