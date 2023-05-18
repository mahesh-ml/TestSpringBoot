package mk.genius.testspringboot.service;

import mk.genius.testspringboot.entity.Employee;
import mk.genius.testspringboot.repo.EmployeeRepository;
import mk.genius.testspringboot.service.error.ResourceNotFoundException;
import mk.genius.testspringboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository mockEmployeeRepo;

    @InjectMocks
    EmployeeServiceImpl classUnderTest;

    Employee employee;
    Employee employee1;

    @BeforeEach
    public void init() {

        employee = Employee.builder()
                .firstName("Mahesh")
                .lastName("Kumar")
                .email("mk@gmail.com")
                .build();


        employee1 = Employee.builder()
                .firstName("Ramesh")
                .lastName("sharma")
                .email("rk@gmail.com")
                .build();

    }

    @Test
    public void givenEmployee_whenSaveEmployee_thenEmployeeSaved() {

        //given precondition or setup
        when(mockEmployeeRepo.findEmployeeByEmail(employee.getEmail())).thenReturn(Optional.empty());
        when(mockEmployeeRepo.save(employee)).thenReturn(employee);

        //when - action or behaviour that we are testing
        Employee savedEmployee = classUnderTest.saveEmployee(employee);

        //then -verify the output
        assertThat(savedEmployee).isNotNull();

    }


    @Test
    public void givenEmployeePresent_whenSaveEmployee_thenError() {

        //given precondition or setup
        when(mockEmployeeRepo.findEmployeeByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        //when - action or behaviour that we are testing
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> classUnderTest.saveEmployee(employee));

        //then -verify the output
        Mockito.verify(mockEmployeeRepo, never()).save(any(Employee.class));

    }

    @Test
    public void givenEmployeeList_whenGetAll_thenListAllEmployees() {
        //given precondition or setup
        given(mockEmployeeRepo.findAll()).willReturn(List.of(employee, employee1));

        //when - action or behaviour that we are testing
        List<Employee> employees = classUnderTest.findAllEmployees();

        //then -verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);

    }

    @Test
    public void givenEmployeeList_whenGetAll_thenReturnEmptyList() {
        //given precondition or setup
        given(mockEmployeeRepo.findAll()).willReturn(Collections.emptyList());

        //when - action or behaviour that we are testing
        List<Employee> employees = classUnderTest.findAllEmployees();

        //then -verify the output
        assertThat(employees).isEmpty();
        assertThat(employees.size()).isEqualTo(0);

    }


    @Test
    public void givenEmployeeId_whenFindById_thenReturnEmployee() {
        //given precondition or setup
        given(mockEmployeeRepo.findById(1L)).willReturn(Optional.of(employee));

        //when - action or behaviour that we are testing
        Optional<Employee> employee = classUnderTest.getEmployeeById(1L);

        //then -verify the output
        assertThat(employee.get()).isNotNull();

    }

    @Test
    public void givenEmployee_whenUpdateEmployee_thenEmployeeUpdated() {

        //given precondition or setup
        given(mockEmployeeRepo.save(employee)).willReturn(employee);
        String updatedFirstName = "updated first name";
        String mail = "updated@gmail.com";
        employee.setFirstName(updatedFirstName);
        employee.setEmail(mail);
        //when - action or behaviour that we are testing
        Employee updatedEmployee = classUnderTest.updateEmployee(employee);

        //then -verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo(updatedFirstName);
        assertThat(updatedEmployee.getEmail()).isEqualTo(mail);
    }


    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenEmployeeDeleted() {

        //given precondition or setup
        willDoNothing().given(mockEmployeeRepo).deleteById(employee.getId());

        //when - action or behaviour that we are testing
        classUnderTest.deleteEmployee(employee.getId());

        //then -verify the output
        verify(mockEmployeeRepo, times(1)).deleteById(employee.getId());
    }

}
