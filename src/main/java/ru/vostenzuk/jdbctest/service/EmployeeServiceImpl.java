package ru.vostenzuk.jdbctest.service;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.mapper.EmployeeMapper;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository repository;
  private final EmployeeMapper mapper;

  public EmployeeServiceImpl(EmployeeRepository repository,
      EmployeeMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<EmployeeDto> getAllEmployees() {
    return repository.findAll().stream().map(mapper::fromEntity).collect(Collectors.toList());
  }

  @Override
  public EmployeeDto getEmployee(UUID id) {
    return repository.findById(id).map(mapper::fromEntity).orElseThrow(() ->
        new EntityNotFoundException(String.format("Employee with id %s is not found", id)));

  }

  @Override
  public EmployeeDto createEmployee(EmployeeRequestDto request) {

    if (repository.existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(
        request.getLastName(),
        request.getFirstName(),
        request.getPosition()
    )) {
      throw new IllegalArgumentException("Employee with those credentials already exists");
    } else {
      return mapper.fromEntity(repository.save(mapper.toEntity(request)));
    }
  }

  @Override
  public EmployeeDto updateEmployee(UUID employeeId, EmployeeRequestDto request) {
    EmployeeEntity employee = repository.findById(employeeId).orElseThrow(() ->
        new EntityNotFoundException(String.format("Employee with id %s is not found", employeeId)));

    if (fieldNeedsUpdating(request.getFirstName())) {
      employee.setFirstName(request.getFirstName());
    }
    if (fieldNeedsUpdating(request.getLastName())) {
      employee.setLastName(request.getLastName());
    }
    if (fieldNeedsUpdating(request.getPosition())) {
      employee.setPosition(request.getPosition());
    }

    return mapper.fromEntity(repository.save(employee));
  }

  @Override
  public void persist(EmployeeDto employeeDto) throws ConstraintViolationException {
    repository.save(mapper.toEntity(employeeDto));
  }

  @Override
  public Optional<EmployeeDto> findByItemId(UUID itemId) {
    return repository.findEmployeeEntityByItemId(itemId.toString()).map(mapper::fromEntity);
  }

  private <T> boolean fieldNeedsUpdating(T value) {
    return !Objects.isNull(value) && !Objects.equals(value, "string");
  }
}
