package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.neo.accessors.PerformanceAccessor;
import com.pmobrien.rest.neo.accessors.WorkoutAccessor;
import com.pmobrien.rest.services.IWorkoutService;
import java.util.UUID;
import javax.ws.rs.core.Response;

public class WorkoutService implements IWorkoutService {

  @Override
  public Response getAllWorkouts() {
    return Response.ok(new WorkoutAccessor().getAllWorkouts()).build();
  }

  @Override
  public Response getPerformancesByWorkout(String workoutUuid) {
    return Response.ok(new PerformanceAccessor().getPerformancesByWorkoutUuid(UUID.fromString(workoutUuid))).build();
  }
}
