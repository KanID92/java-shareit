package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

    @Validated
    @PostMapping
    public BookingOutputDto add(
            @Valid @RequestBody BookingCreateDto bookingCreateDto,
            @RequestHeader(USER_ID_REQUEST_HEADER) long bookingUserId) {
        log.info("==> POST. Adding new booking: {} by user with id {}", bookingCreateDto, bookingUserId);
        BookingOutputDto receivedBookingOutputDto = bookingService.add(bookingCreateDto, bookingUserId);
        log.info("<== POST. Added new booking: {}", receivedBookingOutputDto);
        return receivedBookingOutputDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto patchApproving(
            @PathVariable long bookingId,
            @RequestParam(name = "approved") boolean isApproved,
            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerUserId) {
        log.info("==> PATCH. Set approving {} booking with id: {} by user with id {}",
                isApproved, bookingId, ownerUserId);
        BookingOutputDto receivedBookingOutputDto = bookingService.patchApproving(
                bookingId, isApproved, ownerUserId);
        log.info("<== PATCH. Return approving booking {} ", receivedBookingOutputDto);
        return receivedBookingOutputDto;
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto get(@PathVariable long bookingId,
                                @RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
        log.info("==> GET. Getting info about booking with id: {} by user with id {}",
                bookingId, userId);
        BookingOutputDto receivedBookingOutputDto = bookingService.get(bookingId, userId);
        log.info("<== GET. Return booking {} ", receivedBookingOutputDto);
        return receivedBookingOutputDto;
    }

    @GetMapping
    public List<BookingOutputDto> getCurrentBookingsByBookerUserId(
            @RequestHeader(USER_ID_REQUEST_HEADER) long bookingUserId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        log.info("==> GET. Getting current {} booking by user with id: {}", state, bookingUserId);
        List<BookingOutputDto> receivedBookingOutputDtoList = bookingService.getCurrentBookingsByBookerUserId(
                bookingUserId, state);
        log.info("<== GET. Returning current {} bookings by user with id {}: {} ",
                state, bookingUserId, receivedBookingOutputDtoList);
        return receivedBookingOutputDtoList;
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getCurrentBookingsByOwnerId(
            @RequestHeader(USER_ID_REQUEST_HEADER) long ownerUserId,
            @RequestParam(name = "status", required = false) String state) {
        log.info("==> GET. Getting current {} booking of own items of user with id: {}", state, ownerUserId);
        List<BookingOutputDto> receivedBookingOutputDtoList = bookingService.getCurrentBookingsByOwnerId(
                ownerUserId, state);
        log.info("<== GET. Returning current bookings {} of own items by user with id {}: {} ",
                state, ownerUserId, receivedBookingOutputDtoList);
        return receivedBookingOutputDtoList;
    }


}
