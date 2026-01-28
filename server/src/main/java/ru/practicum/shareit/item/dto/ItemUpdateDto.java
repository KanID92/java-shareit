package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(of = "id")
public class ItemUpdateDto {

    long id;

    String name;

    String description;

    Boolean available;

    private Long requestId;

}
