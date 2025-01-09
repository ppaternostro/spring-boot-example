package com.pasquasoft.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@Service
@Transactional
public abstract class BaseService
{
  protected static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

  @Autowired
  protected ObjectMapper objectMapper;

  @SuppressWarnings("unchecked")
  protected <T> T applyPatch(JsonPatch patch, T targetObject) throws JsonPatchException, JsonProcessingException
  {
    JsonNode patched = patch.apply(objectMapper.convertValue(targetObject, JsonNode.class));
    return (T) objectMapper.treeToValue(patched, targetObject.getClass());
  }

}
