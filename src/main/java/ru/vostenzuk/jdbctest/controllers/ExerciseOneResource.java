package ru.vostenzuk.jdbctest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.*;
import ru.vostenzuk.jdbctest.service.EmployeeService;
import ru.vostenzuk.jdbctest.service.ItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ex1")
public class ExerciseOneResource {

    private final static Logger log = LoggerFactory.getLogger(ExerciseOneResource.class);

    private final EmployeeService employeeService;
    private final ItemService itemService;

    public ExerciseOneResource(EmployeeService employeeService,
                               ItemService itemService) {
        this.employeeService = employeeService;
        this.itemService = itemService;
    }

    @Operation(summary = "Получить список всех сотрудников")
    @GetMapping("/employees")
    public List<EmployeeEntity> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @Operation(summary = "Получить данные о сотруднике")
    @GetMapping("/employees/{id}")
    public EmployeeEntity getEmployee(@PathVariable("id") UUID id) {
        return employeeService.getEmployee(id);
    }

    @Operation(summary = "Создать сотрудника")
    @PostMapping("/employees")
    public EmployeeEntity createEmployee(@RequestBody CreateEmployeeRequest employeeRequest) {
        return employeeService.createEmployee(employeeRequest);
    }

    @Operation(summary = "Получить траты на сотрудника")
    @GetMapping("/employees/{id}/expense")
    public ExpenseDto countExpenses(@PathVariable("id") UUID employeeId) {
        return employeeService.getExpenses(employeeId);
    }

    @Operation(summary = "Обновить данные сотрудника, добавить/удалить предметы")
    @PatchMapping("/employees/{employeeId}")
    public EmployeeEntity addItem(@PathVariable("employeeId") UUID employeeId, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        return employeeService.updateEmployee(employeeId, updateEmployeeRequest);
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
