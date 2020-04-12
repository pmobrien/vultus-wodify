package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.neo.accessors.PerformanceAccessor;
import com.pmobrien.rest.services.IPerformanceService;
import javax.ws.rs.core.Response;

public class PerformanceService implements IPerformanceService {

  @Override
  public Response getPerformances() {
    return Response.ok(new PerformanceAccessor().getAllPerformances()).build();
  }
}
