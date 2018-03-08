package com.pasquasoft.example.employee;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pasquasoft.example.model.Employee;

/**
 * The employee controller.
 * 
 * @author ppaternostro
 *
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController
{
  private EmployeeService employeeService;

  /*
   * Favor constructor injection over attribute or setter injection.
   */
  public EmployeeController(EmployeeService employeeService)
  {
    this.employeeService = employeeService;
  }

  /**
   * Retrieves employees.
   * 
   * @return a list of employees
   */
  @RequestMapping(method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public List<Employee> getEmployees()
  {
    return employeeService.getEmployees();
  }

  /**
   * Retrieves an employee matching the specified id.
   * 
   * @param id the id
   * @return an employee matching the specified id
   */
  @RequestMapping(method = RequestMethod.GET, path = "/{id}", produces = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public Employee getEmployee(@PathVariable("id") Long id)
  {
    return employeeService.getEmployee(id);
  }
}
