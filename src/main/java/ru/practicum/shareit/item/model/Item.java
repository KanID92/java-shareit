package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.validation.Marker;

@Data
@EqualsAndHashCode(of = "id")
public class Item {

    @Null(groups = Marker.OnCreate.class, message = "Created Item can't already have id")
    @NotNull(groups = Marker.OnUpdate.class, message = "Update Item must have id")
    private Long id;

    @NotBlank(groups = Marker.OnCreate.class, message = "Name can't be blank")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank(groups = Marker.OnCreate.class, message = "Description can't be blank")
    @Size(min = 5, max = 500)
    private String description;

    @NotNull(groups = Marker.OnCreate.class, message = "Available of Item can't be empty")
    private Boolean available;

    private long ownerId;
    private long requestId;
}
