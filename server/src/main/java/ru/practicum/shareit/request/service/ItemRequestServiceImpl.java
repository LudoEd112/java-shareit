package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.request.dto.ItemRequestSimpleDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestInfoDto create(Long userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toUser(userService.getUserById(userId));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);

        return ItemRequestMapper.toItemRequestInfoDto(repository.save(itemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestSimpleDto> getAllByUser(Long userId) {

        List<ItemRequestSimpleDto> list = new ArrayList<>();
        List<ItemRequest> requestsList = repository.findAllByRequestorId(userId);
        List<Item> itemslist = (itemRepository.findAll());
        for (ItemRequest request : requestsList) {
            List<ItemSimpleDto> sortList = itemslist.stream()
                    .filter(item -> item.getItemRequest() != null)
                    .filter(item -> item.getItemRequest().equals(request))
                    .map(ItemMapper::toItemSimpleDto)
                    .toList();
            ItemRequestInfoDto itemRequestInfoDto = ItemRequestMapper.toItemRequestInfoDto(request);
            ItemRequestSimpleDto itemRequestSimpleDto = ItemRequestMapper.toItemRequestSimpleDto(itemRequestInfoDto);
            itemRequestSimpleDto.setItems(sortList);
            list.add(itemRequestSimpleDto);
        }
        return list.stream().sorted(Comparator.comparing(ItemRequestSimpleDto::getCreated).reversed()).toList();

    }

    @Override
    public List<ItemRequestSimpleDto> getAll(Long userId) {

        List<ItemRequestSimpleDto> list = new ArrayList<>();
        List<ItemRequest> listRequestsFromDao = repository.findAllByRequestorIdNotOrderByCreatedDesc(userId);
        for (ItemRequest i : listRequestsFromDao) {
            ItemRequestSimpleDto itemRequestSimpleInfoDto = ItemRequestMapper.toItemRequestSimpleDto(ItemRequestMapper.toItemRequestInfoDto(i));
            list.add(itemRequestSimpleInfoDto);
        }
        return list.stream().sorted(Comparator.comparing(ItemRequestSimpleDto::getCreated).reversed()).toList();
    }

    @Override
    public ItemRequestSimpleDto getById(Long requestId) {
        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() -> new NotFoundException("Запроса с id = {} нет." + requestId));
        ItemRequestSimpleDto itemRequestSimpleInfoDto = ItemRequestMapper.toItemRequestSimpleDto(ItemRequestMapper.toItemRequestInfoDto(itemRequest));
        List<ItemSimpleDto> items = itemRepository.findAllByItemRequest(itemRequest)
                .stream().map(ItemMapper::toItemSimpleDto).toList();
        itemRequestSimpleInfoDto.setItems(items);

        return itemRequestSimpleInfoDto;

    }
}
