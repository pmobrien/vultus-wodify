package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.neo.accessors.AthleteAccessor;
import com.pmobrien.rest.neo.accessors.PerformanceAccessor;
import com.pmobrien.rest.neo.pojo.HelloWorld;
import com.pmobrien.rest.services.IHelloWorldService;
import javax.ws.rs.core.Response;

public class HelloWorldService implements IHelloWorldService {

  @Override
  public Response helloWorld() {
    return Response.ok(
        new HelloWorld()
            .setHello(new AthleteAccessor().getAthlete().toJson())
            .setWorld(new PerformanceAccessor().getPerformance().toJson())
    ).build();
  }
}
