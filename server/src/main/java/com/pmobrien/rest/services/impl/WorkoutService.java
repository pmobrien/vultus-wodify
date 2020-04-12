package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.neo.accessors.WorkoutAccessor;
import com.pmobrien.rest.services.IWorkoutService;
import javax.ws.rs.core.Response;

public class WorkoutService implements IWorkoutService {

  @Override
  public Response getAllWorkouts() {
    return Response.ok(new WorkoutAccessor().getAllWorkouts()).build();
  }
}
