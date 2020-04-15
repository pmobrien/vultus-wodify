package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.services.IVersionService;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.ws.rs.core.Response;

public class VersionService implements IVersionService {

  @Override
  public Response getVersion() {
    return Response.ok(new Version()).build();
  }
  
  public static class Version {

    private final String VERSION;
    
    public Version() {
      String version;
      try {
        version = new Manifest(((URLClassLoader)this.getClass().getClassLoader()).findResource("META-INF/MANIFEST.MF").openStream())
            .getMainAttributes()
            .getValue(Attributes.Name.IMPLEMENTATION_VERSION);
      } catch(IOException ex) {
        version = "unknown";
      }
      
      VERSION = version;
    }
    
    public String getVersion() {
      return VERSION;
    }
  }
}
