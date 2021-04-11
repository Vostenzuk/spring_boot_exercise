package ru.vostenzuk.jdbctest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.CreateEmployeeRequest;
import ru.vostenzuk.jdbctest.dto.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.UpdateEmployeeRequest;
import ru.vostenzuk.jdbctest.service.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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
    public EmployeeEntity addItem(@PathVariable("employeeId") UUID employeeId,
                                  @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        return employeeService.updateEmployee(employeeId, updateEmployeeRequest);
    }
}
