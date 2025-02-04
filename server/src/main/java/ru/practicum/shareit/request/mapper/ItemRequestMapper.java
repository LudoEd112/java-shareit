package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestSimpleDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {

        return new ItemRequest(
                null,
                itemRequestDto.getDescription(),
                user,
                LocalDateTime.now()
        );
    }

    public static ItemRequestInfoDto toItemRequestInfoDto(ItemRequest itemRequest) {
        return new ItemRequestInfoDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                UserMapper.toUserDto(itemRequest.getRequestor()),
                itemRequest.getCreated(),
                Collections.emptyList()

        );
    }

    public static ItemRequestSimpleDto toItemRequestSimpleDto(ItemRequestInfoDto itemRequestInfoDto) {
        return new ItemRequestSimpleDto(
                itemRequestInfoDto.getId(),
                itemRequestInfoDto.getDescription(),
                itemRequestInfoDto.getCreated(),
                itemRequestInfoDto.getItems()
        );
    }
}