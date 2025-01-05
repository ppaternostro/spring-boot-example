package com.pasquasoft.example.employee;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pasquasoft.example.model.Employee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

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
    return employeeRepository.findAll();
  }

  /**
   * Retrieves an employee matching the specified id.
   * 
   * @param id the id
   * @return an employee matching the specified id
   */
  public Employee getEmployee(Long id)
  {
    return employeeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  /**
   * Saves the specified employee object.
   * 
   * @param employee employee object to be saved
   * @return the saved employee object
   */
  public Employee save(@Valid Employee employee)
  {
    return employeeRepository.save(employee);
  }

  /**
   * Deletes an employee matching the specified id.
   * 
   * @param id the id
   */
  public void deleteEmployee(Long id)
  {
    employeeRepository.deleteById(id);
  }
}
