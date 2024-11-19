package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean accessible;
    Long ownerId;
    Long requestId;

    public ItemDto(Long id, String name, String description, Boolean accessible, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.accessible = accessible;
        this.requestId = requestId;
    }
}
