package ru.vostenzuk.jdbctest.service;

import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.CreateEmployeeRequest;
import ru.vostenzuk.jdbctest.dto.ExpenseDto;
import ru.vostenzuk.jdbctest.dto.UpdateEmployeeRequest;
import ru.vostenzuk.jdbctest.mapper.EmployeeMapper;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final ItemRepository itemRepository;

    public EmployeeService(EmployeeRepository repository, ItemRepository itemRepository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.itemRepository = itemRepository;
    }

    public List<EmployeeEntity> getAllEmployees() {
        return repository.findAll();
    }

    public EmployeeEntity getEmployee(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Employee with id %s is not found", id)));

    }

    public EmployeeEntity createEmployee(CreateEmployeeRequest request) {
        EmployeeEntity employee = mapper.fromRequest(request);
        if (repository.existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(
                employee.getLastName(),
                employee.getFirstName(),
                employee.getPosition()
        )) {
            throw new IllegalArgumentException("User with those credentials already exists");
        } else {
            return repository.save(employee);
        }
    }

    public ExpenseDto getExpenses(UUID employeeId) {
        EmployeeEntity employee = repository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Employee with id %s is not found", employeeId)));
        return new ExpenseDto(employee.countExpenses());
    }

    public EmployeeEntity updateEmployee(UUID employeeId, UpdateEmployeeRequest request) {
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
        if (fieldNeedsUpdating(request.getAddItems())) {
            request.getAddItems().forEach(id -> employee.getItems().add(itemRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(String.format("Item with id %s is not found", id)))));
        }
        if (fieldNeedsUpdating(request.getRemoveItems())) {
            request.getRemoveItems().forEach(id -> employee.getItems().remove(itemRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(String.format("Item with id %s is not found", id)))));
        }
        try {
            return repository.save(employee);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Can't save items %s, they belong to another employee", request.getAddItems()));
        }
    }

    private <T> boolean fieldNeedsUpdating(T value) {
        return !Objects.isNull(value) && !Objects.equals(value, "string");
    }
}
