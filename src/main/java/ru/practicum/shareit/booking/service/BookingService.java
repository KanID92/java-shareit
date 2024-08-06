package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;

import java.util.List;

public interface BookingService {

    BookingOutputDto add(BookingCreateDto bookingCreateDto, long bookingUserId);

    BookingOutputDto patchApproving(long bookingId, boolean isApproved, long ownerUserId);

    BookingOutputDto get(long bookingId, long userId);

    List<BookingOutputDto> getCurrentBookingsByBookerUserId(long bookerUserId, String state);

    List<BookingOutputDto> getCurrentBookingsByOwnerId(long ownerUserId, String state);

}
