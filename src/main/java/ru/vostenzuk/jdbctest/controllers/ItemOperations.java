package ru.vostenzuk.jdbctest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.item.ItemUpdateRequestDto;

@RequestMapping("/items")
public interface ItemOperations {

  @Operation(summary = "Получить список всех предметов")
  @GetMapping
  List<ItemDto> getAllItems();

  @Operation(summary = "Получить данные о предмете")
  @GetMapping("/{id}")
  ItemDto getItem(@PathVariable("id") UUID id);

  @Operation(summary = "Создать предмет")
  @PostMapping
  ItemDto createItem(@RequestBody CreateItemRequestDto request);

  @Operation(summary = "Обновить данные о предмете")
  @PatchMapping("/{id}")
  ItemDto updateItem(@PathVariable("id") UUID id,
      @RequestBody ItemUpdateRequestDto request);
}
