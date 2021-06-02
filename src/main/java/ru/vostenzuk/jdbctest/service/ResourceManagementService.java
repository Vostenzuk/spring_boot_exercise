package ru.vostenzuk.jdbctest.service;

import java.util.UUID;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ItemListUpdateDto;

public interface ResourceManagementService {

  ExpenseDto countExpensesById(UUID id);

  void updateEmployeeItems(UUID employeeId, ItemListUpdateDto request);
}
