package ru.practicum.shareit.item.validation;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemValidation {
    @SneakyThrows
    public void checkItemFields(ItemDto item) {

        if (item.getAvailable() == null) {
            throw new BadRequestException("Отсутствует поле available");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new BadRequestException("Отсутствует поле name");
        }
        if (item.getDescription() == null) {
            throw new BadRequestException("Отсутствует поле description");
        }
    }
}
