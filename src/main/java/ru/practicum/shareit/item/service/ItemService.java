package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemInfoDto getByItemId(Long itemId);

    List<ItemInfoDto> getUserItems(Long userId);

    List<ItemDto> search(String text);

    CommentInfoDto addComments(Long userId, CommentDto commentDto, Long itemId);
}
