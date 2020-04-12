package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class Workout extends NeoEntity {

  public enum Type {
    LIFT,
    METCON_FOR_TIME,
    METCON_FOR_ROUNDS;
  }
  
  private Type type;
  private String name;
  private String scheme;
  private String description;
  
  public Workout() {}

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public static class Serializer extends StdSerializer<Workout> {

    public Serializer() {
      this(null);
    }

    public Serializer(Class<Workout> type) {
      super(type);
    }

    @Override
    public void serialize(Workout workout, JsonGenerator generator, SerializerProvider provider) throws IOException {
      generator.writeStartObject();
      
      generator.writeStringField("uuid", workout.getUuid().toString());
      generator.writeStringField("type", workout.getType().name());
      generator.writeStringField("name", workout.getName());
      generator.writeStringField("scheme", workout.getScheme());
      generator.writeStringField("description", workout.getDescription());
      
      generator.writeEndObject();
    }
  }
}
