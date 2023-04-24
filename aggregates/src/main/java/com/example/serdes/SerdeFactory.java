package com.example.serdes;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class SerdeFactory {

  public static <T> Serde<T> createDbzEventJsonPojoSerdeFor(
    Class<T> clazz,
    boolean isKey
  ) {
    // serializedClass setting
    Map<String, Object> serdeProps = new HashMap<String, Object>();
    serdeProps.put("serializedClass", clazz);
    // construct serializer
    Serializer<T> ser = new JsonPojoSerializer<>();
    ser.configure(serdeProps, isKey);
    // construct deserializer
    Deserializer<T> de = new JsonHybridDeserializer<>();
    de.configure(serdeProps, isKey);

    // return Serde
    return Serdes.serdeFrom(ser, de);
  }
}
