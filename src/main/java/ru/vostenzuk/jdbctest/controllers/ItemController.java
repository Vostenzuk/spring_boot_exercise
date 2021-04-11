package ru.vostenzuk.jdbctest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.*;
import ru.vostenzuk.jdbctest.service.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ex1")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Получить список всех предметов")
    @GetMapping("/items")
    public List<ItemEntity> getAllItems() {
        return itemService.getAllItems();
    }

    @Operation(summary = "Получить данные о предмете")
    @GetMapping("/items/{id}")
    public ItemEntity getItem(@PathVariable("id") UUID id) {
        return itemService.getItem(id);
    }

    @Operation(summary = "Создать предмет")
    @PostMapping("/items")
    public ItemEntity createItem(@RequestBody CreateItemRequest request) {
        return itemService.createItem(request);
    }

    @Operation(summary = "Обновить данные о предмете")
    @PatchMapping("/items/{id}")
    public ItemEntity updateItem(@PathVariable("id") UUID id, @RequestBody ItemUpdateRequest request) {
        return itemService.updateItem(id, request);
    }

}
