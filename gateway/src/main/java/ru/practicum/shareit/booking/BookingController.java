package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingClient bookingClient;

	private static final String USER_ID_REQUEST_HEADER = "X-Sharer-User-Id";

	@Validated
	@PostMapping
	public ResponseEntity<Object> add(
			@Valid @RequestBody BookingCreateDto bookingCreateDto,
			@RequestHeader(USER_ID_REQUEST_HEADER) long bookingUserId) {
		log.info("==> POST. Adding new booking: {} by user with id {}", bookingCreateDto, bookingUserId);
		ResponseEntity<Object> receivedBookingOutputDto = bookingClient.add(bookingCreateDto, bookingUserId);
		log.info("<== POST. Added new booking: {}", receivedBookingOutputDto);
		return receivedBookingOutputDto;
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> patchApproving(
			@PathVariable long bookingId,
			@RequestParam(name = "approved") boolean isApproved,
			@RequestHeader(USER_ID_REQUEST_HEADER) long ownerUserId) {
		log.info("==> PATCH. Set approving {} booking with id: {} by user with id {}",
				isApproved, bookingId, ownerUserId);
		ResponseEntity<Object> receivedBookingOutputDto = bookingClient.patchApproving(
				bookingId, isApproved, ownerUserId);
		log.info("<== PATCH. Return approving booking {} ", receivedBookingOutputDto);
		return receivedBookingOutputDto;
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> get(@PathVariable long bookingId,
								@RequestHeader(USER_ID_REQUEST_HEADER) long userId) {
		log.info("==> GET. Getting info about booking with id: {} by user with id {}",
				bookingId, userId);
		ResponseEntity<Object> receivedBookingOutputDto = bookingClient.get(bookingId, userId);
		log.info("<== GET. Return booking {} ", receivedBookingOutputDto);
		return receivedBookingOutputDto;
	}

	@GetMapping
	public ResponseEntity<Object> getCurrentBookingsByBookerUserId(
			@RequestHeader(USER_ID_REQUEST_HEADER) long bookingUserId,
			@RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
		log.info("==> GET. Getting current {} booking by user with id: {}", state, bookingUserId);
		ResponseEntity<Object> receivedBookingOutputDtoList = bookingClient.getCurrentBookingsByBookerUserId(
				bookingUserId, state);
		log.info("<== GET. Returning current {} bookings by user with id {}: {} ",
				state, bookingUserId, receivedBookingOutputDtoList);
		return receivedBookingOutputDtoList;
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getCurrentBookingsByOwnerId(
			@RequestHeader(USER_ID_REQUEST_HEADER) long ownerUserId,
			@RequestParam(name = "status", required = false) String state) {
		log.info("==> GET. Getting current {} booking of own items of user with id: {}", state, ownerUserId);
		ResponseEntity<Object> receivedBookingOutputDtoList = bookingClient.getCurrentBookingsByOwnerId(
				ownerUserId, state);
		log.info("<== GET. Returning current bookings {} of own items by user with id {}: {} ",
				state, ownerUserId, receivedBookingOutputDtoList);
		return receivedBookingOutputDtoList;
	}


}
