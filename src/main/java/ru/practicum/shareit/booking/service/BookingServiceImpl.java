package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.mapper.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.SearchState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public BookingOutputDto add(BookingCreateDto bookingCreateDto, long bookingUserId) {
        User bookingUser = userRepository.findById(bookingUserId)
                .orElseThrow(() -> new NotFoundException("User with id " + bookingUserId + " not found"));
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException(
                        "Item with id " + bookingCreateDto.getItemId() + " not found"));
        if (!item.getIsAvailable()) {
            throw new NotAvailableException(
                    "Item + " + bookingCreateDto.getItemId() + ": " + item.getName() + " - is not available");
        }

        Booking booking = BookingDtoMapper.fromCreateDto(bookingCreateDto);
        booking.setBooker(bookingUser);
        booking.setItem(item);
        return BookingDtoMapper.toDto(bookingRepository.save(booking));
    }


    @Override
    public BookingOutputDto patchApproving(long bookingId, boolean isApproved, long ownerUserId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        if (ownerUserId == booking.getItem().getOwnerId()) {
            if (isApproved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            return BookingDtoMapper.toDto(bookingRepository.save(booking));
        } else {
            throw new ValidationException("User with id = " + ownerUserId + " do not own this item");
        }
    }

    @Override
    public BookingOutputDto get(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));
        return BookingDtoMapper.toDto(booking);
    }

    @Override
    public List<BookingOutputDto> getCurrentBookingsByBookerUserId(long bookerUserId, String state) {
        userRepository.findById(bookerUserId)
                .orElseThrow(() -> new NotFoundException("User with id " + bookerUserId + " not found"));

        List<Booking> bookingsList;

        switch (SearchState.valueOf(state)) {
            case ALL -> bookingsList = bookingRepository.findAllByBookerId(
                    bookerUserId, sortByStartDesc);

            case CURRENT -> bookingsList = bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
                    bookerUserId, LocalDateTime.now(), LocalDateTime.now(), sortByStartDesc);

            case PAST -> bookingsList = bookingRepository.findAllByBookerIdAndEndIsBefore(
                    bookerUserId, LocalDateTime.now(), sortByStartDesc);

            case FUTURE -> bookingsList = bookingRepository.findAllByBookerIdAndStartIsAfter(
                    bookerUserId, LocalDateTime.now(), sortByStartDesc);

            case WAITING -> bookingsList = bookingRepository.findAllByBookerIdAndStatus(
                    bookerUserId, Status.WAITING, sortByStartDesc);

            case REJECTED -> bookingsList = bookingRepository.findAllByBookerIdAndStatus(
                    bookerUserId, Status.REJECTED, sortByStartDesc);

            default -> throw new ValidationException("Unknown state " + state);
        }

        System.out.println(bookingsList);

        return bookingsList.stream()
                .map(BookingDtoMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingOutputDto> getCurrentBookingsByOwnerId(long ownerUserId, String state) {
        userRepository.findById(ownerUserId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerUserId + " not found"));

        List<Booking> bookingsList;

        switch (SearchState.valueOf(state)) {
            case ALL -> bookingsList = bookingRepository.findAllByItemOwnerIdAndStatus(
                    ownerUserId, Status.APPROVED, sortByStartDesc);

            case CURRENT -> bookingsList = bookingRepository.findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterAndStatus(
                    ownerUserId, LocalDateTime.now(), LocalDateTime.now(), Status.APPROVED, sortByStartDesc);

            case PAST -> bookingsList = bookingRepository.findAllByItemOwnerIdAndEndIsBefore(
                    ownerUserId, LocalDateTime.now(), Status.APPROVED, sortByStartDesc);

            case FUTURE -> bookingsList = bookingRepository.findAllByBookerIdAndStartIsAfter(
                    ownerUserId, LocalDateTime.now(), sortByStartDesc);

            case WAITING -> bookingsList = bookingRepository.findAllByItemOwnerIdAndStatus(
                    ownerUserId, Status.WAITING, sortByStartDesc);

            case REJECTED -> bookingsList = bookingRepository.findAllByItemOwnerIdAndStatus(
                    ownerUserId, Status.REJECTED, sortByStartDesc);

            default -> throw new ValidationException("Unknown state " + state);
        }

        return bookingsList.stream()
                .map(BookingDtoMapper::toDto)
                .toList();
    }

}
