package com.pasquasoft.example.employee;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonpatch.JsonPatch;
import com.pasquasoft.example.model.Employee;

import jakarta.validation.Valid;

/**
 * The employee controller.
 * 
 * @author ppaternostro
 *
 */
@RestController
@Validated
@CrossOrigin
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
  @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
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
  @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public Employee getEmployee(@PathVariable Long id)
  {
    return employeeService.getEmployee(id);
  }

  /**
   * Updates an employee matching the specified id.
   * 
   * @param id the id
   * @return an updated employee matching the specified id
   */
  @PutMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public Employee updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee)
  {
    return employeeService.updateEmployee(id, employee);
  }

  /**
   * Retrieves an employee matching the specified id.
   * 
   * @param id the id
   * @return an employee matching the specified id
   */
  @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public Employee createEmployee(@Valid @RequestBody Employee employee)
  {
    return employeeService.save(employee);
  }

  /**
   * Deletes an employee matching the specified id.
   * 
   * @param id the id
   */
  @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  public void deleteEmployee(@PathVariable Long id)
  {
    employeeService.deleteEmployee(id);
  }

  /**
   * Partially updates an employee matching the specified id.
   * 
   * @param id the id
   * @return an updated employee matching the specified id
   */
  @PatchMapping(path = "/{id}", consumes = "application/json-patch+json", produces = MediaType.APPLICATION_JSON_VALUE)
  public Employee patchEmployee(@RequestBody JsonPatch patch, @PathVariable Long id)
  {
    return employeeService.patch(patch, id);
  }
}
