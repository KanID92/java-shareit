package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKINGS")
@Getter
@Setter
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "finish")
    private LocalDateTime end;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

}