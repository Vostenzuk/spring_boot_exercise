package ru.vostenzuk.jdbctest.controllers;

import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ItemListUpdateDto;
import ru.vostenzuk.jdbctest.service.ResourceManagementService;

@RestController
public class ResourceManagementController implements ResourceManagementOperations {

  private final ResourceManagementService service;

  public ResourceManagementController(ResourceManagementService service) {
    this.service = service;
  }

  @Override
  public ExpenseDto getEmployeeExpenses(UUID employeeId) {
    return service.countExpensesById(employeeId);
  }

  @Override
  public void updateEmployeeItems(UUID id, ItemListUpdateDto itemListUpdateDto) {
    service.updateEmployeeItems(id, itemListUpdateDto);
  }
}
