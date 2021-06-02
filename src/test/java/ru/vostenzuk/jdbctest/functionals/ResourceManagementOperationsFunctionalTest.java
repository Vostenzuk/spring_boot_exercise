package ru.vostenzuk.jdbctest.functionals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.dto.item.CreateItemRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.dto.resourceManagement.ItemListUpdateDto;
import ru.vostenzuk.jdbctest.service.EmployeeService;
import ru.vostenzuk.jdbctest.service.ItemService;
import ru.vostenzuk.jdbctest.service.ResourceManagementService;

public class ResourceManagementOperationsFunctionalTest extends AbstractRestFunctionalTest {

  @Autowired
  private EmployeeService employeeService;
  @Autowired
  private ItemService itemService;
  @Autowired
  private ResourceManagementService service;

  public static final String BASE_URL = "/resource-management/employee/";


  @Test
  @DisplayName("POSITIVE: Получаем расходы по сотруднику, у которого нет предметов")
  public void givenEmployeeDoesntHaveItems_whenGetEmployeeExpenses_thenReturnZero()
      throws Exception {

    EmployeeDto employee = employeeService
        .createEmployee(easyRandom.nextObject(EmployeeRequestDto.class));

    mockMvc.perform(get(BASE_URL + employee.getId() + "/expense"))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.amount").value(BigDecimal.ZERO));
  }

  @Test
  @DisplayName("POSITIVE: Получаем расходы по сотруднику, у которого есть предметы")
  public void givenEmployeeHasItems_whenGetEmployeeExpenses_thenReturnExpenses() throws Exception {
    EmployeeDto employeeDto = employeeService
        .createEmployee(easyRandom.nextObject(EmployeeRequestDto.class));

    ItemDto item1 = itemService.createItem(
        CreateItemRequestDto.builder()
            .type(easyRandom.nextObject(String.class))
            .price(BigDecimal.TEN).build());
    ItemDto item2 = itemService.createItem(
        CreateItemRequestDto.builder()
            .type(easyRandom.nextObject(String.class))
            .price(BigDecimal.ONE).build()
    );

    Set<UUID> itemsToAdd = Sets.newSet(item1.getId(), item2.getId());

    service.updateEmployeeItems(employeeDto.getId(), ItemListUpdateDto.builder()
        .addItems(itemsToAdd)
        .removeItems(Collections.emptySet())
        .build());

    mockMvc.perform(get(BASE_URL + employeeDto.getId() + "/expense"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.amount")
            .value(item1.getPrice().add(item2.getPrice()).doubleValue()));
  }

  @Test
  @DisplayName("Positive: Прицепляем к сотруднику один предмет")
  public void givenEmployeeExistsAndItemExist_whenBindItemToEmployee_thenBindAndReturnEmployeeWithItem()
      throws Exception {

    EmployeeDto employee = employeeService
        .createEmployee(easyRandom.nextObject(EmployeeRequestDto.class));
    ItemDto item = itemService.createItem(easyRandom.nextObject(CreateItemRequestDto.class));

    ItemListUpdateDto request = new ItemListUpdateDto();
    request.setAddItems(Collections.singleton(item.getId()));
    request.setRemoveItems(Collections.emptySet());

    mockMvc.perform(patch(BASE_URL + employee.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isNoContent());

    EmployeeDto updatedEmployee = employeeService.getEmployee(employee.getId());

    assertThat(updatedEmployee.getItems()).hasSize(1);
  }

  @Test
  @DisplayName("Positive: Удаляем предмет с сотрудника")
  public void givenEmployeeHasItem_whenUnbindItem_thenReturnEmployeeWithoutItem() throws Exception {
    EmployeeDto employeeDto = employeeService
        .createEmployee(easyRandom.nextObject(EmployeeRequestDto.class));

    ItemDto itemDto = itemService.createItem(easyRandom.nextObject(CreateItemRequestDto.class));

    service.updateEmployeeItems(employeeDto.getId(), ItemListUpdateDto.builder()
        .addItems(Collections.singleton(itemDto.getId()))
        .removeItems(Collections.emptySet())
        .build());

    ItemListUpdateDto request = ItemListUpdateDto.builder().addItems(Collections.emptySet())
        .removeItems(Collections.singleton(itemDto.getId()))
        .build();

    mockMvc.perform(patch(BASE_URL + employeeDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isNoContent());

    EmployeeDto updatedEmployee = employeeService.getEmployee(employeeDto.getId());

    assertThat(updatedEmployee.getItems()).hasSize(0);
  }

  @Test
  @DisplayName("NEGATIVE: Добавляем предмет, принадлежащий другому сотруднику")
  public void givenItemBelongsToEmployee_whenBindItemToAnotherEmployee_thenReturnConflict()
      throws Exception {
    EmployeeDto employeeWithItem = employeeService
        .createEmployee(easyRandom.nextObject(EmployeeRequestDto.class));

    ItemDto itemDto = itemService.createItem(easyRandom.nextObject(CreateItemRequestDto.class));

    service.updateEmployeeItems(employeeWithItem.getId(),
        ItemListUpdateDto.builder()
            .addItems(Collections.singleton(itemDto.getId()))
            .removeItems(Collections.emptySet())
            .build());

    EmployeeDto employeeWithoutItem = employeeService
        .createEmployee(easyRandom.nextObject(EmployeeRequestDto.class));

    ItemListUpdateDto request = ItemListUpdateDto.builder()
        .addItems(Collections.singleton(itemDto.getId())).removeItems(Collections.emptySet())
        .build();

    mockMvc.perform(patch(BASE_URL + employeeWithoutItem.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.errorMessage", equalTo(
            "Item with id " + itemDto.getId() + " belongs to user " + employeeWithItem
                .getId())));
  }

}
