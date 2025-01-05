package com.pasquasoft.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.pasquasoft.example.employee.EmployeeController;
import com.pasquasoft.example.model.Address;
import com.pasquasoft.example.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class SpringBootExampleApplicationTests
{
  private static Map<String, String> employeeMap = Map.of("/1", "Mercury", "/2", "May", "/3", "Taylor", "/4", "Deacon");

  @Autowired
  private EmployeeController controller;

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private String url;

  @BeforeAll
  public void init()
  {
    url = "http://localhost:" + port + "/employees";
  }

  @Test
  public void contextLoads()
  {
    assertThat(controller).isNotNull();
  }

  @Test
  public void getEmployeesShouldReturnResult()
  {
    ResponseEntity<List<Employee>> response = restTemplate.exchange(url, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Employee>>() {
        });

    List<Employee> employees = response.getBody();

    assertThat(employees.size() > 0).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = { "/1", "/2", "/3", "/4" })
  public void getEmployeeWithPathParamShouldReturnCorrectResult(String path)
  {
    ResponseEntity<Employee> response = restTemplate.exchange(url + path, HttpMethod.GET, null,
        new ParameterizedTypeReference<Employee>() {
        });

    assertThat(response.getBody().getLastName()).isEqualTo(employeeMap.get(path));
  }

  /*
   * REST DELETEs are idempotent. If an existing resource is deleted multiple
   * times the state of the server will be the same as if it only received that
   * request once.
   */
  @ParameterizedTest
  @ValueSource(strings = { "/5", "/5", "/9000" })
  public void deleteEmployeeWithPathParamShouldReturnCorrectStatusCode(String path)
  {
    ResponseEntity<Void> response = restTemplate.exchange(url + path, HttpMethod.DELETE, null,
        new ParameterizedTypeReference<Void>() {
        });

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @ParameterizedTest
  @MethodSource("provideMediaType")
  public void postEmployeeShouldReturnCreatedEmployee(MediaType type)
  {
    Employee payload = new Employee("Plant", "Robert");
    payload.setAddresses(Arrays.asList(new Address("1 Abbey Road", "London", "Greater London")));

    HttpHeaders headers = new HttpHeaders();

    headers.setAccept(Arrays.asList(type));
    headers.setContentType(type);

    HttpEntity<Employee> requestEntity = new HttpEntity<Employee>(payload, headers);

    ResponseEntity<Employee> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
        new ParameterizedTypeReference<Employee>() {
        });

    Employee employee = response.getBody();

    assertThat(employee).isNotNull();
    assertThat(employee.getLastName()).isEqualTo("Plant");
    assertThat(employee.getFirstName()).isEqualTo("Robert");
    assertThat(employee.getAddresses()).isNotNull();
    assertThat(employee.getAddresses().size()).isEqualTo(1);
    assertThat(employee.getAddresses().get(0).getStreet()).isEqualTo("1 Abbey Road");
    assertThat(employee.getAddresses().get(0).getCity()).isEqualTo("London");
    assertThat(employee.getAddresses().get(0).getState()).isEqualTo("Greater London");
  }

  @ParameterizedTest
  @MethodSource("provideParamsForNegativeTests")
  public void restCallsShouldReturnSpecifiedStatusCodes(HttpMethod method, String path, HttpStatusCode statusCode)
  {
    ResponseEntity<?> response = restTemplate.exchange(url + path, method, null, (Class<?>) null);

    assertThat(response.getStatusCode()).isEqualTo(statusCode);
  }

  private static Stream<Arguments> provideParamsForNegativeTests()
  {
    return Stream.of(Arguments.of(HttpMethod.GET, "/-1", HttpStatus.NOT_FOUND),
        Arguments.of(HttpMethod.GET, "/0", HttpStatus.NOT_FOUND),
        Arguments.of(HttpMethod.GET, "/a", HttpStatus.BAD_REQUEST),
        Arguments.of(HttpMethod.POST, "", HttpStatus.BAD_REQUEST),
        Arguments.of(HttpMethod.POST, "/1", HttpStatus.METHOD_NOT_ALLOWED),
        Arguments.of(HttpMethod.DELETE, "", HttpStatus.METHOD_NOT_ALLOWED));
  }

  private Stream<Arguments> provideMediaType()
  {
    return Stream.of(Arguments.of(MediaType.APPLICATION_JSON), Arguments.of(MediaType.APPLICATION_XML));
  }
}
