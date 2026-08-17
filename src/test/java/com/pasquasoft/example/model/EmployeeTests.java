package com.pasquasoft.example.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class EmployeeTests
{
  @Test
  public void twoEmployeesWithSameValuesShouldBeEqual()
  {
    Employee first = new Employee("Mercury", "Freddie");
    Employee second = new Employee("Mercury", "Freddie");

    assertThat(first).isEqualTo(second);
    assertThat(first.hashCode()).isEqualTo(second.hashCode());
  }

  @Test
  public void employeesWithDifferentNamesShouldNotBeEqual()
  {
    Employee first = new Employee("Mercury", "Freddie");
    Employee second = new Employee("Mercury", "Farrokh");

    assertThat(first).isNotEqualTo(second);
  }

  @Test
  public void employeesWithDifferentSsnShouldNotBeEqual()
  {
    Employee first = new Employee("Mercury", "Freddie");
    first.setSsn("111-11-1111");

    Employee second = new Employee("Mercury", "Freddie");
    second.setSsn("222-22-2222");

    assertThat(first).isNotEqualTo(second);
  }

  @Test
  public void employeeShouldNotBeEqualToNull()
  {
    assertThat(new Employee()).isNotEqualTo(null);
  }

  @Test
  public void employeeShouldNotBeEqualToDifferentClassObject()
  {
    assertThat(new Employee()).isNotEqualTo(new Address("1 Abbey Road", "London", "Greater London"));
  }

  @Test
  public void toStringShouldContainEmployeeAttributes()
  {
    Employee employee = new Employee("Mercury", "Freddie");
    employee.setId(1L);
    employee.setMiddleName("B");
    employee.setSsn("111-11-1111");

    String result = employee.toString();

    assertThat(result).contains("id=1", "ssn=111-11-1111", "lastName=Mercury", "firstName=Freddie", "middleName=B");
  }

  @Test
  public void addAddressShouldSetEmployeeBackReference()
  {
    Employee employee = new Employee("Mercury", "Freddie");
    Address address = new Address("1 Abbey Road", "London", "Greater London");

    employee.addAddress(address);

    assertThat(employee.getAddresses()).containsExactly(address);
    assertThat(address.getEmployee()).isSameAs(employee);
  }

  @Test
  public void removeAddressShouldClearEmployeeBackReference()
  {
    Employee employee = new Employee("Mercury", "Freddie");
    Address address = new Address("1 Abbey Road", "London", "Greater London");
    employee.addAddress(address);

    employee.removeAddress(address);

    assertThat(employee.getAddresses()).isEmpty();
    assertThat(address.getEmployee()).isNull();
  }
}
