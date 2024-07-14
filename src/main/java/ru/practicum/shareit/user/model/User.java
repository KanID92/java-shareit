package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.validation.Marker;


@Data
@EqualsAndHashCode(of = "id")
public class User {

    @Null(groups = Marker.OnCreate.class, message = "Creating user can't already have id")
    @NotNull(groups = Marker.OnUpdate.class, message = "Updating user must have id")
    private Long id;

    @NotBlank(groups = Marker.OnCreate.class, message = "Name can't be blank")
    @Size(min = 3, max = 50)
    private String name;

    @NotNull(groups = Marker.OnCreate.class,
            message = "Email should not be null")
    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class},
            message = "Email should be valid")
    private String email;
}
