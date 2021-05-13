package ru.vostenzuk.jdbctest.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;

public interface ItemService {

  List<ItemDto> getAllItems();

  Set<ItemDto> getAllByIds(Set<UUID> ids);

  ItemDto getItem(UUID id);

  ItemDto createItem(CreateItemRequestDto request);

  ItemDto updateItem(UUID id, ItemUpdateRequestDto request);

}
