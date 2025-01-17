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
        UserDto userDto1 = UserMapper.toUserDto(userRepository.save(user));
        userRepository.flush();
        return userDto1;
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(Long userId) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
        return UserMapper.toUserDto(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long userId) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        checkUser(userId);
        userRepository.deleteById(userId);
    }

    private void checkUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + id));
    }
}
