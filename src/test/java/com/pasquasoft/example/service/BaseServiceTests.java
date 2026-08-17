package com.pasquasoft.example.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.pasquasoft.example.model.Employee;

import tools.jackson.databind.ObjectMapper;

public class BaseServiceTests
{
  private TestBaseService baseService;

  @BeforeEach
  public void setUp()
  {
    baseService = new TestBaseService(new ObjectMapper(), new XmlMapper());
  }

  @Test
  public void applyXmlPatchWithExplicitTargetTypeShouldReturnPatchedObject() throws IOException
  {
    Employee unpatched = new Employee("Mercury", "Freddie");

    String xmlPatch = """
        <diff>
          <replace sel="employee/lastName/text()">Bulsara</replace>
        </diff>""";

    Employee result = baseService.applyPatch(xmlPatch, unpatched, Employee.class);

    assertThat(result).isNotSameAs(unpatched);
    assertThat(result).isInstanceOf(Employee.class);
    assertThat(result.getLastName()).isEqualTo("Bulsara");
  }

  @Test
  public void applyXmlPatchShouldHonorTargetTypeOverObjectRuntimeType() throws IOException
  {
    Employee unpatched = new Employee("Mercury", "Freddie") {};

    String xmlPatch = """
        <diff>
          <replace sel="employee/firstName/text()">Farrokh</replace>
        </diff>""";

    Employee result = baseService.applyPatch(xmlPatch, unpatched, Employee.class);

    assertThat(result).isNotSameAs(unpatched);
    assertThat(result.getClass()).isEqualTo(Employee.class);
    assertThat(result.getFirstName()).isEqualTo("Farrokh");
  }

  private static class TestBaseService extends BaseService
  {
    TestBaseService(ObjectMapper objectMapper, XmlMapper xmlMapper)
    {
      super(objectMapper, xmlMapper);
    }

    protected <T> T applyPatch(String xmlPatch, T unpatchedObject, Class<T> targetType) throws IOException
    {
      return super.applyPatch(xmlPatch, unpatchedObject, targetType);
    }
  }
}
