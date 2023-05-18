package mk.genius.testspringboot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import mk.genius.testspringboot.entity.Employee;
import mk.genius.testspringboot.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @Autowired
    ObjectMapper objectMapper;
    Employee employee;

    @BeforeEach
    public void init() {

        employee = Employee.builder()
                .firstName("Mahesh")
                .lastName("Kumar")
                .email("mk@gmail.com")
                .build();

    }

    @Test
    public void givenEmployee_whenCreateEmployee_thenReturnEmployeeCreated() throws Exception {
        //given precondition or setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

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
    public void givenEmployeeList_whenFindAll_thenReturnEmployees() throws Exception {
        //given precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Ramesh")
                .lastName("sharma")
                .email("rk@gmail.com")
                .build();
        given(employeeService.findAllEmployees()).willReturn(List.of(employee, employee1));

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc.perform(get("/api/employees"));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));

    }

    @Test
    public void givenEmpId_whenGetByEmpId_thenReturnEmployeeObject() throws Exception {
        //given precondition or setup
        Long id = 1L;
        given(employeeService.getEmployeeById(id)).willReturn(Optional.of(employee));

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc
                .perform(get("/api/employees/{id}", id));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));


    }

    @Test
    public void givenInValidEmpId_whenGetByEmpId_thenReturnNotFound() throws Exception {
        //given precondition or setup
        Long id = 1L;
        given(employeeService.getEmployeeById(id)).willReturn(Optional.empty());

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc
                .perform(get("/api/employees/{id}", id));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isNotFound());


    }


    @Test
    public void givenEmployeeId_whenUpdateEmployee_thenEmployeeUpdated() throws Exception {
        //given precondition or setup
        Long empId = 1L;

        Employee toBeUpdated = Employee.builder()
                .firstName("Ramesh")
                .lastName("sharma")
                .email("rk@gmail.com")
                .build();

        given(employeeService.getEmployeeById(empId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

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


    @Test
    public void givenEmployeeId_whenUpdateEmployee_thenThrowError() throws Exception {
        //given precondition or setup
        Long empId = 1L;

        Employee toBeUpdated = Employee.builder()
                .firstName("Ramesh")
                .lastName("sharma")
                .email("rk@gmail.com")
                .build();

        given(employeeService.getEmployeeById(empId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc.perform(put("/api/employees/{id}", empId)
                .content(objectMapper.writeValueAsString(toBeUpdated))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenEmployeeDeleted() throws Exception {
        //given precondition or setup
        Long empId = 1L;

        Mockito.doNothing().when(employeeService).deleteEmployee(empId);

        //when - action or behaviour that we are testing
        ResultActions result = mockMvc.perform(delete("/api/employees/{id}", empId));

        //then -verify the output
        result.andDo(print())
                .andExpect(status().isOk());
    }

}
