package com.example.serdes;

import com.example.model.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class JsonHybridDeserializer<T> implements Deserializer<T> {

  private static final String DBZ_CDC_EVENT_PAYLOAD_FIELD = "payload";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private Class<T> clazz;

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {
    clazz = (Class<T>) props.get("serializedClass");
  }

  @Override
  public T deserialize(String topic, byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    T data = null;
    // convert byte[] to java object
    try {
      data = OBJECT_MAPPER.readValue(bytes, clazz);
    } catch (IOException e) {
      try {
        // 1) convert to map
        Map temp = (Map) OBJECT_MAPPER
          .readValue(new String(bytes), Map.class)
          .get(DBZ_CDC_EVENT_PAYLOAD_FIELD);
        if (temp == null) {
          temp = new HashMap();
          // It is a delete case. So we record a delete event in this case.
          temp.put("_eventType", EventType.DELETE);
        }
        data =
          OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsBytes(temp), clazz);
      } catch (IOException ioe) {
        throw new SerializationException(ioe);
      }
    }
    return data;
  }

  @Override
  public void close() {}
}
