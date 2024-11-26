package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    Item update(Item item);

    Item getByItemId(Long itemId);

    List<Item> getUserItems(Long userId);

    List<Item> search(String text);
}
