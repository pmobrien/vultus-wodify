package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.neo.accessors.AthleteAccessor;
import com.pmobrien.rest.services.IAthleteService;
import java.util.UUID;
import javax.ws.rs.core.Response;

public class AthleteService implements IAthleteService {

  @Override
  public Response getAthletes() {
    return Response.ok(new AthleteAccessor().getAllAthletes()).build();
  }

  @Override
  public Response getWorkoutsForAthlete(String athleteUuid) {
    return Response.ok(new AthleteAccessor().getWorkoutsForAthlete(UUID.fromString(athleteUuid))).build();
  }

  @Override
  public Response getPerformancesForAthleteByWorkout(String athleteUuid, String workoutUuid) {
    return Response.ok(
        new AthleteAccessor()
            .getPerformancesForAthleteByWorkout(
                UUID.fromString(athleteUuid),
                UUID.fromString(workoutUuid)
            )
    ).build();
  }
}
