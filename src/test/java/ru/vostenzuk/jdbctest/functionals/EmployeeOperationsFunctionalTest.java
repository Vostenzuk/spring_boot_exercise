package ru.vostenzuk.jdbctest.functionals;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;

public class EmployeeOperationsFunctionalTest extends AbstractRestFunctionalTest {

  @Autowired
  private EmployeeRepository repository;

  private static final String BASE_URL = "/employees";

  @Test
  @DisplayName("POSITIVE: Создаём нового пользователя")
  public void givenEmployeeDoesntExist_whenCreateEmployee_thenCreateAndReturnEmployee()
      throws Exception {
    EmployeeRequestDto request = easyRandom.nextObject(EmployeeRequestDto.class);

    mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(request.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(request.getLastName()))
        .andExpect(jsonPath("$.position").value(request.getPosition()))
        .andExpect(jsonPath("$.id").isNotEmpty());
  }

  @Test
  @DisplayName("NEGATIVE: Создаём уже существующего пользователя")
  public void givenEmployeeAlreadyExists_whenCreateEmployee_thenReturnConflict() throws Exception {

    EmployeeEntity employeeEntity = easyRandom.nextObject(EmployeeEntity.class);
    repository.save(employeeEntity);

    EmployeeRequestDto request = EmployeeRequestDto.builder()
        .firstName(employeeEntity.getFirstName())
        .lastName(employeeEntity.getLastName())
        .position(employeeEntity.getPosition())
        .build();

    mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(
            jsonPath("$.errorMessage")
                .value("Employee with those credentials already exists"));
  }

  @Test
  @DisplayName("POSITIVE: Получаем данные сотрудника по id")
  public void givenEmployeeExists_whenGetEmployeeById_thenReturnEmployee() throws Exception {

    EmployeeEntity employeeEntity = repository.save(easyRandom.nextObject(EmployeeEntity.class));

    mockMvc.perform(get(BASE_URL + "/" + employeeEntity.getId()))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.firstName").value(employeeEntity.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(employeeEntity.getLastName()))
        .andExpect(jsonPath("$.position").value(employeeEntity.getPosition()))
        .andExpect(jsonPath("$.id").value(employeeEntity.getId().toString()));
  }

  @Test
  @DisplayName("NEGATIVE: Получаем данные сотрудника по несуществующему id")
  public void givenIdDoesntExist_whenGetEmployeeById_thenReturnNotFound() throws Exception {

    UUID id = UUID.randomUUID();

    mockMvc.perform(get(BASE_URL + "/" + id))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage")
            .value("Employee with id " + id + " is not found"));
  }

  @Test
  @DisplayName("POSITIVE: Получаем список сотрудников")
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  public void givenEmployeesExist_whenGetAllEmployees_thenReturnAllEmployees() throws Exception {

    List<EmployeeEntity> employeeEntities = easyRandom.objects(EmployeeEntity.class, 3).collect(
        Collectors.toList());

    List<EmployeeEntity> savedEmployees = repository.saveAll(employeeEntities);

    mockMvc.perform(get(BASE_URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[*].id").value(containsInAnyOrder(
            savedEmployees.stream().map(e -> e.getId().toString()).toArray())))
        .andExpect(jsonPath("$[*].lastName").value(containsInAnyOrder(
            savedEmployees.stream().map(EmployeeEntity::getLastName).toArray())))
        .andExpect(jsonPath("$[*].firstName").value(containsInAnyOrder(
            savedEmployees.stream().map(EmployeeEntity::getFirstName).toArray())))
        .andExpect(jsonPath("$[*].position").value(containsInAnyOrder(
            savedEmployees.stream().map(EmployeeEntity::getPosition).toArray())));
  }

  @Test
  @DisplayName("POSITIVE: Получаем пустой список сотрудников")
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  public void givenEmployeesDontExist_whenGetAllEmployees_thenReturnEmptyList() throws Exception {

    mockMvc.perform(get(BASE_URL))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName("POSITIVE: Обновить данные сотрудника")
  public void givenEmployeeExist_whenUpdateEmployee_thenUpdateAndReturnEmployee() throws Exception {

    EmployeeEntity employeeEntity = repository.save(easyRandom.nextObject(EmployeeEntity.class));

    EmployeeRequestDto request = easyRandom.nextObject(EmployeeRequestDto.class);

    mockMvc.perform(patch(BASE_URL + "/" + employeeEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.position").value(request.getPosition()))
        .andExpect(jsonPath("$.lastName").value(request.getLastName()))
        .andExpect(jsonPath("$.firstName").value(request.getFirstName()));
  }

  @Test
  @DisplayName("NEGATIVE: Обновляемый сотрудник не существует")
  public void givenEmployeeDoesntExist_whenUpdateEmployee_thenReturnNotFound() throws Exception {
    UUID id = UUID.randomUUID();

    EmployeeRequestDto requestDto = easyRandom.nextObject(EmployeeRequestDto.class);

    mockMvc.perform(patch(BASE_URL + "/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(requestDto)))
        .andExpect(status().isNotFound());
  }

}
