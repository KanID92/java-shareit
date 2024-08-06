package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public record BookingOutputDto(
        long id,
        User booker,
        Item item,
        LocalDateTime start,
        LocalDateTime end,
        Status status
) {
}

