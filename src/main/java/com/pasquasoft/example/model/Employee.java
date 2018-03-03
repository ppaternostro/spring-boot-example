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
  private Long id;
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
   * @param id the id
   * @param lastName the last name
   */
  public Employee(Long id, String lastName)
  {
    setId(id);
    setLastName(lastName);
  }

  /**
   * Retrieves the id.
   * 
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * Sets the id.
   * 
   * @param id the id
   */
  public void setId(Long id)
  {
    this.id = id;
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
   * @param ssn the ssn
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
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
    return "Employee [id=" + id + ", ssn=" + ssn + ", "
        + super.toString() + "]";
  }

}
