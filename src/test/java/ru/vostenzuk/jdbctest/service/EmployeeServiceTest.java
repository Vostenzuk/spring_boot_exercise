package ru.vostenzuk.jdbctest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.vostenzuk.jdbctest.domain.EmployeeEntity;
import ru.vostenzuk.jdbctest.dto.CreateEmployeeRequest;
import ru.vostenzuk.jdbctest.mapper.EmployeeMapperImpl;
import ru.vostenzuk.jdbctest.repository.EmployeeRepository;
import ru.vostenzuk.jdbctest.repository.ItemRepository;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    private final EmployeeRepository employeeRepository = Mockito.mock(EmployeeRepository.class);
    private final ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private final EmployeeMapperImpl mapper = new EmployeeMapperImpl();
    private EmployeeService employeeService;

    private final static String FIRST_NAME = "Имя";
    private final static String LAST_NAME = "Фамилия";
    private final static String POSITION = "Разработчик";

    @BeforeEach
    void init() {
        employeeService = new EmployeeService(employeeRepository, itemRepository, mapper);
    }

    @Test
    void createdEmployeeHasFirstNameLastNamePositionAndEmptyItemList() {

        CreateEmployeeRequest request = new CreateEmployeeRequest.Builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .position(POSITION)
                .build();

        when(employeeRepository.existsEmployeeEntitiesByLastNameAndFirstNameAndPosition(LAST_NAME, FIRST_NAME, POSITION)).thenReturn(false);
        when(employeeRepository.save(any(EmployeeEntity.class))).then(returnsFirstArg());

        EmployeeEntity employee = employeeService.createEmployee(request);
        assertThat("first name is not same as in request", employee.getFirstName().equals(FIRST_NAME));
        assertThat("last name is not the same as in request", employee.getLastName().equals(LAST_NAME));
        assertThat("position is not the same as in request", employee.getPosition().equals(POSITION));
        assertThat("item list is not empty", employee.getItems().isEmpty());
    }
}