package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validation.UserValidation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceImplTest {


    UserService userService;

    UserMapper userMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    UserValidation userValidation;

    UserDto userDto1;

    User user1;

    @BeforeEach
    public void setUp() {

        userMapper = new UserMapper();

        userService = new UserServiceImpl(userRepository, userValidation);

        userDto1 = new UserDto();
        userDto1.setName("Dima");
        userDto1.setEmail("dima@mail.ru");

        user1 = new User();
        user1.setName("Dima");
        user1.setEmail("Dima@yandex.ru");
        user1.setId(1L);
    }


    @Test
    @DisplayName("UserService_create")
    void testCreate() {

        when(userRepository.save(UserMapper.toUser(userDto1))).thenReturn(user1);

        final UserDto userDto = userService.create(userDto1);

        assertEquals("Dima", userDto.getName());
        assertEquals(1, userDto.getId());
    }

    @Test
    @DisplayName("UserService_findById")
    void testFindById() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        assertEquals("Dima", userService.getUserById(1L).getName());
    }

    @Test
    @DisplayName("UserService_findByIdNotUser")
    void testFindByIdNotUser() {

        assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(3L)
        );
    }

    @Test
    @DisplayName("UserService_updateNotUser")
    void testUpdateNotUser() {

        assertThrows(
                NotFoundException.class,
                () -> userService.update(1L, userDto1)
        );
    }


    @Test
    @DisplayName("UserService_updateSetName")
    void testUpdateSetName() {

        final UserDto userDto2 = new UserDto();
        userDto2.setName("Geralt");

        final User user2 = new User();
        user2.setName("Geralt");
        user2.setEmail("Dima@yandex.ru");
        user2.setId(1L);

        when(userRepository.save(UserMapper.toUser(userDto1))).thenReturn(user1);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.save(user2)).thenReturn(user2);

        userService.create(userDto1);

        final UserDto userDto = userService.update(1L, userDto2);

        assertEquals("Geralt", userDto.getName());
    }

    @Test
    @DisplayName("UserService_updateSetEmail")
    void testUpdateSetEmail() {

        final UserDto userDto3 = new UserDto();
        userDto3.setEmail("Ciri@yandex.ru");

        final User user3 = new User();
        user3.setName("Dima");
        user3.setEmail("Ciri@yandex.ru");
        user3.setId(1L);

        when(userRepository.save(UserMapper.toUser(userDto1))).thenReturn(user1);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        when(userRepository.save(user3)).thenReturn(user3);

        userService.create(userDto1);

        final UserDto userDto = userService.update(1L, userDto3);

        assertEquals("Ciri@yandex.ru", userDto.getEmail());
    }

    @Test
    @DisplayName("UserService_findAll")
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(user1));

        final Collection<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("UserService_deleteNotUser")
    void testDeleteNotUser() {

        assertThrows(
                NotFoundException.class,
                () -> userService.deleteUserById(3L)
        );
    }

    @Test
    @DisplayName("UserService_delete")
    void testDelete() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        userService.deleteUserById(1L);

        verify(userRepository).deleteById(user1.getId());
    }
}