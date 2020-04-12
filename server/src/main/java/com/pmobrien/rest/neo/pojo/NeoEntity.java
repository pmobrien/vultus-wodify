package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pmobrien.rest.converters.UUIDConverter;
import java.util.UUID;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
public abstract class NeoEntity {
  
  // TODO fix these annotations
  @Convert(UUIDConverter.class)
  @Index(unique = true, primary = true)
  private UUID uuid;
  
  public UUID getUuid() {
    return uuid;
  }
  
  protected NeoEntity setUuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }
  
  public String toJson() {
    try {
      return new ObjectMapper()
          .configure(SerializationFeature.INDENT_OUTPUT, true)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .writeValueAsString(this);
    } catch(JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
