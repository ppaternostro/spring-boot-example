package com.pasquasoft.example.model;

import javax.persistence.MappedSuperclass;

/**
 * A class that defines the attributes and behavior of a person.
 * 
 * @author ppaternostro
 * 
 */
@MappedSuperclass
public abstract class Person
{
  private String lastName;
  private String firstName;
  private String middleName;

  /**
   * Retrieves the last name.
   * 
   * @return the last name
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * Sets the last name.
   * 
   * @param lastName the last name
   */
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  /**
   * Retrieves the first name.
   * 
   * @return the first name
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Sets the first name.
   * 
   * @param firstName the first name
   */
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  /**
   * Retrieves the middle name.
   * 
   * @return the middle name
   */
  public String getMiddleName()
  {
    return middleName;
  }

  /**
   * Sets the middle name.
   * 
   * @param middleName the middle name
   */
  public void setMiddleName(String middleName)
  {
    this.middleName = middleName;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result
        + ((middleName == null) ? 0 : middleName.hashCode());
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
    Person other = (Person) obj;
    if (firstName == null)
    {
      if (other.firstName != null)
        return false;
    }
    else if (!firstName.equals(other.firstName))
      return false;
    if (lastName == null)
    {
      if (other.lastName != null)
        return false;
    }
    else if (!lastName.equals(other.lastName))
      return false;
    if (middleName == null)
    {
      if (other.middleName != null)
        return false;
    }
    else if (!middleName.equals(other.middleName))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "Person [lastName=" + lastName + ", firstName=" + firstName
        + ", middleName=" + middleName + "]";
  }

}
