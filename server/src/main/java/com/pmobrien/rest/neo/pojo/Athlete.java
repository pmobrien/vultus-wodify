package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class Athlete extends NeoEntity {

  private String wodifyId;
  private String name;
  private String email;
  
  public Athlete() {}

  public String getWodifyId() {
    return wodifyId;
  }

  public void setWodifyId(String wodifyId) {
    this.wodifyId = wodifyId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
      
      generator.writeStringField("uuid", athlete.getUuid().toString());
      generator.writeStringField("wodifyId", athlete.getWodifyId());
      generator.writeStringField("name", athlete.getName());
      generator.writeStringField("email", athlete.getEmail());
      
      generator.writeEndObject();
    }
  }
}
