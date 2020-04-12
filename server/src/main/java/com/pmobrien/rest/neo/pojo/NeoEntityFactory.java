package com.pmobrien.rest.neo.pojo;

import java.util.UUID;

public class NeoEntityFactory {

  public static <T extends NeoEntity> T create(Class<T> type) {
    try {
      T instance = type.newInstance();
      instance.setUuid(UUID.randomUUID());
      
      return instance;
    } catch(ReflectiveOperationException ex) {
      throw new RuntimeException(String.format("Error creating NeoEntity of type %s.", type), ex);
    }
  }
}
