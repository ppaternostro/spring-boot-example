package com.pasquasoft.example.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pasquasoft.example.model.Employee;

/**
 * The employee repository.
 * 
 * @author ppaternostro
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{

}
