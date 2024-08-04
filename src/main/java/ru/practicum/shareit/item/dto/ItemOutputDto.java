package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public record ItemOutputDto(
        long id,
        String name,
        String description,
        boolean available,
        BookingOutputDto lastBooking,
        BookingOutputDto nextBooking,
        List<Comment> comments) {
}
