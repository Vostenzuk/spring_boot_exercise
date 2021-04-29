package ru.vostenzuk.jdbctest.controllers;

import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;
import ru.vostenzuk.jdbctest.mapper.ItemMapper;
import ru.vostenzuk.jdbctest.service.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
public class ItemController implements ItemOperations {

  private final ItemService service;
  private final ItemMapper mapper;

  public ItemController(ItemService service, ItemMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }


  @Override
  public List<ItemDto> getAllItems() {
    return service.getAllItems().stream().map(mapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  public ItemDto getItem(UUID id) {
    return mapper.fromEntity(service.getItem(id));
  }

  @Override
  public ItemDto createItem(CreateItemRequestDto request) {
    return mapper.fromEntity(service.createItem(mapper.toEntity(request)));
  }

  @Override
  public ItemDto updateItem(UUID id, ItemUpdateRequestDto request) {
    return mapper.fromEntity(service.updateItem(id, mapper.toEntity(request)));
  }
}
