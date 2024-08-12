package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemOutputDto;
import ru.practicum.shareit.item.dto.ItemShortOutputDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public class ItemDtoMapper {

    public static ItemOutputDto toOutputDto(Item item) {
        return new ItemOutputDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                null,
                null,
                null,
                item.getRequestId() != null ? item.getRequestId() : null);
    }

    public static ItemOutputDto toOutputDto(Item item, List<Comment> commentList) {
        return new ItemOutputDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                null,
                null,
                commentList
                        .stream()
                        .map(CommentDtoMapper::toOutputDto)
                        .toList(),
                item.getRequestId() != null ? item.getRequestId() : null);
    }

    public static ItemOutputDto toOutputDtoWithBooking(
            Item item, BookingOutputDto lastBookingDto, BookingOutputDto nextBookingDto, List<Comment> commentList) {
        return new ItemOutputDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                lastBookingDto,
                nextBookingDto,
                commentList
                        .stream()
                        .map(CommentDtoMapper::toOutputDto)
                        .toList(),
                item.getRequestId() != null ? item.getRequestId() : null);
    }

    public static ItemShortOutputDto toShortOutputDto(Item item) {
        return new ItemShortOutputDto(
                item.getId(),
                item.getName(),
                item.getOwner().getId()
        );
    }

    public static Item fromCreateDto(ItemCreateDto itemCreateDto) {
        Item item = new Item();
        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setIsAvailable(itemCreateDto.getAvailable());
        item.setRequestId(itemCreateDto.getRequestId() != null ? itemCreateDto.getRequestId() : null);
        return item;
    }

    public static Item fromUpdateDto(ItemUpdateDto itemUpdateDto) {
        Item item = new Item();
        item.setId(itemUpdateDto.getId());
        item.setName(itemUpdateDto.getName());
        item.setDescription(itemUpdateDto.getDescription());
        item.setIsAvailable(itemUpdateDto.getAvailable());
        item.setRequestId(itemUpdateDto.getRequestId() != null ? itemUpdateDto.getRequestId() : null);
        return item;
    }



}
