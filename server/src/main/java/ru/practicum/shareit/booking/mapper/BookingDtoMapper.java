package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.mapper.ItemDtoMapper;
import ru.practicum.shareit.user.mapper.UserDtoMapper;

@Component
public class BookingDtoMapper {

    public static BookingOutputDto toDto(Booking booking) {
        return new BookingOutputDto(
                booking.getId(),
                UserDtoMapper.toDto(booking.getBooker()),
                ItemDtoMapper.toOutputDto(booking.getItem()),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }

    public static Booking fromCreateDto(BookingCreateDto bookingCreateDto) {
        Booking booking = new Booking();
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setStatus(Status.WAITING);

        return booking;
    }


}
