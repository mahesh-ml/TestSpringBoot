package mk.genius.testspringboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import mk.genius.testspringboot.entity.Employee;
import mk.genius.testspringboot.repo.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class TCIntegrationEmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ObjectMapper objectMapper;
    Employee employee;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();

        employee = Employee.builder()
                .firstName("Mahesh")
                .lastName("Kumar")
                .email("mk@gmail.com")
                .build();
    }

    @Test
    @DisplayName("Integration test for create employee operation")
    public void givenEmployee_whenCreateEmployee_thenReturnEmployeeCreated() throws Exception {

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc.perform(post("/api/employees")
                .content(objectMapper.writeValueAsString(employee))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    @Test
    @DisplayName("Integration test for find All operation")
    public void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {

        Employee employee1 = Employee.builder()
                .firstName("Ramesh")
                .lastName("sharma")
                .email("rk@gmail.com")
                .build();


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
    @DisplayName("Integration test for find By Id  operation")
    public void givenEmpId_whenGetByEmpId_thenReturnEmployeeObject() throws Exception {
        //given precondition or setup
        Long id = 1L;
        employeeRepository.save(employee);
        //when - action or behaviour that we are testing
        ResultActions result = mockMvc
                .perform(get("/api/employees/{id}", employee.getId()));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));


    }

    @Test
    @DisplayName("Integration test for find By Id Error  operation")
    public void givenInValidEmpId_whenGetByEmpId_thenReturnNotFound() throws Exception {
        //given precondition or setup
        Long id = 1L;
        employeeRepository.save(employee);

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc
                .perform(get("/api/employees/{id}", 23L));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isNotFound());


    }

    @Test
    @DisplayName("Integration test for update employee  operation")
    public void givenEmployeeId_whenUpdateEmployee_thenEmployeeUpdated() throws Exception {
        //given precondition or setup
        Long empId = 1L;

        Employee toBeUpdated = Employee.builder()
                .firstName("Ramesh")
                .lastName("sharma")
                .email("rk@gmail.com")
                .build();

        employeeRepository.save(employee);

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc.perform(put("/api/employees/{id}", empId)
                .content(objectMapper.writeValueAsString(toBeUpdated))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(toBeUpdated.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(toBeUpdated.getLastName())))
                .andExpect(jsonPath("$.email", is(toBeUpdated.getEmail())));
    }

    @DisplayName("Integration test for update employee  failed")
    @Test
    public void givenEmployeeId_whenUpdateEmployee_thenThrowError() throws Exception {
        //given precondition or setup
        Long empId = 1L;

        Employee toBeUpdated = Employee.builder()
                .firstName("Ramesh")
                .lastName("sharma")
                .email("rk@gmail.com")
                .build();

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc.perform(put("/api/employees/{id}", empId)
                .content(objectMapper.writeValueAsString(toBeUpdated))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Integration test for delete employee ")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenEmployeeDeleted() throws Exception {
        //given precondition or setup
        Long empId = 1L;

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc.perform(delete("/api/employees/{id}", empId));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isOk());
    }


}
