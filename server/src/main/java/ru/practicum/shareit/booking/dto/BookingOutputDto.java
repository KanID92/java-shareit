package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.user.dto.UserOutputDto;

import java.time.LocalDateTime;

public record BookingOutputDto(
        long id,
        UserOutputDto booker,
        ItemOutputDto item,
        LocalDateTime start,
        LocalDateTime end,
        Status status
) {
}

