package com.pasquasoft.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pasquasoft.example.employee.EmployeeRepository;
import com.pasquasoft.example.model.Address;
import com.pasquasoft.example.model.Employee;

@SpringBootApplication
public class SpringBootExampleApplication
{
  @Autowired
  private EmployeeRepository employeeRepository;

  public static void main(String[] args)
  {
    SpringApplication.run(SpringBootExampleApplication.class, args);
  }

  @Bean
  @Primary
  ObjectMapper objectMapper()
  {
    return new ObjectMapper();
  }

  @Bean
  XmlMapper xmlMapper()
  {
    return new XmlMapper();
  }

  /*
   * Seed the in-memory database on application startup.
   */
  @Bean
  CommandLineRunner runner()
  {
    return args -> {
      Employee mercury = new Employee("Mercury", "Freddie");
      Employee may = new Employee("May", "Brian");
      Employee taylor = new Employee("Taylor", "Roger");
      Employee deacon = new Employee("Deacon", "John");
      Employee perry = new Employee("Perry", "Joe");
      Employee tyler = new Employee("Tyler", "Steven");

      mercury.addAddress(new Address("1 Abbey Road", "London", "Greater London"));
      may.addAddress(new Address("2 Abbey Road", "London", "Greater London"));
      taylor.addAddress(new Address("3 Abbey Road", "London", "Greater London"));
      deacon.addAddress(new Address("4 Abbey Road", "London", "Greater London"));
      tyler.addAddress(new Address("5 Abbey Road", "London", "Greater London"));

      employeeRepository.save(mercury);
      employeeRepository.save(may);
      employeeRepository.save(taylor);
      employeeRepository.save(deacon);
      employeeRepository.save(perry);
      employeeRepository.save(tyler);
    };
  }
}
