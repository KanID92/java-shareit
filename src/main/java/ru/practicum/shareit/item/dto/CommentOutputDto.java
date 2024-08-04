package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;


public record CommentOutputDto(
        long id,
        String text,
        Item item,
        String authorName,
        LocalDateTime created) {

}

