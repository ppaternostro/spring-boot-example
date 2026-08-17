package com.pasquasoft.example.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pasquasoft.example.exception.PatchConversionException;
import com.pasquasoft.example.model.Address;
import com.pasquasoft.example.model.Employee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests
{
  private EmployeeRepository employeeRepository;

  private ObjectMapper objectMapper;
  private XmlMapper xmlMapper;
  private EmployeeService employeeService;

  @BeforeEach
  public void setUp()
  {
    employeeRepository = mock(EmployeeRepository.class);
    objectMapper = spy(new ObjectMapper());
    xmlMapper = new XmlMapper();
    employeeService = new EmployeeService(objectMapper, xmlMapper, employeeRepository);
  }

  @Test
  public void getEmployeesShouldReturnAllEmployees()
  {
    List<Employee> employees = List.of(new Employee("Mercury", "Freddie"));

    when(employeeRepository.findAll()).thenReturn(employees);

    assertThat(employeeService.getEmployees()).isSameAs(employees);
  }

  @Test
  public void getEmployeeShouldReturnMatchingEmployee()
  {
    Employee employee = new Employee("Mercury", "Freddie");
    employee.setId(1L);

    when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

    assertThat(employeeService.getEmployee(1L)).isSameAs(employee);
  }

  @Test
  public void getEmployeeWithNonExistentIdShouldThrowEntityNotFoundException()
  {
    when(employeeRepository.findById(9000L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> employeeService.getEmployee(9000L)).isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Employee with id 9000 was not found");
  }

  @Test
  public void saveShouldDelegateToRepository()
  {
    Employee employee = new Employee("Mercury", "Freddie");

    when(employeeRepository.save(employee)).thenReturn(employee);

    assertThat(employeeService.save(employee)).isSameAs(employee);
    verify(employeeRepository).save(employee);
  }

  @Test
  public void deleteEmployeeShouldDelegateToRepository()
  {
    employeeService.deleteEmployee(1L);

    verify(employeeRepository).deleteById(1L);
  }

  @Test
  public void saveShouldEnforceBeanValidation()
  {
    try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory())
    {
      Validator validator = validatorFactory.getValidator();

      Set<ConstraintViolation<Employee>> violations = validator.validate(new Employee("", null));

      assertThat(violations).hasSize(2);
      assertThat(violations).extracting(violation -> violation.getPropertyPath().toString())
          .containsExactlyInAnyOrder("firstName", "lastName");
      assertThat(violations).extracting(ConstraintViolation::getMessage)
          .containsExactlyInAnyOrder("First name is required", "Last name is required");
    }
  }

  @Test
  public void updateEmployeeShouldCopyFieldsAndReplaceAddresses()
  {
    Employee original = new Employee("Mercury", "Freddie");
    original.setId(1L);
    original.setSsn("111-11-1111");
    original.setMiddleName("Q");

    Address originalAddress = new Address("1 Abbey Road", "London", "Greater London");
    originalAddress.setId(10L);
    original.addAddress(originalAddress);

    Employee updated = new Employee("Bulsara", "Farrokh");
    updated.setSsn("222-22-2222");

    Address updatedAddress = new Address("1535 Broadway", "New York City", "NY");
    updated.addAddress(updatedAddress);

    when(employeeRepository.findById(1L)).thenReturn(Optional.of(original));
    when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Employee result = employeeService.updateEmployee(1L, updated);

    assertThat(result).isSameAs(original);
    assertThat(result.getFirstName()).isEqualTo("Farrokh");
    assertThat(result.getLastName()).isEqualTo("Bulsara");
    assertThat(result.getMiddleName()).isNull();
    assertThat(result.getSsn()).isEqualTo("222-22-2222");
    assertThat(result.getAddresses()).containsExactly(updatedAddress);
    assertThat(updatedAddress.getEmployee()).isSameAs(original);
    verify(employeeRepository).save(original);
  }

  @Test
  public void patchWithValidJsonPatchShouldReturnUpdatedEmployee()
  {
    Employee unpatched = new Employee("Mercury", "Freddie");
    unpatched.setId(4L);
    unpatched.addAddress(new Address("4 Abbey Road", "London", "Greater London"));

    when(employeeRepository.findById(4L)).thenReturn(Optional.of(unpatched));
    when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

    List<Map<String, Object>> jsonPatch = List.of(Map.of("op", "replace", "path", "/lastName", "value", "Bulsara"));

    Employee result = employeeService.patch(jsonPatch, 4L);

    assertThat(result).isSameAs(unpatched);
    assertThat(result.getLastName()).isEqualTo("Bulsara");
    verify(employeeRepository).save(unpatched);
  }

  @Test
  public void patchWithInvalidJsonPatchShouldThrowPatchConversionException()
  {
    Employee unpatched = new Employee("Mercury", "Freddie");
    unpatched.setId(4L);

    when(employeeRepository.findById(4L)).thenReturn(Optional.of(unpatched));

    List<Map<String, Object>> jsonPatch = List
        .of(Map.of("op", "replace", "path", "/nonexistentField", "value", "Bulsara"));

    assertThatThrownBy(() -> employeeService.patch(jsonPatch, 4L)).isInstanceOf(PatchConversionException.class)
        .hasMessageContaining("nonexistentField");
  }

  @Test
  public void patchShouldWrapJacksonErrorsIntoPatchConversionException()
  {
    Employee unpatched = new Employee("Mercury", "Freddie");
    unpatched.setId(4L);

    when(employeeRepository.findById(4L)).thenReturn(Optional.of(unpatched));
    when(objectMapper.treeToValue(any(JsonNode.class), eq(Employee.class))).thenThrow(mock(JacksonException.class));

    List<Map<String, Object>> jsonPatch = List.of(Map.of("op", "replace", "path", "/firstName", "value", "New"));

    assertThatThrownBy(() -> employeeService.patch(jsonPatch, 4L)).isInstanceOf(PatchConversionException.class);
  }

  @Test
  public void patchWithValidXmlPatchShouldReturnUpdatedEmployee()
  {
    Employee unpatched = new Employee("Mercury", "Freddie");
    unpatched.setId(6L);
    unpatched.addAddress(new Address("6 Abbey Road", "London", "Greater London"));

    when(employeeRepository.findById(6L)).thenReturn(Optional.of(unpatched));
    when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

    String xmlPatch = """
        <diff>
          <replace sel="employee/lastName/text()">Bulsara</replace>
        </diff>""";

    Employee result = employeeService.patch(xmlPatch, 6L);

    assertThat(result).isSameAs(unpatched);
    assertThat(result.getLastName()).isEqualTo("Bulsara");
    verify(employeeRepository).save(unpatched);
  }

  @Test
  public void patchWithXmlShouldWrapProcessingErrorsIntoPatchConversionException() throws JsonProcessingException
  {
    Employee unpatched = new Employee("Mercury", "Freddie");
    unpatched.setId(6L);

    when(employeeRepository.findById(6L)).thenReturn(Optional.of(unpatched));

    XmlMapper failingXmlMapper = spy(xmlMapper);
    doThrow(mock(JsonProcessingException.class)).when(failingXmlMapper).writeValueAsBytes(any(Employee.class));

    EmployeeService failingService = new EmployeeService(objectMapper, failingXmlMapper, employeeRepository);

    assertThatThrownBy(() -> failingService.patch("<diff></diff>", 6L)).isInstanceOf(PatchConversionException.class);
  }
}
