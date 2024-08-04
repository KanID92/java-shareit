package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(
            long bookingId, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(
            long bookerUserId, Status status, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(
            long bookerUserId, LocalDateTime localDateTime, Sort sort);

    List<Booking> findAllByBookerIdAndEndIsBefore(
            long bookerUserId, LocalDateTime localDateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfter(
            long bookerUserId, LocalDateTime localDateTime1, LocalDateTime localDateTime2, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(
            long ownerId, Status status, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsBeforeAndEndIsAfterAndStatus(
            long ownerId, LocalDateTime localDateTime1, LocalDateTime localDateTime2, Status status, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndIsBefore(
            long ownerId, LocalDateTime localDateTime, Status status, Sort sort);

    Optional<Booking> findFirstByItemIdAndEndBeforeOrderByEndDesc(
            Long itemId, LocalDateTime localDateTime);

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(
            Long itemId, LocalDateTime localDateTime);


    Optional<Booking> findFirstByItemIdAndBookerIdAndEndIsBefore(
            Long itemId, Long bookerId, LocalDateTime localDateTime);

}
