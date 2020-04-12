package com.pmobrien.rest.neo.pojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class HelloWorld extends NeoEntity {

  private String hello = "hello";
  private String world = "world";
  
  public HelloWorld() {}

  public String getHello() {
    return hello;
  }

  public HelloWorld setHello(String hello) {
    this.hello = hello;
    return this;
  }

  public String getWorld() {
    return world;
  }

  public HelloWorld setWorld(String world) {
    this.world = world;
    return this;
  }
  
  public static class Serializer extends StdSerializer<HelloWorld> {

    public Serializer() {
      this(null);
    }

    public Serializer(Class<HelloWorld> type) {
      super(type);
    }

    @Override
    public void serialize(HelloWorld entity, JsonGenerator generator, SerializerProvider provider) throws IOException {
      generator.writeStartObject();
    
      generator.writeStringField("hello", entity.getHello());
      generator.writeStringField("world", entity.getWorld());
      
      generator.writeEndObject();
    }
  }
}
