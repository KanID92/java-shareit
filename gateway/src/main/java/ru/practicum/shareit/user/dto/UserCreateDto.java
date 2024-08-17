package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserCreateDto {

    @NotBlank(message = "Name can't be blank")
    @Size(min = 3, max = 50)
    String name;

    @NotNull(message = "Email should not be null")
    @Email(message = "Email should be valid")
    String email;

}
