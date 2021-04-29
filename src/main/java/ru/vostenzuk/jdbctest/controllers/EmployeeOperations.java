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
import org.springframework.web.bind.annotation.RestController;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;

@RequestMapping("/employees")
public interface EmployeeOperations {

  @Operation(summary = "Получить список всех сотрудников")
  @GetMapping
  List<EmployeeDto> getEmployees();

  @Operation(summary = "Получить данные о сотруднике")
  @GetMapping("/{id}")
  EmployeeDto getEmployee(@PathVariable("id") UUID id);

  @Operation(summary = "Создать сотрудника")
  @PostMapping
  EmployeeDto createEmployee(@RequestBody EmployeeRequestDto employeeRequest);

  @Operation(summary = "Обновить данные сотрудника")
  @PatchMapping("/{id}")
  EmployeeDto updateEmployee(@PathVariable("id") UUID employeeId,
      @RequestBody EmployeeRequestDto employeeRequest);
}
