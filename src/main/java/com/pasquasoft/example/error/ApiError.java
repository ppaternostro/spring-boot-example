package com.pasquasoft.example.error;

public class ApiError
{
  private String fieldName;
  private String errorCode;

  public ApiError(String fieldName, String errorCode)
  {
    setFieldName(fieldName);
    setErrorCode(errorCode);
  }

  public String getFieldName()
  {
    return fieldName;
  }

  public void setFieldName(String fieldName)
  {
    this.fieldName = fieldName;
  }

  public String getErrorCode()
  {
    return errorCode;
  }

  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }

}
