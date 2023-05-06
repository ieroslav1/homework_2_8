package pro.sky.homework_2_8.service;

import org.springframework.stereotype.Service;
import pro.sky.homework_2_8.exception.EmployeeAlreadyAddedException;
import pro.sky.homework_2_8.exception.EmployeeNotFoundException;
import pro.sky.homework_2_8.exception.EmployeeStorageIsFullException;
import pro.sky.homework_2_8.model.Employee;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {

    private final Map<String, Employee> employees;
    private static final int MAX_EMPLOYEES = 4;

    public EmployeeService() {
        this.employees = new HashMap<>();
    }

    public Employee add(String firstName, String lastName, int department, int salary) {
        if (employees.size() >= MAX_EMPLOYEES) {
            throw new EmployeeStorageIsFullException();
        }
        Employee employee = new Employee(firstName, lastName, department, salary);
        return employees.merge(employee.getFullName(), employee, (oldEmployee, newEmployee) -> {
            throw new EmployeeAlreadyAddedException();
        });
    }


    public Employee remove(String firstName, String lastName, int department, int salary) {
        Employee employee = new Employee(firstName, lastName, department, salary);
        return employees.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(employee))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(employees::remove)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public Employee find(String firstName, String lastName, int department, int salary) {
        Employee employee = new Employee(firstName, lastName, department, salary);
        return employees.values()
                .stream()
                .filter(v -> v.equals(employee))
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public Collection<Employee> findAll() {
        return Collections.unmodifiableCollection(employees.values());
    }
}
