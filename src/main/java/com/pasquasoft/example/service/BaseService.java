package com.pasquasoft.example.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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

  protected <T> T applyPatch(List<Map<String, Object>> jsonPatch, T targetObject, Class<T> targetType)
  {
    JsonNode patchNode = objectMapper.valueToTree(jsonPatch);

    JsonNode targetNode = objectMapper.valueToTree(targetObject);

    JsonNode patchedNode = Jackson3JsonPatch.apply(patchNode, targetNode);

    return objectMapper.treeToValue(patchedNode, targetType);
  }

  @SuppressWarnings("unchecked")
  protected <T> T applyPatch(String xmlPatch, T unpatchedObject) throws IOException
  {
    // Convert un-patched object into an input stream
    InputStream unpatchedStream = new ByteArrayInputStream(xmlMapper.writeValueAsBytes(unpatchedObject));

    // Convert XML patch string into an input stream
    InputStream patchStream = new ByteArrayInputStream(xmlPatch.getBytes());

    // Apply the patch
    OutputStream patchedStream = new ByteArrayOutputStream();
    Patcher.patch(unpatchedStream, patchStream, patchedStream);

    return (T) xmlMapper.readValue(patchedStream.toString(), unpatchedObject.getClass());
  }

}
