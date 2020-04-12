package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Date;

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
  
  public Performance() {}

  public Boolean isPr() {
    return pr;
  }

  public void setPr(Boolean pr) {
    this.pr = pr;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
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
      
      generator.writeBooleanField("pr", performance.isPr());
      generator.writeStringField("comment", performance.getComment());
      generator.writeStringField("result", performance.getResult());
      generator.writeStringField("type", performance.getType().name());
      generator.writeStringField("date", performance.getDate().toString());
      
      generator.writeEndObject();
    }
  }
}
