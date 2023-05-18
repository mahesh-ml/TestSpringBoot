package mk.genius.testspringboot.service.impl;

import mk.genius.testspringboot.entity.Employee;
import mk.genius.testspringboot.repo.EmployeeRepository;
import mk.genius.testspringboot.service.EmployeeService;
import mk.genius.testspringboot.service.error.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> emp = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if (emp.isPresent()) {
            throw new ResourceNotFoundException(" the employee already exists with email ", employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employeeUpdated) {
        return employeeRepository.save(employeeUpdated);
    }

    @Override
    public void deleteEmployee(Long empId) {
        employeeRepository.deleteById(empId);
    }
}
