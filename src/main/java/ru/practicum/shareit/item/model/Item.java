package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode(of = "id")
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private long requestId;
}
