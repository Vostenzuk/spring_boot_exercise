package ru.vostenzuk.jdbctest.controllers;

import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.mapper.EmployeeMapper;
import ru.vostenzuk.jdbctest.service.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
public class EmployeeController implements EmployeeOperations {

  private final EmployeeService employeeService;
  private final EmployeeMapper mapper;

  public EmployeeController(EmployeeService employeeService,
      EmployeeMapper mapper) {
    this.employeeService = employeeService;
    this.mapper = mapper;
  }

  @Override
  public List<EmployeeDto> getEmployees() {
    return employeeService.getAllEmployees().stream().map(mapper::fromEntity).collect(
        Collectors.toList());
  }

  @Override
  public EmployeeDto getEmployee(@PathVariable("id") UUID id) {
    return mapper.fromEntity(employeeService.getEmployee(id));
  }

  @Override
  public EmployeeDto createEmployee(@RequestBody EmployeeRequestDto employeeRequest) {
    return mapper.fromEntity(employeeService.createEmployee(mapper.fromRequest(employeeRequest)));
  }

  @Override
  public EmployeeDto updateEmployee(@PathVariable("id") UUID employeeId,
      @RequestBody EmployeeRequestDto employeeRequest) {
    return mapper.fromEntity(
        employeeService.updateEmployee(employeeId, mapper.fromRequest(employeeRequest)));
  }
}
