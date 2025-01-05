package com.pasquasoft.example.error;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ApiErrorResponse
{
  @JacksonXmlProperty(localName = "error")
  @JacksonXmlElementWrapper(localName = "errors")
  private List<ApiError> errors;

  public ApiErrorResponse(List<ApiError> errors)
  {
    setErrors(errors);
  }

  public List<ApiError> getErrors()
  {
    return errors;
  }

  public void setErrors(List<ApiError> errors)
  {
    this.errors = errors;
  }

}
