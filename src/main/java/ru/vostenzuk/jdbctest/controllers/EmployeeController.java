package ru.vostenzuk.jdbctest.controllers;

import org.springframework.web.bind.annotation.*;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeDto;
import ru.vostenzuk.jdbctest.dto.employee.EmployeeRequestDto;
import ru.vostenzuk.jdbctest.service.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
public class EmployeeController implements EmployeeOperations {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @Override
  public List<EmployeeDto> getEmployees() {
    return employeeService.getAllEmployees();
  }

  @Override
  public EmployeeDto getEmployee(@PathVariable("id") UUID id) {
    return employeeService.getEmployee(id);
  }

  @Override
  public EmployeeDto createEmployee(@RequestBody EmployeeRequestDto employeeRequest) {
    return employeeService.createEmployee(employeeRequest);
  }

  @Override
  public EmployeeDto updateEmployee(@PathVariable("id") UUID employeeId,
      @RequestBody EmployeeRequestDto employeeRequest) {
    return employeeService.updateEmployee(employeeId, employeeRequest);
  }
}
