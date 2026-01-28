package ru.practicum.shareit.request.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemRequestCreateDto {

    @NotNull
    @Size(min = 5, max = 513)
    String description;

}
