package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.ItemValidation;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    UserService userService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ItemValidation itemValidation;

    @Mock
    ItemRequestRepository itemRequestRepository;
    @Mock
    CommentRepository commentRepository;

    @Mock
    BookingRepository bookingRepository;

    ItemService itemService;

    User user1;
    User user2;
    UserDto userDto;
    ItemRequest itemRequest1;
    ItemDto itemDto1;
    Item item1;

    Item item2;

    CommentDto commentDto1;

    Comment comment;
    Booking booking;
    Booking booking2;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(itemRepository, itemValidation, userService, bookingRepository, commentRepository, itemRequestRepository);

        user1 = new User();
        user1.setId(1L);
        user1.setName("Geralt");
        user1.setEmail("Geralt@outlook.com");

        user2 = new User();
        user2.setId(2L);
        user2.setName("Dima");
        user2.setEmail("Dima@gmail.com");

        userDto = new UserDto();
        userDto.setName("Geralt");
        userDto.setEmail("Geralt@outlook.com");

        itemRequest1 = new ItemRequest();
        itemRequest1.setId(1L);
        itemRequest1.setDescription("New keyboard");
        itemRequest1.setRequestor(user1);
        itemRequest1.setCreated(LocalDateTime.now());

        itemDto1 = new ItemDto();
        itemDto1.setName("KeyboardDto");
        itemDto1.setDescription("DarkProject");
        itemDto1.setAvailable(false);

        item1 = new Item();
        item1.setName("Mouse");
        item1.setDescription("Zgaming");
        item1.setOwner(user1);
        item1.setAvailable(true);
        item1.setId(1L);

        item2 = new Item();
        item2.setName("Mouse");
        item2.setDescription("Zgaming");
        item2.setOwner(user1);
        item2.setAvailable(true);
        item2.setItemRequest(itemRequest1);

        commentDto1 = new CommentDto();
        commentDto1.setText("CommentText");
        commentDto1.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setItem(item1);
        comment.setText("CommentText");
        comment.setAuthor(user1);
        comment.setCreated(LocalDateTime.now());

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item1);
        booking.setStartBooking(LocalDateTime.now().plusDays(1));
        booking.setEndBooking(LocalDateTime.now().plusDays(2));

        booking2 = new Booking();
        booking2.setId(1L);
        booking2.setItem(item1);
        booking2.setBooker(user1);
        booking2.setStartBooking(LocalDateTime.now().minusDays(1));
        booking2.setEndBooking(LocalDateTime.now().plusDays(1));
        booking2.setStatus(BookingStatus.APPROVED);

    }

    @Test
    @DisplayName("Создание элемента с действующим запросом (владелец)")
    void testCreateWithValidRequest() {
        itemDto1.setRequestId(1L);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest1));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto createdItemDto = itemService.create(1L, itemDto1);

        assertEquals("KeyboardDto", createdItemDto.getName());
        assertEquals(1L, createdItemDto.getRequestId());
    }

    @Test
    @DisplayName("Создание элемента с отсутствующим запросом")
    void testCreateWithNonExistentRequest() {
        itemDto1.setRequestId(1L);

        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.empty()); // Запрос не найден

        assertThrows(NotFoundException.class, () -> itemService.create(1L, itemDto1));
    }

    @Test
    @DisplayName("Создание элемента без запроса")
    void testCreateWithoutRequest() {
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto createdItemDto = itemService.create(1L, itemDto1);

        assertEquals("KeyboardDto", createdItemDto.getName());
        assertNull(createdItemDto.getRequestId());
    }

    @Test
    @DisplayName("Обновление несуществующего элемента")
    void testUpdateNonExistentItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty()); // Элемент не найден

        assertThrows(NotFoundException.class, () -> itemService.update(1L, 1L, itemDto1));
    }

    @Test
    @DisplayName("Успешное обновление элемента")
    void testUpdateItemSuccessfully() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Возвращаем сам объект

        ItemDto updatedItemDto = itemService.update(1L, 1L, itemDto1);

        assertEquals("KeyboardDto", updatedItemDto.getName());
        assertEquals("DarkProject", updatedItemDto.getDescription());
        assertEquals(false, updatedItemDto.getAvailable());
    }

    @Test
    @DisplayName("Обновление элемента без прав владельца")
    void testUpdateItemWithoutOwnership() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setName("Dima");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        assertThrows(ForbiddenException.class, () -> itemService.update(2L, 1L, itemDto1));
    }

    @Test
    @DisplayName("Успешное получение элемента по id")
    void testGetByIdSuccessfully() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(commentRepository.findAllByItemId(1L)).thenReturn(List.of(comment));
        when(bookingRepository.findAllByItemId(1L)).thenReturn(List.of(booking));

        ItemInfoDto itemInfoDto = itemService.getByItemId(1L);

        assertEquals("Mouse", itemInfoDto.getName());
        assertEquals("Zgaming", itemInfoDto.getDescription());
        assertEquals(1, itemInfoDto.getComments().size());
        assertEquals("CommentText", itemInfoDto.getComments().get(0).getText());
    }

    @Test
    @DisplayName("Получение несуществующего элемента")
    void testGetByIdNonExistentItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getByItemId(1L));
    }

    @Test
    @DisplayName("Получение элемента без комментариев и бронирований")
    void testGetByIdWithoutCommentsAndBookings() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(commentRepository.findAllByItemId(1L)).thenReturn(Collections.emptyList());
        when(bookingRepository.findAllByItemId(1L)).thenReturn(Collections.emptyList());

        ItemInfoDto itemInfoDto = itemService.getByItemId(1L);

        assertEquals("Mouse", itemInfoDto.getName());
        assertEquals("Zgaming", itemInfoDto.getDescription());
        assertEquals(0, itemInfoDto.getComments().size());
    }

    @Test
    @DisplayName("ItemService_findByIdNotItem")
    void testFindByIdNotItem() {

        assertThrows(
                NotFoundException.class,
                () -> itemService.getByItemId(2L)
        );
    }

    @Test
    @DisplayName("ItemService_deleteNotItem")
    void testDeleteNotItem() {

        assertThrows(
                NotFoundException.class,
                () -> itemService.delete(5L)
        );
    }

    @Test
    @DisplayName("ItemService_delete")
    void testDelete() {

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        itemService.delete(1L);

        verify(itemRepository).delete(any(Item.class));
    }

    @Test
    @DisplayName("Успешное получение предметов владельца")
    void testGetOwnerItemsSuccessfully() {
        when(itemRepository.findAllByOwnerId(1L)).thenReturn(List.of(item1));
        when(bookingRepository.findAllByIdIn(List.of(item1.getId()))).thenReturn(List.of(booking));
        when(commentRepository.findAllByItemIdIn(List.of(item1.getId()))).thenReturn(List.of(comment));

        List<ItemInfoDto> itemInfoDtos = itemService.getUserItems(1L);

        assertEquals(1, itemInfoDtos.size());
        assertEquals("Mouse", itemInfoDtos.get(0).getName());
        assertEquals("Zgaming", itemInfoDtos.get(0).getDescription());
        assertEquals(1, itemInfoDtos.get(0).getComments().size());
        assertEquals("CommentText", itemInfoDtos.get(0).getComments().get(0).getText());
    }

    @Test
    @DisplayName("Получение предметов владельца без предметов")
    void testGetOwnerItemsWithoutItems() {
        when(itemRepository.findAllByOwnerId(1L)).thenReturn(Collections.emptyList());

        List<ItemInfoDto> itemInfoDtos = itemService.getUserItems(1L);

        assertEquals(0, itemInfoDtos.size());
    }

    @Test
    @DisplayName("Получение предметов владельца без комментариев и бронирований")
    void testGetOwnerItemsWithoutCommentsAndBookings() {
        when(itemRepository.findAllByOwnerId(1L)).thenReturn(List.of(item1));
        when(bookingRepository.findAllByIdIn(List.of(item1.getId()))).thenReturn(Collections.emptyList());
        when(commentRepository.findAllByItemIdIn(List.of(item1.getId()))).thenReturn(Collections.emptyList());

        List<ItemInfoDto> itemInfoDtos = itemService.getUserItems(1L);

        assertEquals(1, itemInfoDtos.size());
        assertEquals("Mouse", itemInfoDtos.get(0).getName());
        assertEquals("Zgaming", itemInfoDtos.get(0).getDescription());
        assertEquals(0, itemInfoDtos.get(0).getComments().size());
    }

    @Test
    @DisplayName("ItemService_searchTextEmpty")
    void testSearchTextEmpty() {

        final List<ItemDto> itemDtos = itemService.search("");

        assertTrue(itemDtos.isEmpty());
    }

    @Test
    @DisplayName("ItemService_searchText")
    void testSearchText() {

        when(itemRepository.search("as")).thenReturn(List.of(item1));

        final List<ItemDto> itemDtos = itemService.search("as");

        assertEquals("Mouse", itemDtos.getFirst().getName());
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    void testAddCommentSuccessfully() {
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(bookingRepository.getALLByItemIdAndBookerIdAndStatusIsOrderByEndBookingDesc(1L, 1L, BookingStatus.APPROVED))
                .thenReturn(Collections.singletonList(booking2));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentInfoDto commentInfoDto = itemService.addComments(1L, commentDto1, 1L);

        assertEquals("CommentText", commentInfoDto.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("Ошибка при добавлении комментария без брони")
    void testAddCommentWithoutBooking() {
        when(userService.getUserById(1L)).thenReturn(userDto);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(bookingRepository.getALLByItemIdAndBookerIdAndStatusIsOrderByEndBookingDesc(1L, 1L, BookingStatus.APPROVED))
                .thenReturn(Collections.emptyList());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            itemService.addComments(1L, commentDto1, 1L);
        });

        assertEquals(exception.getMessage(), "Пользователь не бронировал вещь");
    }
}