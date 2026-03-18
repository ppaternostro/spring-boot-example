package com.pasquasoft.example.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.dnault.xmlpatch.Patcher;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@Service
@Transactional
public abstract class BaseService
{
  protected static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected XmlMapper xmlMapper;

  @SuppressWarnings("unchecked")
  protected <T> T applyPatch(JsonPatch jsonPatch, T targetObject) throws JsonPatchException, JsonProcessingException
  {
    JsonNode patched = jsonPatch.apply(objectMapper.convertValue(targetObject, JsonNode.class));
    return (T) objectMapper.treeToValue(patched, targetObject.getClass());
  }

  @SuppressWarnings("unchecked")
  protected <T> T applyPatch(String patchXml, T existingObject) throws IOException
  {
    // Convert existing resource into an input stream
    InputStream existingStream = new ByteArrayInputStream(xmlMapper.writeValueAsBytes(existingObject));

    // Convert incoming patch into an input stream
    InputStream patchStream = new ByteArrayInputStream(patchXml.getBytes());

    // Apply the patch
    OutputStream patchedStream = new ByteArrayOutputStream();
    Patcher.patch(existingStream, patchStream, patchedStream);

    return (T) xmlMapper.readValue(patchedStream.toString(), existingObject.getClass());
  }

}
