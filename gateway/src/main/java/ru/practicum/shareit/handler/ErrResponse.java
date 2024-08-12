package ru.practicum.shareit.handler;

import lombok.Getter;

@Getter
public class ErrResponse {
    String error;
    String description;

    public ErrResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
