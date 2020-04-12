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

  public Workout setType(Type type) {
    this.type = type;
    return this;
  }

  public String getName() {
    return name;
  }

  public Workout setName(String name) {
    this.name = name;
    return this;
  }

  public String getScheme() {
    return scheme;
  }

  public Workout setScheme(String scheme) {
    this.scheme = scheme;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Workout setDescription(String description) {
    this.description = description;
    return this;
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
