package ru.vostenzuk.jdbctest.service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ItemListUpdateDto;

@Service
public class ResourceManagementServiceImpl implements ResourceManagementService {

  private final EmployeeService employeeService;
  private final ItemService itemService;

  public ResourceManagementServiceImpl(
      EmployeeService employeeRepository,
      ItemService itemService) {
    this.employeeService = employeeRepository;
    this.itemService = itemService;
  }


  @Override
  public ExpenseDto countExpensesById(UUID employeeId) {
    return new ExpenseDto(employeeService.getEmployee(employeeId)
        .getItems()
        .stream()
        .map(ItemEntity::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add));
  }

  @Override
  public void updateEmployeeItems(UUID employeeId, ItemListUpdateDto request) {
    EmployeeEntity employeeEntity = employeeService.getEmployee(employeeId);

    Set<ItemEntity> itemsToAdd = itemService.getAllByIds(request.getAddItems());
    Set<ItemEntity> itemsToRemove = itemService.getAllByIds(request.getRemoveItems());

    Set<ItemEntity> items = employeeEntity.getItems();
    items.addAll(itemsToAdd);
    items.removeAll(itemsToRemove);

    try {
      employeeService.persist(employeeEntity);
    } catch (Exception ex) {
      for (ItemEntity item : itemsToAdd) {
        UUID itemHolder = employeeService.findByItemId(item.getId())
            .map(EmployeeEntity::getId)
            .orElse(null);
        if (!Objects.isNull(itemHolder) && !Objects.equals(employeeEntity.getId(), itemHolder)) {
          throw new IllegalArgumentException(
              "Item with id " + item.getId() + " belongs to user " + itemHolder);
        }
      }
    }
  }

}