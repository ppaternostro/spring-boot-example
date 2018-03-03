package com.pasquasoft.example.employee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pasquasoft.example.model.Employee;

/**
 * The employee service class.
 * 
 * @author ppaternostro
 *
 */
@Service
public class EmployeeService
{
  // private static final List<Employee> employees = new ArrayList<Employee>() {
  // /**
  // * Generated serial version UID.
  // */
  // private static final long serialVersionUID = 473436982328938528L;
  //
  // {
  // add(new Employee(1L, "Lennon"));
  // add(new Employee(2L, "McCartney"));
  // add(new Employee(3L, "Harrison"));
  // add(new Employee(4L, "Starr"));
  // }
  // };
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
   * Retrieves an employee with the specified parameter,
   * 
   * @param id the id
   * @return an employee with the specified parameter
   */
  public Employee getEmployee(Long id)
  {
    return employeeRepository.findById(id).get();
  }
}
