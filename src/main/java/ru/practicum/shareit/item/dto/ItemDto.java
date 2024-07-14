package ru.practicum.shareit.item.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.validation.Marker;

@Data
@EqualsAndHashCode(of = "id")
public class ItemDto {

        @Null(groups = Marker.OnCreate.class, message = "Created item can't have id")
        @NotNull(groups = Marker.OnUpdate.class, message = "Update Item must have id")
        Long id;

        @NotBlank(groups = Marker.OnCreate.class, message = "Name can't be blank")
        @Size(min = 3, max = 50)
        String name;

        @NotBlank(groups = Marker.OnCreate.class, message = "Description can't be blank")
        @Size(min = 5, max = 500)
        String description;

        @NotNull(groups = Marker.OnCreate.class, message = "Available of Item can't be empty")
        Boolean available;
}
