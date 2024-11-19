package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.List;

public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemValidation itemValidation;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidation userValidation;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {

    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto getByItemId(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        return null;
    }

    @Override
    public List<ItemDto> search() {
        return null;
    }
}
