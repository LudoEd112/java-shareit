package ru.practicum.shareit.booking.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_date")
    LocalDateTime startBooking;

    @Column(name = "end_date")
    LocalDateTime endBooking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id")
    User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    BookingStatus status;

    public Booking(LocalDateTime startBooking, LocalDateTime endBooking, Item item, User booker) {
        this.startBooking = startBooking;
        this.endBooking = endBooking;
        this.item = item;
        this.booker = booker;
        this.status = BookingStatus.WAITING;
    }
}
