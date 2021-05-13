package ru.vostenzuk.jdbctest.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ItemListUpdateDto;

@ExtendWith(MockitoExtension.class)
class ResourceManagementServiceTest {

  @Mock
  private EmployeeService employeeService;
  @Mock
  private ItemService itemService;

  private ResourceManagementService service;

  private final List<EmployeeDto> employees = new ArrayList<>();
  private final Set<ItemDto> items = new HashSet<>();

  @BeforeEach
  void setUp() {
    service = new ResourceManagementServiceImpl(employeeService, itemService);

    for (int i = 0; i < 2; i++) {
      employees
          .add(EmployeeDto.builder()
              .id(UUID.randomUUID())
              .firstName("firstName" + i)
              .lastName("lastName" + i)
              .position("position" + i)
              .build());
    }

    for (int i = 0; i < 2; i++) {
      items.add(ItemDto.builder()
          .id(UUID.randomUUID())
          .type("type" + i)
          .price(BigDecimal.ONE)
          .build());
    }

    employees.get(0).setItems(items);

  }

  @Test
  @DisplayName("Считаем расходы на сотрудника по ID")
  void countExpensesById() {
    EmployeeDto employee = employees.get(0);
    when(employeeService.getEmployee(employees.get(0).getId())).thenReturn(employee);

    ExpenseDto expense = service.countExpensesById(employee.getId());

    assertThat(expense).isEqualTo(new ExpenseDto(BigDecimal.valueOf(2)));
  }

  @Test
  @DisplayName("Добавляем предметы на сотрудника")
  void updateEmployeeItemsAddItemsSuccess() {
    ItemListUpdateDto request = new ItemListUpdateDto();

    request.setAddItems(items.stream().map(ItemDto::getId).collect(
        Collectors.toSet()));
    request.setRemoveItems(Collections.emptySet());

    EmployeeDto employee = employees.get(1);

    when(employeeService.getEmployee(employee.getId())).thenReturn(employee);
    when(itemService.getAllByIds(request.getAddItems())).thenReturn(items);
    when(itemService.getAllByIds(request.getRemoveItems())).thenReturn(Collections.emptySet());

    service.updateEmployeeItems(employee.getId(), request);
    assertThat(employee.getItems()).hasSameElementsAs(items);
  }

  @Test
  @DisplayName("Добавляем на сотрудника уже прикрепленный к другому предмет")
  void updateEmployeeItemsAddItemsOfAnotherEmployee() {
    ItemListUpdateDto request = new ItemListUpdateDto();
    EmployeeDto employeeWithItems = employees.get(0);
    employeeWithItems.setItems(items);

    EmployeeDto employeeWithoutItems = employees.get(1);

    request.setAddItems(items.stream().map(ItemDto::getId).collect(Collectors.toSet()));
    request.setRemoveItems(Collections.emptySet());

    when(employeeService.getEmployee(employeeWithoutItems.getId()))
        .thenReturn(employeeWithoutItems);
    when(itemService.getAllByIds(request.getAddItems())).thenReturn(items);
    when(itemService.getAllByIds(request.getRemoveItems())).thenReturn(Collections.emptySet());
    doThrow(ConstraintViolationException.class).when(employeeService).persist(employeeWithoutItems);
    when(employeeService.findByItemId(items.iterator().next().getId()))
        .thenReturn(Optional.of(employeeWithItems));

    assertThatThrownBy(() -> service.updateEmployeeItems(employeeWithoutItems.getId(), request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Item with id " + items.iterator().next().getId() + " belongs to user "
            + employeeWithItems.getId());

  }

  @Test
  @DisplayName("Удаляем предметы")
  void removeItems() {
    ItemListUpdateDto request = new ItemListUpdateDto();
    request.setRemoveItems(items.stream().map(ItemDto::getId).collect(Collectors.toSet()));

    EmployeeDto employeeWithItems = employees.get(0);
    employeeWithItems.setItems(items);

    when(employeeService.getEmployee(employeeWithItems.getId())).thenReturn(employeeWithItems);
    when(itemService.getAllByIds(request.getAddItems())).thenReturn(Collections.emptySet());
    when(itemService.getAllByIds(request.getRemoveItems())).thenReturn(items);

    service.updateEmployeeItems(employeeWithItems.getId(), request);

    assertThat(employeeWithItems.getItems().size()).isEqualTo(0);
  }
}