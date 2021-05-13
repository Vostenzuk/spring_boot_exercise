package ru.vostenzuk.jdbctest.functionals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MvcResult;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.mapper.EmployeeMapper;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;

public class EmployeeOperationsFunctionalTest extends AbstractRestFunctionalTest {

  @Autowired
  private EmployeeMapper employeeMapper;

  @Autowired
  private EmployeeRepository repository;

  private static final String BASE_URL = "/employees";

  @Test
  @DisplayName("POSITIVE: Создаём нового пользователя")
  public void createEmployeeSuccessfulTest() throws Exception {
    EmployeeRequestDto request = easyRandom.nextObject(EmployeeRequestDto.class);

    MvcResult result = mockMvc.perform(post(BASE_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    EmployeeDto createdEmployee = mapper
        .readValue(result
                .getResponse()
                .getContentAsString(),
            EmployeeDto.class);

    assertThat(createdEmployee)
        .hasNoNullFieldsOrProperties();

    assertThat(createdEmployee)
        .hasFieldOrPropertyWithValue("firstName", request.getFirstName())
        .hasFieldOrPropertyWithValue("lastName", request.getLastName())
        .hasFieldOrPropertyWithValue("position", request.getPosition());

    Optional<EmployeeEntity> persistedUser = repository.findById(createdEmployee.getId());

    assertThat(persistedUser).isPresent();
    assertThat(persistedUser.get().getFirstName()).isEqualTo(request.getFirstName());
    assertThat(persistedUser.get().getLastName()).isEqualTo(request.getLastName());
    assertThat(persistedUser.get().getPosition()).isEqualTo(request.getPosition());
  }

  @Test
  @DisplayName("NEGATIVE: Создаём уже существующего пользователя")
  public void createExistingEmployeeFailure() throws Exception {

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
        .andExpect(content()
            .json("{\"errorMessage\": \"Employee with those credentials already exists\"}"));
  }

  @Test
  @DisplayName("POSITIVE: Получаем данные сотрудника по id")
  public void getEmployeeByIdSuccess() throws Exception {

    EmployeeEntity employeeEntity = repository.save(easyRandom.nextObject(EmployeeEntity.class));

    MvcResult result = mockMvc.perform(get(BASE_URL + "/" + employeeEntity.getId()))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    EmployeeDto foundEmployee = mapper
        .readValue(result.getResponse().getContentAsString(), EmployeeDto.class);

    assertThat(foundEmployee).isEqualTo(employeeMapper.fromEntity(employeeEntity));
  }

  @Test
  @DisplayName("NEGATIVE: Получаем данные сотрудника по несуществующему id")
  public void getEmployeeByIdFailure() throws Exception {

    UUID id = UUID.randomUUID();

    mockMvc.perform(get(BASE_URL + "/" + id))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(
            content().json("{\"errorMessage\": \"Employee with id " + id + " is not found\"}"));
  }

  @Test
  @DisplayName("POSITIVE: Получаем список сотрудников")
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  public void getEmployeesSuccess() throws Exception {

    List<EmployeeEntity> employeeEntities = easyRandom.objects(EmployeeEntity.class, 3).collect(
        Collectors.toList());

    repository.saveAll(employeeEntities);

    MvcResult result = mockMvc.perform(get(BASE_URL))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    List<EmployeeDto> foundEmployees = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<EmployeeDto>>() {
        });

    assertThat(foundEmployees).hasSize(3);
  }

  @Test
  @DisplayName("POSITIVE: Получаем пустой список сотрудников")
  @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
  public void getEmployeesEmptySuccess() throws Exception {

    MvcResult result = mockMvc.perform(get(BASE_URL))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    List<EmployeeDto> foundEmployees = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<EmployeeDto>>() {
        });
    assertThat(foundEmployees).hasSize(0);
  }

  @Test
  @DisplayName("POSITIVE: Обновить данные сотрудника")
  public void updateEmployeeDataSuccess() throws Exception {

    EmployeeEntity employeeEntity = repository.save(easyRandom.nextObject(EmployeeEntity.class));

    EmployeeRequestDto request = easyRandom.nextObject(EmployeeRequestDto.class);

    mockMvc.perform(patch(BASE_URL + "/" + employeeEntity.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn();

    Optional<EmployeeEntity> updatedEmployee = repository.findById(employeeEntity.getId());

    assertThat(updatedEmployee).isPresent();
    assertThat(updatedEmployee.get().getLastName()).isEqualTo(request.getLastName());
    assertThat(updatedEmployee.get().getFirstName()).isEqualTo(request.getFirstName());
    assertThat(updatedEmployee.get().getPosition()).isEqualTo(request.getPosition());
  }

}
