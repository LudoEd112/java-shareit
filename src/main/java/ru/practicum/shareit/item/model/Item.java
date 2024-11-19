package ru.practicum.shareit.item.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Long id;
    String name;
    String description;
    Boolean accessible;
    User owner;
    ItemRequest itemRequest;

    public Item(String name, String description, Boolean accessible, User owner, ItemRequest itemRequest) {
        this.name = name;
        this.description = description;
        this.accessible = accessible;
        this.owner = owner;
        this.itemRequest = itemRequest;
    }
}
