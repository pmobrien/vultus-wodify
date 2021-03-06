package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Date;
import org.neo4j.ogm.annotation.Relationship;

public class Performance extends NeoEntity {

  public enum Type {
    SCALED,
    RX,
    RX_PLUS;
  }
  
  private Boolean pr;
  private String comment;
  private String result;
  private Type type;
  private Date date;
  
  @Relationship(type = "COMPLETED", direction = Relationship.INCOMING)
  private Athlete athlete;
  
  @Relationship(type = "INSTANCE_OF", direction = Relationship.OUTGOING)
  private Workout workout;
  
  public Performance() {}

  public Boolean isPr() {
    return pr;
  }

  public Performance setPr(Boolean pr) {
    this.pr = pr;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public Performance setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public String getResult() {
    return result;
  }

  public Performance setResult(String result) {
    this.result = result;
    return this;
  }

  public Type getType() {
    return type;
  }

  public Performance setType(Type type) {
    this.type = type;
    return this;
  }

  public Date getDate() {
    return date;
  }

  public Performance setDate(Date date) {
    this.date = date;
    return this;
  }

  public Athlete getAthlete() {
    return athlete;
  }

  public Performance setAthlete(Athlete athlete) {
    this.athlete = athlete;
    return this;
  }

  public Workout getWorkout() {
    return workout;
  }

  public Performance setWorkout(Workout workout) {
    this.workout = workout;
    return this;
  }
  
  public static class Serializer extends StdSerializer<Performance> {

    public Serializer() {
      this(null);
    }

    public Serializer(Class<Performance> type) {
      super(type);
    }

    @Override
    public void serialize(Performance performance, JsonGenerator generator, SerializerProvider provider) throws IOException {
      generator.writeStartObject();
      
      writeFields(performance, generator);
      
      if(performance.getAthlete() != null) {
        generator.writeFieldName("athlete");
        generator.writeStartObject();
        new Athlete.Serializer().writeFields(performance.getAthlete(), generator);
        generator.writeEndObject();
      }
      
      if(performance.getWorkout() != null) {
        generator.writeFieldName("workout");
        generator.writeStartObject();
        new Workout.Serializer().writeFields(performance.getWorkout(), generator);
        generator.writeEndObject();
      }
      
      generator.writeEndObject();
    }
    
    protected void writeFields(Performance performance, JsonGenerator generator) throws IOException {
      generator.writeStringField("uuid", performance.getUuid().toString());
      generator.writeBooleanField("pr", performance.isPr());
      generator.writeStringField("comment", performance.getComment());
      generator.writeStringField("result", performance.getResult());
      generator.writeStringField("type", performance.getType().name());
      generator.writeStringField("date", performance.getDate().toString());
    }
  }
}
