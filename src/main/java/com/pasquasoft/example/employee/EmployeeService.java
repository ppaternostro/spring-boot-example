package com.pasquasoft.example.employee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pasquasoft.example.model.Employee;

/**
 * The employee service.
 * 
 * @author ppaternostro
 *
 */
@Service
public class EmployeeService
{
  private EmployeeRepository employeeRepository;

  /*
   * Favor constructor injection over attribute or setter injection.
   */
  public EmployeeService(EmployeeRepository employeeRepository)
  {
    this.employeeRepository = employeeRepository;
  }

  /**
   * Retrieves employees.
   * 
   * @return a list of employees
   */
  public List<Employee> getEmployees()
  {
    ArrayList<Employee> employees = new ArrayList<>();

    employeeRepository.findAll().forEach(employees::add);

    return employees;
  }

  /**
   * Retrieves an employee matching the specified id.
   * 
   * @param id the id
   * @return an employee matching the specified id
   */
  public Employee getEmployee(Long id)
  {
    return employeeRepository.findById(id).get();
  }
}
