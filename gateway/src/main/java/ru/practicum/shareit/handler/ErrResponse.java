package ru.practicum.shareit.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrResponse {
    String error;
    String description;
}
