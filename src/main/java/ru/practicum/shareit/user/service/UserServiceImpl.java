package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidation userValidation;

    @Override
    public UserDto create(UserDto userDto) {
        userValidation.checkUserFields(userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        checkUser(userId);
        User updatedUser = UserMapper.toUser(userDto);
        updatedUser.setId(userId);
        return UserMapper.toUserDto(userRepository.update(updatedUser));
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        checkUser(userId);
        return UserMapper.toUserDto(userRepository.getUserById(userId));
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void deleteUserById(Long userId) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        checkUser(userId);
        userRepository.delete(userId);
    }

    private void checkUser(Long id) {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("Пользователь по id не найден");
        }
    }
}
