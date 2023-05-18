package mk.genius.testspringboot.service;

import mk.genius.testspringboot.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> findAllEmployees();

    Optional<Employee> getEmployeeById(Long id);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Long empId);


}
