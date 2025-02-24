package com.pasquasoft.example;

import org.apache.commons.lang3.RandomStringUtils;
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
  private Map<String, String> employeeMap = Map.of("/1", "Mercury", "/2", "May", "/3", "Taylor");

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
  @ValueSource(strings = { "/1", "/2", "/3" })
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
   * request once. Deleting a non-existent resource has the same effect as
   * deleting an existent resource multiple times. Thanks Captain Obvious! :-)
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
    executeAndAssert(HttpMethod.POST, type, "");
  }

  @ParameterizedTest
  @MethodSource("provideMediaType")
  public void putEmployeeShouldReturnUpdatedEmployee(MediaType type)
  {
    executeAndAssert(HttpMethod.PUT, type, "/4");
  }

  @ParameterizedTest
  @MethodSource("provideEmployee")
  public void putEmployeeWithInvalidBodyShouldFail(Employee employee)
  {
    HttpEntity<Employee> requestEntity = new HttpEntity<Employee>(employee);

    ResponseEntity<Employee> response = restTemplate.exchange(url + "/1", HttpMethod.PUT, requestEntity,
        new ParameterizedTypeReference<Employee>() {
        });

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void patchEmployeeWithValidJsonShouldReturnUpdatedEmployee()
  {
    String payload = "[{\"op\": \"replace\",\"path\": \"/lastName\", \"value\": \"Bulsara\"},"
        + "{\"op\": \"replace\",\"path\": \"/firstName\",\"value\": \"Farohk\"},"
        + "{\"op\": \"remove\",\"path\": \"/addresses/0\"}]";

    ResponseEntity<Employee> response = setHeadersAndPayloadAndExecute(payload);

    HttpStatusCode statusCode = response.getStatusCode();
    Employee employee = response.getBody();

    assertThat(statusCode).isEqualTo(HttpStatus.OK);
    assertThat(employee).isNotNull();
    assertThat(employee.getLastName()).isEqualTo("Bulsara");
    assertThat(employee.getFirstName()).isEqualTo("Farohk");
    assertThat(employee.getAddresses()).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = { "", "{}", "{[]}", "{\"op\": \"replace\",\"path\": \"/lastName\", \"value\": \"Bulsara\"}" })
  public void patchEmployeeWithInvalidJsonShouldFail(String payload)
  {
    ResponseEntity<Employee> response = setHeadersAndPayloadAndExecute(payload);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @ParameterizedTest
  @MethodSource("provideParamsForNegativeTests")
  public void restCallsShouldReturnSpecifiedStatusCodes(HttpMethod method, String path, HttpStatusCode statusCode)
  {
    ResponseEntity<?> response = restTemplate.exchange(url + path, method, null, (Class<?>) null);

    assertThat(response.getStatusCode()).isEqualTo(statusCode);
  }

  private ResponseEntity<Employee> setHeadersAndPayloadAndExecute(String payload)
  {
    HttpHeaders headers = new HttpHeaders();

    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.set("Content-Type", "application/json-patch+json");

    HttpEntity<String> requestEntity = new HttpEntity<String>(payload, headers);

    return restTemplate.exchange(url + "/4", HttpMethod.PATCH, requestEntity,
        new ParameterizedTypeReference<Employee>() {
        });
  }

  private static Stream<Arguments> provideParamsForNegativeTests()
  {
    return Stream.of(Arguments.of(HttpMethod.GET, "/-1", HttpStatus.NOT_FOUND),
        Arguments.of(HttpMethod.GET, "/0", HttpStatus.NOT_FOUND),
        Arguments.of(HttpMethod.GET, "/a", HttpStatus.BAD_REQUEST),
        Arguments.of(HttpMethod.POST, "", HttpStatus.BAD_REQUEST),
        Arguments.of(HttpMethod.POST, "/1", HttpStatus.METHOD_NOT_ALLOWED),
        Arguments.of(HttpMethod.PUT, "", HttpStatus.METHOD_NOT_ALLOWED),
        Arguments.of(HttpMethod.PUT, "/0", HttpStatus.BAD_REQUEST),
        Arguments.of(HttpMethod.PATCH, "", HttpStatus.METHOD_NOT_ALLOWED),
        Arguments.of(HttpMethod.PATCH, "/0", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
        Arguments.of(HttpMethod.DELETE, "", HttpStatus.METHOD_NOT_ALLOWED));
  }

  private Stream<Arguments> provideMediaType()
  {
    return Stream.of(Arguments.of(MediaType.APPLICATION_JSON), Arguments.of(MediaType.APPLICATION_XML));
  }

  private Stream<Arguments> provideEmployee()
  {
    Employee employee = new Employee("Plant", "Robert");
    employee.setAddresses(Arrays.asList(new Address(null, "", null)));

    return Stream.of(Arguments.of(employee), Arguments.of((Employee) null), Arguments.of(new Employee()),
        Arguments.of(new Employee("", null)), Arguments.of(new Employee("Mercury", "")),
        Arguments.of(new Employee(null, "Freddie")));
  }

  private void executeAndAssert(HttpMethod method, MediaType type, String path)
  {
    String lastName = RandomStringUtils.secure().nextAlphabetic(10);
    String firstName = RandomStringUtils.secure().nextAlphabetic(5);
    String street = RandomStringUtils.secure().nextAlphabetic(8);
    String city = RandomStringUtils.secure().nextAlphabetic(8);
    String state = RandomStringUtils.secure().nextAlphabetic(8);

    Employee payload = new Employee(lastName, firstName);
    payload.setAddresses(Arrays.asList(new Address(street, city, state)));

    HttpHeaders headers = new HttpHeaders();

    headers.setAccept(Arrays.asList(type));
    headers.setContentType(type);

    HttpEntity<Employee> requestEntity = new HttpEntity<Employee>(payload, headers);

    ResponseEntity<Employee> response = restTemplate.exchange(url + path, method, requestEntity,
        new ParameterizedTypeReference<Employee>() {
        });

    Employee employee = response.getBody();

    assertThat(employee).isNotNull();
    assertThat(employee.getLastName()).isEqualTo(lastName);
    assertThat(employee.getFirstName()).isEqualTo(firstName);
    assertThat(employee.getAddresses()).isNotNull();
    assertThat(employee.getAddresses().size()).isEqualTo(1);
    assertThat(employee.getAddresses().get(0).getStreet()).isEqualTo(street);
    assertThat(employee.getAddresses().get(0).getCity()).isEqualTo(city);
    assertThat(employee.getAddresses().get(0).getState()).isEqualTo(state);
  }
}
