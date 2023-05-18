package mk.genius.testspringboot.repo;

import mk.genius.testspringboot.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;
    Employee employee;

    Employee employee1;

    @BeforeEach
    public void init() {

        employeeRepository.deleteAll();
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
    @DisplayName("junit test for save operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        //given precondition or setup


        //when - action or behaviour that we are testing
        Employee savedEmployee = employeeRepository.save(employee);

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());

    }


    @Test
    @DisplayName("junit test for find All operation")
    public void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        //given precondition or setup
        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when - action or behaviour that we are testing
        List<Employee> empList = employeeRepository.findAll();

        //then -verify the output
        assertThat(empList).isNotNull();
        assertThat(empList.size()).isEqualTo(2);

    }


    @Test
    @DisplayName("junit test for find By Id operation")
    public void givenEmployeeId_whenFindById_thenReturnEmployee() {
        //given precondition or setup

        employeeRepository.save(employee);
        //when - action or behaviour that we are testing
        Employee emp = employeeRepository.findById(employee.getId()).get();

        //then -verify the output
        assertThat(emp).isNotNull();

    }


    @Test
    @DisplayName("junit test for get employee by Email operation")
    public void givenEmail_whenFindByEmail_thenEmployeeWithTheGivenEmail() {
        //given precondition or setup

        employeeRepository.save(employee);
        //when - action or behaviour that we are testing
        Employee emp = employeeRepository.findEmployeeByEmail(employee.getEmail()).get();
        //then -verify the output
        assertThat(emp).isNotNull();
        assertThat(emp.getEmail()).isEqualTo(employee.getEmail());
    }


    @Test
    @DisplayName("junit test for update employee operation")
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given precondition or setup

        employeeRepository.save(employee);


        //when - action or behaviour that we are testing
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("mk12828@gmail.com");
        savedEmployee.setFirstName("test updated");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);


        //then -verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("mk12828@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("test updated");

    }


    @Test
    @DisplayName("junit test for delete employee operation")
    public void givenEmployeeId_whenDelete_thenEmployeeDeleted() {
        //given precondition or setup

        employeeRepository.save(employee);

        //when - action or behaviour that we are testing
        employeeRepository.delete(employee);
        Optional<Employee> empl = employeeRepository.findById(employee.getId());

        //then -verify the output
        assertThat(empl).isEmpty();

    }

    @Test
    @DisplayName("junit test for custom query FindByJPQL with index operation")
    public void givenFirstNameLastName_whenFindByJPQL_thenReturnEmployee() {
        //given precondition or setup

        employeeRepository.save(employee);
        //when - action or behaviour that we are testing
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameJPQL(employee.getFirstName(), employee.getLastName());

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }


    @Test
    @DisplayName("junit test for custom query FindByJPQL with NamedParam operation")
    public void givenFirstNameLastName_whenFindByJPQLNamedParam_thenReturnEmployee() {
        //given precondition or setup

        employeeRepository.save(employee);
        //when - action or behaviour that we are testing
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameJPQLWithNamedParams(employee.getFirstName(), employee.getLastName());

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    @DisplayName("junit test for custom query FindBy with NativeQuery operation")
    public void givenFirstNameLastName_whenFindByJPQLNativeQuery_thenReturnEmployee() {
        //given precondition or setup
        employeeRepository.save(employee);
        //when - action or behaviour that we are testing
        Employee savedEmployee = employeeRepository.findByFirstNameAndLastNameJPQLWithNativeQuery(employee.getFirstName(), employee.getLastName());

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

}
