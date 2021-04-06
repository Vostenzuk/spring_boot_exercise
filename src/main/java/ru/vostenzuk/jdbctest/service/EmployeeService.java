package ru.vostenzuk.jdbctest.service;

import org.springframework.stereotype.Service;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.domain.ItemEntity;
import ru.vostenzuk.jdbctest.dto.CreateEmployeeRequest;
import ru.vostenzuk.jdbctest.dto.ExpenseDto;
import ru.vostenzuk.jdbctest.mapper.EmployeeMapper;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;
import ru.vostenzuk.jdbctest.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final ItemRepository itemRepository;

    public EmployeeService(EmployeeRepository repository, ItemRepository itemRepository, EmployeeMapper mapper, ItemRepository itemRepository1) {
        this.repository = repository;
        this.mapper = mapper;
        this.itemRepository = itemRepository1;
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

    public EmployeeEntity addItem(UUID employeeId, UUID itemId) {
        EmployeeEntity employee = repository.findById(employeeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Employee with id %s is not found", employeeId)));
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Item with id %s is not found", itemId)));

        employee.getItems().add(item);
        try {
            return repository.save(employee);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Item with id %s belongs to another employee", item.getId()));
        }
    }

    public EmployeeEntity removeItem(UUID employeeId, UUID itemId) {
        EmployeeEntity employee = repository.findById(employeeId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Employee with id %s is not found", employeeId)));
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Item with id %s is not found", itemId)));
        employee.getItems().remove(item);
        return repository.save(employee);
    }
}
