package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

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

    @Query("select b from Booking b where b.item.ownerId = ?1 and b.status = 'REJECTED' or b.status = 'CANCELED'")
    List<Booking> findAllByOwnerIdWithRejectedAndCanceled(
            long ownerId, Sort sort);

    @Query("select (count(b) > 0) " +
            "from Booking b " +
            "where b.item.id = ?1 and b.status = 'APPROVED' and b.start <= ?3 and b.end >= ?2")
    boolean isNotAvailableForBooking(Long itemId, LocalDateTime start, LocalDateTime end);

    @Query("select b " +
            "from Booking b " +
            "where b.item.ownerId = ?1 and ?2 between b.start and b.end")
    List<Booking> findOwnerCurrentForDate(Long bookerId, LocalDateTime date, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndIsBefore(
            long ownerId, LocalDateTime localDateTime, Status status, Sort sort);

    Optional<Booking> findFirstByItemIdAndEndBeforeOrderByEndDesc(
            Long itemId, LocalDateTime localDateTime);

    List<Booking> findFirstByItemInAndEndBefore(
            List<Item> items, LocalDateTime localDateTime, Sort sort);

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(
            Long itemId, LocalDateTime localDateTime);

    List<Booking> findFirstByItemInAndStartAfter(
            List<Item> items, LocalDateTime localDateTime, Sort sort);


    Optional<Booking> findFirstByItemIdAndBookerIdAndEndIsBefore(
            Long itemId, Long bookerId, LocalDateTime localDateTime);

}
