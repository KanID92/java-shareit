package ru.practicum.shareit.item.dto;


public record ItemOutputDto(
        long id,
        String name,
        String description,
        boolean available) {
}
