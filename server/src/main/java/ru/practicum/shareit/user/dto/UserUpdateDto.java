package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class UserUpdateDto {

    Long id;

    String name;

    String email;

}
