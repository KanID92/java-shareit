package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;


public record CommentOutputDto(
        long id,
        String text,
        ItemOutputDto item,
        String authorName,
        LocalDateTime created) {

}

