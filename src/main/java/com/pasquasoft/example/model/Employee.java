package com.pasquasoft.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A class that defines the attributes and behavior of an employee.
 * 
 * @author ppaternostro
 *
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Employee extends Person
{
  @Id
  @GeneratedValue
  private Long employeeId;
  private String ssn;

  /**
   * Constructs an <code>Employee</code> object.
   */
  public Employee()
  {

  }

  /**
   * Constructs an employee object with the specified parameters.
   * 
   * @param lastName the last name
   * @param firstName the first name
   */
  public Employee(String lastName, String firstName)
  {
    setLastName(lastName);
    setFirstName(firstName);
  }

  /**
   * Retrieves the employee id.
   * 
   * @return the employee id
   */
  public Long getEmployeeId()
  {
    return employeeId;
  }

  /**
   * Sets the employee id.
   * 
   * @param employeeId the employee id
   */
  public void setId(Long employeeId)
  {
    this.employeeId = employeeId;
  }

  /**
   * Retrieves the SSN.
   * 
   * @return the SSN
   */
  public String getSsn()
  {
    return ssn;
  }

  /**
   * Sets the SSN.
   * 
   * @param ssn the SSN
   */
  public void setSsn(String ssn)
  {
    this.ssn = ssn;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + ((employeeId == null) ? 0 : employeeId.hashCode());
    result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Employee other = (Employee) obj;
    if (employeeId == null)
    {
      if (other.employeeId != null)
        return false;
    }
    else if (!employeeId.equals(other.employeeId))
      return false;
    if (ssn == null)
    {
      if (other.ssn != null)
        return false;
    }
    else if (!ssn.equals(other.ssn))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "Employee [employeeId=" + employeeId + ", ssn=" + ssn + ", "
        + super.toString() + "]";
  }

}
