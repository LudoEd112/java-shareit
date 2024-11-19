package ru.practicum.shareit.user.validation;

import lombok.SneakyThrows;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

public class UserValidation {
    private UserRepository userRepository;

    @SneakyThrows
    public void checkUserFields(UserDto userDto) {
        if (userDto == null) {
            throw new BadRequestException("Нет данных");
        }

        if (userDto.getName() == null || userDto.getName().isBlank()) {
            throw new BadRequestException("Имя пользователя не указано");
        }

        if (userDto.getEmail() == null || userDto.getEmail().isBlank() || !(userDto.getEmail().contains("@"))) {
            throw new BadRequestException("Почта не может не содержать символ @ или быть пустой");
        }
    }

    @SneakyThrows
    public void checkUserId(Long userDtoId) {
        if (userDtoId == null) {
            throw new BadRequestException("id пользователя не указан");
        }
    }
}
