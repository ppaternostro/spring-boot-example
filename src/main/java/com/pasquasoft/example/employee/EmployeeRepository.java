package com.pasquasoft.example.employee;

import org.springframework.data.repository.CrudRepository;

import com.pasquasoft.example.model.Employee;

/**
 * The employee repository.
 * 
 * @author ppaternostro
 *
 */
public interface EmployeeRepository
    extends CrudRepository<Employee, Long>
{

}
