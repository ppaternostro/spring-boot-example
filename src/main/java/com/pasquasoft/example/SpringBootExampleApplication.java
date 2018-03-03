package com.pasquasoft.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pasquasoft.example.employee.EmployeeRepository;
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

  /*
   * Seed the in-memory database on application startup.
   */
  @Bean
  CommandLineRunner runner()
  {
    return args -> {
      employeeRepository.save(new Employee(1L, "Lennon"));
      employeeRepository.save(new Employee(2L, "McCartney"));
      employeeRepository.save(new Employee(3L, "Harrison"));
      employeeRepository.save(new Employee(4L, "Starr"));      
    };
  }
}
