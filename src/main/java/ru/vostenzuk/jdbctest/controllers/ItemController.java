package ru.vostenzuk.jdbctest.controllers;

import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;
import ru.vostenzuk.jdbctest.service.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
public class ItemController implements ItemOperations {

  private final ItemService service;

  public ItemController(ItemService service) {
    this.service = service;
  }

  @Override
  public List<ItemDto> getAllItems() {
    return service.getAllItems();
  }

  @Override
  public ItemDto getItem(UUID id) {
    return service.getItem(id);
  }

  @Override
  public ItemDto createItem(CreateItemRequestDto request) {
    return service.createItem(request);
  }

  @Override
  public ItemDto updateItem(UUID id, ItemUpdateRequestDto request) {
    return service.updateItem(id, request);
  }
}
