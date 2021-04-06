package ru.vostenzuk.jdbctest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.CreateEmployeeRequest;
import ru.vostenzuk.jdbctest.dto.CreateItemRequest;
import ru.vostenzuk.jdbctest.dto.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.ItemUpdateRequest;
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

    @GetMapping("/employees")
    public List<EmployeeEntity> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/employees/{id}")
    public EmployeeEntity getEmployee(@PathVariable("id") UUID id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping("/employees")
    public EmployeeEntity createEmployee(@RequestBody CreateEmployeeRequest employeeRequest) {
        return employeeService.createEmployee(employeeRequest);
    }

    @GetMapping("/employees/{id}/expense")
    public ExpenseDto countExpenses(@PathVariable("id") UUID employeeId) {
        return employeeService.getExpenses(employeeId);
    }

    @PostMapping("/employees/{employeeId}/item/{itemId}")
    public EmployeeEntity addItem(@PathVariable("employeeId") UUID employeeId, @PathVariable("itemId") UUID itemId) {
        return employeeService.addItem(employeeId, itemId);
    }

    @DeleteMapping("/employees/{employeeId}/item/{itemId}")
    public EmployeeEntity removeItem(@PathVariable("employeeId") UUID employeeId, @PathVariable("itemId") UUID itemId) {
        return employeeService.removeItem(employeeId, itemId);
    }

    @GetMapping("/items")
    public List<ItemEntity> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/{id}")
    public ItemEntity getItem(@PathVariable("id") UUID id) {
        return itemService.getItem(id);
    }

    @PostMapping("/items")
    public ItemEntity createItem(@RequestBody CreateItemRequest request) {
        return itemService.createItem(request);
    }

    @PatchMapping("/items/{id}")
    public ItemEntity updateItem(@PathVariable("id") UUID id, @RequestBody ItemUpdateRequest request) {
        return itemService.updateItem(id, request);
    }

}
