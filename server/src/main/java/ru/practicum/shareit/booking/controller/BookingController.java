package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingFullDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingFullDto approved(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long bookingId, @RequestParam Boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingFullDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable("bookingId") Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingFullDto> getAllByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingFullDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(ownerId, state);
    }
}