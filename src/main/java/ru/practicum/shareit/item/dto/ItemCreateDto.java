package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemCreateDto {

    @NotBlank(message = "Name can't be blank")
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank(message = "Description can't be blank")
    @Size(min = 5, max = 500)
    private String description;

    @NotNull(message = "Available of Item can't be empty")
    private Boolean available;

}
