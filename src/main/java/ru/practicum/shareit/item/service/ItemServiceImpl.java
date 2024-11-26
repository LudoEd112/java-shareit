package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

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
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        itemValidation.checkItemFields(itemDto);
        UserDto userDto = userService.getUserById(userId);
        User user = UserMapper.toUser(userDto);
        Item item = ItemMapper.toItem(user, itemDto);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        if (itemId == null) {
            throw new BadRequestException("id не указан");
        }
        userService.getUserById(userId);
        checkItemById(itemId);
        checkOwner(userId, itemId);
        User owner = UserMapper.toUser(userService.getUserById(userId));
        Item item = ItemMapper.toItem(owner, itemDto);
        item.setId(itemId);
        return ItemMapper.toItemDto(itemRepository.update(item));
    }

    @Override
    public ItemDto getByItemId(Long itemId) {
        if (itemId == null) {
            throw new BadRequestException("id не указан");
        }
        checkItemById(itemId);
        return ItemMapper.toItemDto(itemRepository.getByItemId(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        if (userId == null) {
            throw new BadRequestException("id не указан");
        }
        userService.getUserById(userId);
        return itemRepository.getUserItems(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    private void checkItemById(Long itemId) {
        if (itemRepository.getByItemId(itemId) == null) {
            throw new NotFoundException("Предмета по id не существует");
        }
    }

    private void checkOwner(Long userId, Long itemId) {
        if (!userId.equals(itemRepository.getByItemId(itemId).getOwner().getId())) {
            throw new ForbiddenException("Только собственники предметов имеют доступ");
        }
    }
}
