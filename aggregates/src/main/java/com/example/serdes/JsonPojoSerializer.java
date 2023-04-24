package com.example.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class JsonPojoSerializer<T> implements Serializer<T> {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public void close() {}

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {}

  @Override
  public byte[] serialize(String topic, T data) {
    if (data == null) {
      return null;
    }
    // convert json to bytes[]
    try {
      return OBJECT_MAPPER.writeValueAsBytes(data);
    } catch (Exception e) {
      throw new SerializationException("Error serializing JSON message", e);
    }
  }
}
