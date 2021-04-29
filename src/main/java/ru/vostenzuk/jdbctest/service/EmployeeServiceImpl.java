package ru.vostenzuk.jdbctest.service;

import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository repository;

  public EmployeeServiceImpl(EmployeeRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<EmployeeEntity> getAllEmployees() {
    return repository.findAll();
  }

  @Override
  public EmployeeEntity getEmployee(UUID id) {
    return repository.findById(id).orElseThrow(() ->
        new EntityNotFoundException(String.format("Employee with id %s is not found", id)));

  }

  @Override
  public EmployeeEntity createEmployee(EmployeeEntity employeeEntity) {

    if (repository.existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(
        employeeEntity.getLastName(),
        employeeEntity.getFirstName(),
        employeeEntity.getPosition()
    )) {
      throw new IllegalArgumentException("User with those credentials already exists");
    } else {
      return repository.save(employeeEntity);
    }
  }

  @Override
  public EmployeeEntity updateEmployee(UUID employeeId, EmployeeEntity request) {
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

    return repository.save(employee);
  }

  @Override
  public void persist(EmployeeEntity employeeEntity) throws ConstraintViolationException {
    repository.save(employeeEntity);
  }

  @Override
  public Optional<EmployeeEntity> findByItemId(UUID itemId) {
    return repository.findEmployeeEntityByItemId(itemId.toString());
  }

  private <T> boolean fieldNeedsUpdating(T value) {
    return !Objects.isNull(value) && !Objects.equals(value, "string");
  }
}
