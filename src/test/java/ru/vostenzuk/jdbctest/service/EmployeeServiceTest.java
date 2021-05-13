package ru.vostenzuk.jdbctest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.dto.item.ItemDto;
import ru.vostenzuk.jdbctest.mapper.EmployeeMapper;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock
  private EmployeeRepository repository;
  @Mock
  private EmployeeMapper mapper;

  private EmployeeService service;

  private final List<EmployeeDto> employees = new ArrayList<>();
  private final List<EmployeeEntity> employeeEntities = new ArrayList<>();

  @BeforeEach
  void setUp() {
    service = new EmployeeServiceImpl(repository, mapper);
    for (int i = 0; i < 3; i++) {
      employees.add(EmployeeDto.builder()
          .id(UUID.randomUUID())
          .firstName("firstName" + i)
          .lastName("lastName" + i)
          .position("position" + i)
          .build());
    }

    for (int i = 0; i < 3; i++) {
      employeeEntities.add(EmployeeEntity.builder().id(employees.get(i).getId())
          .firstName(employees.get(i).getFirstName())
          .lastName(employees.get(i).getLastName())
          .position(employees.get(i).getPosition())
          .build());
    }
  }

  @Test
  @DisplayName("Получаем список всех сотрудников")
  void getAllEmployees() {
    when(repository.findAll()).thenReturn(employeeEntities);
    when(mapper.fromEntity(any(EmployeeEntity.class)))
        .thenReturn(employees.get(0), employees.get(1), employees.get(2));

    List<EmployeeDto> foundEmployees = service.getAllEmployees();

    assertThat(foundEmployees).hasSameElementsAs(employees);
  }

  @Test
  @DisplayName("Получаем данные о сотруднике")
  void getEmployee() {
    EmployeeDto employee = employees.get(0);

    when(repository.findById(employee.getId())).thenReturn(Optional.of(employeeEntities.get(0)));
    when(mapper.fromEntity(employeeEntities.get(0))).thenReturn(employee);

    EmployeeDto foundEmployee = service.getEmployee(employee.getId());

    assertThat(foundEmployee).isEqualTo(employee);
  }

  @Test
  @DisplayName("Получаем данные по несуществующему id")
  void getEmployeeByNonExistentId() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getEmployee(id))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Employee with id " + id + " is not found");
  }

  @Test
  @DisplayName("Создаём сотрудника")
  void createEmployee() {
    EmployeeEntity employeeEntity = employeeEntities.get(0);
    EmployeeRequestDto request = EmployeeRequestDto.builder()
        .firstName(employeeEntity.getFirstName()).lastName(employeeEntity.getLastName())
        .position(employeeEntity.getPosition()).build();

    when(mapper.toEntity(request)).thenReturn(employeeEntity);
    when(mapper.fromEntity(employeeEntity)).thenReturn(employees.get(0));
    when(repository.existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(
        request.getLastName(), request.getFirstName(), request.getPosition())).thenReturn(false);
    when(repository.save(employeeEntity)).thenReturn(employeeEntity);

    EmployeeDto createdEmployee = service.createEmployee(request);

    assertThat(createdEmployee).isEqualTo(employees.get(0));
  }

  @Test
  @DisplayName("Создаём уже существующего сотрудника")
  void createEmployeeThatAlreadyExists() {
    EmployeeEntity employeeEntity = employeeEntities.get(0);
    EmployeeRequestDto request = EmployeeRequestDto.builder()
        .firstName(employeeEntity.getFirstName())
        .lastName(employeeEntity.getLastName())
        .position(employeeEntity.getPosition())
        .build();

    when(repository.existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(request.getLastName(),
        request.getFirstName(), request.getPosition())).thenReturn(true);

    assertThatThrownBy(() -> service.createEmployee(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Employee with those credentials already exists");
  }

  @Test
  @DisplayName("Обновляем данные сотрудника")
  void updateEmployee() {
    EmployeeEntity employeeEntity = employeeEntities.get(0);
    UUID id = employeeEntity.getId();
    EmployeeRequestDto request = EmployeeRequestDto.builder()
        .lastName("Redacted")
        .build();

    when(repository.findById(id)).thenReturn(Optional.of(employeeEntity));
    when(repository.save(employeeEntity)).thenReturn(employeeEntity);

    EmployeeDto employeeDto = EmployeeDto.builder().id(employeeEntity.getId())
        .firstName(employeeEntity.getFirstName()).lastName(employeeEntity.getLastName())
        .position(employeeEntity.getPosition()).build();

    when(mapper.fromEntity(employeeEntity))
        .thenReturn(employeeDto);

    EmployeeDto updatedEmployee = service.updateEmployee(id, request);

    assertThat(updatedEmployee).isEqualTo(employeeDto);
  }

  @Test
  @DisplayName("Обновляем несуществующего сотрудника")
  void updateEmployeeWithNonExistentId() {
    EmployeeDto employee = employees.get(0);

    when(repository.findById(employee.getId())).thenReturn(Optional.empty());

    assertThatThrownBy(
        () -> service.updateEmployee(employee.getId(), EmployeeRequestDto.builder().build()))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Employee with id " + employee.getId() + " is not found");
  }

  @Test
  @DisplayName("Сохраняем данные сотрудника")
  void persist() {

    when(mapper.toEntity(employees.get(0))).thenReturn(employeeEntities.get(0));
    when(repository.save(employeeEntities.get(0))).thenThrow(ConstraintViolationException.class);

    assertThatThrownBy(() -> service.persist(employees.get(0)))
        .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  @DisplayName("Находим сотрудника по id предмета")
  void findByItemId() {
    EmployeeDto employee = employees.get(0);
    ItemDto item = ItemDto.builder().id(UUID.randomUUID()).type("type").price(BigDecimal.ZERO)
        .build();
    employee.setItems(Collections.singleton(item));

    when(repository.findEmployeeEntityByItemId(item.getId().toString()))
        .thenReturn(Optional.of(employeeEntities.get(0)));
    when(mapper.fromEntity(employeeEntities.get(0))).thenReturn(employees.get(0));

    Optional<EmployeeDto> foundEmployee = service.findByItemId(item.getId());

    assertThat(foundEmployee).isEqualTo(Optional.of(employee));
  }

}