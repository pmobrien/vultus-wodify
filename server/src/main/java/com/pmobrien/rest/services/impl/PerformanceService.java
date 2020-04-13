package com.pmobrien.rest.services.impl;

import com.google.common.base.Strings;
import com.pmobrien.rest.neo.accessors.PerformanceAccessor;
import com.pmobrien.rest.services.IPerformanceService;
import java.util.UUID;
import javax.ws.rs.core.Response;

public class PerformanceService implements IPerformanceService {

  @Override
  public Response getPerformances(String workoutUuid) {
    if(Strings.isNullOrEmpty(workoutUuid)) {
      return Response.ok(new PerformanceAccessor().getAllPerformances()).build();
    } else {
      return Response.ok(new PerformanceAccessor().getPerformancesByWorkoutUuid(UUID.fromString(workoutUuid))).build();
    }
  }
}
