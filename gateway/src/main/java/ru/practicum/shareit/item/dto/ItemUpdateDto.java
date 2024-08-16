package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(of = "id")
public class ItemUpdateDto {

//    @NotNull(message = "Updated Item must have id")
    Long id;

    @Size(min = 3, max = 50)
    String name;

    @Size(min = 5, max = 500)
    String description;

    Boolean available;

    private Long requestId;

}
