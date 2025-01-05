package com.pasquasoft.example.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * A class that defines the attributes and behavior of an employee.
 * 
 * @author ppaternostro
 *
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "employee")
public class Employee extends Person
{
  @Id
  @GeneratedValue
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;
  private String ssn;
  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  @JacksonXmlElementWrapper(localName = "addresses")
  @JacksonXmlProperty(localName = "address")
  @Valid
  private List<Address> addresses = new ArrayList<>();

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
   * @param ssn the SSN
   */
  public void setSsn(String ssn)
  {
    this.ssn = ssn;
  }

  /**
   * Retrieves the addresses.
   * 
   * @return the addresses
   */
  public List<Address> getAddresses()
  {
    return addresses;
  }

  /**
   * Sets the addresses.
   * 
   * @param addresses the addresses
   */
  public void setAddresses(List<Address> addresses)
  {
    this.addresses = addresses;
  }

  public void addAddress(Address address)
  {
    addresses.add(address);
    address.setEmployee(this);
  }

  public void removeAddress(Address address)
  {
    addresses.remove(address);
    address.setEmployee(null);
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
    return "Employee [id=" + id + ", ssn=" + ssn + ", " + super.toString() + "]";
  }

}
