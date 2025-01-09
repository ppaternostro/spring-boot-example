package com.pasquasoft.example.employee;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.pasquasoft.example.exception.PatchConversionException;
import com.pasquasoft.example.model.Employee;
import com.pasquasoft.example.service.BaseService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

/**
 * The employee service.
 * 
 * @author ppaternostro
 *
 */
@Service
public class EmployeeService extends BaseService
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

  /**
   * Updates an employee matching the specified id.
   * 
   * @param id the id
   */
  public Employee updateEmployee(Long id, Employee updated)
  {
    Employee original = getEmployee(id);

    return applyUpdatesAndSave(original, updated);
  }

  /**
   * Partially updates an employee matching the specified id.
   * 
   * @param patch JsonPatch object
   * @param id the id
   */
  public Employee patch(JsonPatch patch, Long id)
  {
    Employee original = getEmployee(id);
    Employee patched;

    try
    {
      patched = applyPatch(patch, original);
    }
    catch (JsonProcessingException | JsonPatchException e)
    {
      LOG.error("Patch conversion processing error: Employee", e);
      throw new PatchConversionException(e.getMessage());
    }

    return applyUpdatesAndSave(original, patched);
  }

  private Employee applyUpdatesAndSave(Employee original, Employee updated)
  {
    original.setFirstName(updated.getFirstName());
    original.setMiddleName(updated.getMiddleName());
    original.setLastName(updated.getLastName());
    original.setSsn(updated.getSsn());
    original.getAddresses().clear();
    updated.getAddresses().forEach(address -> original.addAddress(address));

    return employeeRepository.save(original);
  }
}
