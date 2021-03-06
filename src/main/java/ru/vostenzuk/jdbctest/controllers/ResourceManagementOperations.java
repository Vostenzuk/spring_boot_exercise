package ru.vostenzuk.jdbctest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ItemListUpdateDto;

@RequestMapping("/resource-management/employee")
public interface ResourceManagementOperations {

  @Operation(summary = "Получить данные о расходах на сотрудника")
  @GetMapping("/{id}/expense")
  ExpenseDto getEmployeeExpenses(@PathVariable("id") UUID employeeId);

  @Operation(summary = "Обновить список предметов сотрудника")
  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void updateEmployeeItems(@PathVariable("id") UUID id, @RequestBody ItemListUpdateDto itemListUpdateDto);
}
