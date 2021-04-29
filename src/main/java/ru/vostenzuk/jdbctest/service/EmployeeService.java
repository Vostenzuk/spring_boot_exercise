package ru.vostenzuk.jdbctest.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;

public interface EmployeeService {

  List<EmployeeEntity> getAllEmployees();

  EmployeeEntity getEmployee(UUID id);

  EmployeeEntity createEmployee(EmployeeEntity employeeEntity);

  EmployeeEntity updateEmployee(UUID employeeId, EmployeeEntity employeeEntity);

  void persist(EmployeeEntity employeeEntity) throws ConstraintViolationException;

  Optional<EmployeeEntity> findByItemId(UUID itemId);
}
