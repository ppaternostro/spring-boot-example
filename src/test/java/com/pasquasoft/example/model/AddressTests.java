package com.pasquasoft.example.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class AddressTests
{
  @Test
  public void twoAddressesWithSameIdShouldBeEqual()
  {
    Address first = new Address("1 Abbey Road", "London", "Greater London");
    first.setId(1L);

    Address second = new Address("2 Abbey Road", "London", "Greater London");
    second.setId(1L);

    assertThat(first).isEqualTo(second);
    assertThat(first.hashCode()).isEqualTo(second.hashCode());
  }

  @Test
  public void addressesWithDifferentIdsShouldNotBeEqual()
  {
    Address first = new Address("1 Abbey Road", "London", "Greater London");
    first.setId(1L);

    Address second = new Address("1 Abbey Road", "London", "Greater London");
    second.setId(2L);

    assertThat(first).isNotEqualTo(second);
  }

  @Test
  public void addressShouldNotBeEqualToNull()
  {
    assertThat(new Address()).isNotEqualTo(null);
  }

  @Test
  public void addressShouldNotBeEqualToDifferentClassObject()
  {
    assertThat(new Address("1 Abbey Road", "London", "Greater London")).isNotEqualTo(new Employee());
  }

  @Test
  public void toStringShouldContainAddressAttributes()
  {
    Address address = new Address("1 Abbey Road", "London", "Greater London");
    address.setId(1L);
    address.setSecondStreet("Apt 2");
    address.setZip("NW8");

    String result = address.toString();

    assertThat(result).contains("id=1", "street=1 Abbey Road", "secondStreet=Apt 2", "city=London",
        "state=Greater London", "zip=NW8");
  }
}
