package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.neo4j.ogm.annotation.Relationship;

public class Athlete extends NeoEntity {

  private String wodifyId;
  private String name;
  private String email;
  
  @Relationship(type = "COMPLETED", direction = Relationship.OUTGOING)
  private Performance performance;
  
  public Athlete() {}

  public String getWodifyId() {
    return wodifyId;
  }

  public Athlete setWodifyId(String wodifyId) {
    this.wodifyId = wodifyId;
    return this;
  }

  public String getName() {
    return name;
  }

  public Athlete setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Athlete setEmail(String email) {
    this.email = email;
    return this;
  }

  public Performance getPerformance() {
    return performance;
  }

  public Athlete setPerformance(Performance performance) {
    this.performance = performance;
    return this;
  }
  
  public static class Serializer extends StdSerializer<Athlete> {

    public Serializer() {
      this(null);
    }

    public Serializer(Class<Athlete> type) {
      super(type);
    }

    @Override
    public void serialize(Athlete athlete, JsonGenerator generator, SerializerProvider provider) throws IOException {
      generator.writeStartObject();
      
      writeFields(athlete, generator);
      
      if(athlete.getPerformance() != null) {
        generator.writeFieldName("performance");
        generator.writeStartObject();
        new Performance.Serializer().writeFields(athlete.getPerformance(), generator);
        generator.writeEndObject();
      }
      
      generator.writeEndObject();
    }
    
    protected void writeFields(Athlete athlete, JsonGenerator generator) throws IOException {
      generator.writeStringField("uuid", athlete.getUuid().toString());
      generator.writeStringField("wodifyId", athlete.getWodifyId());
      generator.writeStringField("name", athlete.getName());
      generator.writeStringField("email", athlete.getEmail());
    }
  }
}
