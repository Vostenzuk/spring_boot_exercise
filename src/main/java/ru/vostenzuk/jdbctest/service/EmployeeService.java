package ru.vostenzuk.jdbctest.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;

public interface EmployeeService {

  List<EmployeeDto> getAllEmployees();

  EmployeeDto getEmployee(UUID id);

  EmployeeDto createEmployee(EmployeeRequestDto employeeRequestDto);

  EmployeeDto updateEmployee(UUID employeeId, EmployeeRequestDto employeeRequestDto);

  void persist(EmployeeDto employeeDto) throws ConstraintViolationException;

  Optional<EmployeeDto> findByItemId(UUID itemId);
}
