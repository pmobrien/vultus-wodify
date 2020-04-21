package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.Strings;
import java.io.IOException;
import org.neo4j.ogm.annotation.Relationship;

public class Workout extends NeoEntity {
  
  public enum Type {
    AMRAP_REPS("AMRAP - Reps"),
    AMRAP_ROUNDS("AMRAP - Rounds"),
    AMRAP_ROUNDS_AND_REPS("AMRAP - Rounds and Reps"),
    EACH_ROUND_FOR_REPS("Each Round for For Reps"),
    EACH_ROUND_FOR_TIME("Each Round for For Time"),
    TIME("Time"),
    WEIGHT("Weight");
    
    private final String value;
    
    Type(String value) {
      this.value = value;
    }
    
    public static Type parse(String string) {
      if(Strings.isNullOrEmpty(string)) {
        return null;
      }
      
      for(Type type : Type.values()) {
        if(type.value.equals(string)) {
          return type;
        }
      }
      
      throw new RuntimeException(String.format("Unknow Workout.Type: %s", string));
    }
  }
  
  private Type type;
  private String name;
  private String scheme;
  private String description;
  
  @Relationship(type = "INSTANCE_OF", direction = Relationship.INCOMING)
  private Performance performance;
  
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

  public Performance getPerformance() {
    return performance;
  }

  public Workout setPerformance(Performance performance) {
    this.performance = performance;
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
      
      writeFields(workout, generator);
      
      generator.writeEndObject();
    }
    
    public void writeFields(Workout workout, JsonGenerator generator) throws IOException {
      generator.writeStringField("uuid", workout.getUuid().toString());
      generator.writeStringField("type", workout.getType() == null ? null : workout.getType().name());
      generator.writeStringField("name", workout.getName());
      generator.writeStringField("scheme", workout.getScheme());
      generator.writeStringField("description", workout.getDescription());
    }
  }
}
