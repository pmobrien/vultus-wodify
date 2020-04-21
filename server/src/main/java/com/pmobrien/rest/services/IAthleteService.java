package com.pmobrien.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/athletes")
public interface IAthleteService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAthletes();
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{athleteUuid}/workouts")
  public Response getWorkoutsForAthlete(
      @PathParam("athleteUuid") String athleteUuid
  );
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{athleteUuid}/workouts/{workoutUuid}/performances")
  public Response getPerformancesForAthleteByWorkout(
      @PathParam("athleteUuid") String athleteUuid,
      @PathParam("workoutUuid") String workoutUuid
  );
}
