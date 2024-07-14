package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.validation.Marker;

@Data
@EqualsAndHashCode(of = "id")
public class UserDto {

        Long id;

        @NotBlank(groups = Marker.OnCreate.class, message = "Name can't be blank")
        @Size(min = 3, max = 50)
        String name;

        @NotNull(groups = Marker.OnCreate.class,
                message = "Email should not be null")
        @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class},
                message = "Email should be valid")
        String email;

}
