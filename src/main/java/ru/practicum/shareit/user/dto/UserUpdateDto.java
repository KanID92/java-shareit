package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class UserUpdateDto {

    @NotNull(message = "Updated User must have id")
    Long id;

    @Size(min = 3, max = 50)
    String name;

    @Email(message = "Email should be valid")
    String email;

}
