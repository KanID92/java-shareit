package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ITEMS")
@Getter
@Setter
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "owner_id", nullable = false)
    private long ownerId;

    @Column(name = "request_id")
    private Long requestId;

}
