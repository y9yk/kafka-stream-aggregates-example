package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.KeyDeserializers;
import java.io.IOException;

public class DefaultId {

  @JsonIgnore
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final Integer id;

  @JsonCreator
  public DefaultId(@JsonProperty("id") Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return this.id;
  }

  public String toString() {
    return "DefaultId{" + "id=" + id + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    // process
    DefaultId defaultId = (DefaultId) o;
    return this.id != null
      ? this.id.equals(defaultId.id)
      : defaultId.id == null;
  }

  @Override
  public int hashCode() {
    return this.id != null ? this.id.hashCode() : 0;
  }

  public static class IdSerializer extends JsonSerializer<DefaultId> {

    @Override
    public void serialize(
      DefaultId key,
      JsonGenerator gen,
      SerializerProvider serializers
    ) throws IOException {
      gen.writeFieldName(OBJECT_MAPPER.writeValueAsString(key));
    }
  }

  public static class IdDeserializer extends KeyDeserializer {

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt)
      throws IOException {
      return OBJECT_MAPPER.readValue(key, DefaultId.class);
    }
  }
}
