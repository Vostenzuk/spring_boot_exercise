package ru.vostenzuk.jdbctest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.*;
import ru.vostenzuk.jdbctest.service.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "Получить список всех предметов")
    @GetMapping
    public List<ItemEntity> getAllItems() {
        return itemService.getAllItems();
    }

    @Operation(summary = "Получить данные о предмете")
    @GetMapping("/{id}")
    public ItemEntity getItem(@PathVariable("id") UUID id) {
        return itemService.getItem(id);
    }

    @Operation(summary = "Создать предмет")
    @PostMapping
    public ItemEntity createItem(@RequestBody CreateItemRequest request) {
        return itemService.createItem(request);
    }

    @Operation(summary = "Обновить данные о предмете")
    @PatchMapping("/{id}")
    public ItemEntity updateItem(@PathVariable("id") UUID id, @RequestBody ItemUpdateRequest request) {
        return itemService.updateItem(id, request);
    }

}
