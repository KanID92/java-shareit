package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDtoMapper {
    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }

    public static Item fromDto(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.id());
        item.setName(itemDto.name());
        item.setDescription(itemDto.description());
        item.setAvailable(itemDto.available());
        return item;
    }

    public static List<ItemDto> toDtos(List<Item> itemsList) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemsList) {
            itemDtoList.add(ItemDtoMapper.toDto(item));
        }
        return itemDtoList;
    }

}
