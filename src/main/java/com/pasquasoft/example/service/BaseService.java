package com.pasquasoft.example.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.flipkart.zjsonpatch.Jackson3JsonPatch;
import com.github.dnault.xmlpatch.Patcher;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@Transactional
public abstract class BaseService
{
  protected static final Logger LOG = LoggerFactory.getLogger(BaseService.class);

  protected final ObjectMapper objectMapper;
  protected final XmlMapper xmlMapper;

  /*
   * Favor constructor injection over attribute or setter injection.
   */
  protected BaseService(ObjectMapper objectMapper, XmlMapper xmlMapper)
  {
    this.objectMapper = objectMapper;
    this.xmlMapper = xmlMapper;
  }

  protected <T> T applyPatch(JsonNode patchNode, T targetObject, Class<T> targetType)
  {
    JsonNode targetNode = objectMapper.valueToTree(targetObject);

    JsonNode patchedNode = Jackson3JsonPatch.apply(patchNode, targetNode);

    return objectMapper.treeToValue(patchedNode, targetType);
  }

  protected <T> T applyPatch(String xmlPatch, T unpatchedObject, Class<T> targetType) throws IOException
  {
    // Convert un-patched object into an input stream
    InputStream unpatchedStream = new ByteArrayInputStream(xmlMapper.writeValueAsBytes(unpatchedObject));

    // Convert XML patch string into an input stream
    InputStream patchStream = new ByteArrayInputStream(xmlPatch.getBytes(StandardCharsets.UTF_8));

    // Apply the patch
    ByteArrayOutputStream patchedStream = new ByteArrayOutputStream();
    Patcher.patch(unpatchedStream, patchStream, patchedStream);

    return xmlMapper.readValue(patchedStream.toByteArray(), targetType);
  }

}
