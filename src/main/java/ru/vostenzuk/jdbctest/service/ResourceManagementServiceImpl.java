package ru.vostenzuk.jdbctest.service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ItemListUpdateDto;

@Service
public class ResourceManagementServiceImpl implements ResourceManagementService {

  private final EmployeeService employeeService;
  private final ItemService itemService;

  public ResourceManagementServiceImpl(EmployeeService employeeRepository,
      ItemService itemService) {
    this.employeeService = employeeRepository;
    this.itemService = itemService;
  }


  @Override
  public ExpenseDto countExpensesById(UUID employeeId) {
    return new ExpenseDto(employeeService.getEmployee(employeeId)
        .getItems()
        .stream()
        .map(ItemDto::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add));
  }

  @Override
  public void updateEmployeeItems(UUID employeeId, ItemListUpdateDto request) {
    EmployeeDto employee = employeeService.getEmployee(employeeId);

    Set<ItemDto> itemsToAdd = itemService.getAllByIds(request.getAddItems());
    Set<ItemDto> itemsToRemove = itemService.getAllByIds(request.getRemoveItems());

    Set<ItemDto> items = employee.getItems();
    items.addAll(itemsToAdd);
    items.removeAll(itemsToRemove);

    try {
      employeeService.persist(employee);
    } catch (Exception ex) {
      for (ItemDto item : itemsToAdd) {
        UUID itemHolderId = employeeService.findByItemId(item.getId())
            .map(EmployeeDto::getId)
            .orElse(null);
        if (!Objects.isNull(itemHolderId) && !Objects.equals(employee.getId(), itemHolderId)) {
          throw new IllegalArgumentException(
              "Item with id " + item.getId() + " belongs to user " + itemHolderId);
        }
      }
    }
  }

}