package com.pasquasoft.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A class that defines the attributes and behavior of an address.
 * 
 * @author ppaternostro
 *
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Address
{
  @Id
  @GeneratedValue
  private Long id;
  private String street;
  private String secondStreet;
  private String city;
  private String state;
  private String zip;
  @ManyToOne
  @JoinColumn(name = "employee_id")
  @JsonIgnore
  private Employee employee;

  /**
   * Constructs an <code>Address</code> object.
   */
  public Address()
  {

  }

  /**
   * Constructs an <code>Address</code> object with the specified parameters.
   * 
   * @param street the street
   * @param city the city
   * @param state the state
   */
  public Address(String street, String city, String state)
  {
    setStreet(street);
    setCity(city);
    setState(state);
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
   * Retrieves the street.
   * 
   * @return the street
   */
  public String getStreet()
  {
    return street;
  }

  /**
   * Sets the street.
   * 
   * @param street the street
   */
  public void setStreet(String street)
  {
    this.street = street;
  }

  /**
   * Retrieves the second street.
   * 
   * @return the second street
   */
  public String getSecondStreet()
  {
    return secondStreet;
  }

  /**
   * Sets the second street.
   * 
   * @param secondStreet the second street
   */
  public void setSecondStreet(String secondStreet)
  {
    this.secondStreet = secondStreet;
  }

  /**
   * Retrieves the city.
   * 
   * @return the city
   */
  public String getCity()
  {
    return city;
  }

  /**
   * Sets the city.
   * 
   * @param city the city
   */
  public void setCity(String city)
  {
    this.city = city;
  }

  /**
   * Retrieves the state.
   * 
   * @return the state
   */
  public String getState()
  {
    return state;
  }

  /**
   * Sets the state.
   * 
   * @param state the state
   */
  public void setState(String state)
  {
    this.state = state;
  }

  /**
   * Retrieves the zip.
   * 
   * @return the zip
   */
  public String getZip()
  {
    return zip;
  }

  /**
   * Sets the zip.
   * 
   * @param zip the zip
   */
  public void setZip(String zip)
  {
    this.zip = zip;
  }

  /**
   * Retrieves the employee.
   * 
   * @return the employee
   */
  public Employee getEmployee()
  {
    return employee;
  }

  /**
   * Sets the employee.
   * 
   * @param employee the employee
   */
  public void setEmployee(Employee employee)
  {
    this.employee = employee;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Address other = (Address) obj;
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "Address [id=" + id + ", street=" + street + ", secondStreet="
        + secondStreet + ", city=" + city + ", state=" + state + ", zip=" + zip
        + ", employee=" + employee + "]";
  }

}
