package mk.genius.testspringboot.controller;

import mk.genius.testspringboot.entity.Employee;
import mk.genius.testspringboot.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> findAllEmployees() {
        return employeeService.findAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable("id") Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        return employeeService.getEmployeeById(id)
                .map(emp -> {
                    emp.setEmail(employee.getEmail());
                    emp.setLastName(employee.getLastName());
                    emp.setFirstName(employee.getFirstName());
                    return ResponseEntity.ok(employeeService.updateEmployee(employee));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.getEmployeeById(id);
        return ResponseEntity.ok("Employee deleted successfully " + id);
    }
}
