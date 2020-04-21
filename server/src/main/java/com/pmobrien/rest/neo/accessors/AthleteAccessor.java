package com.pmobrien.rest.neo.accessors;

import com.google.common.collect.Lists;
import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.Athlete;
import com.pmobrien.rest.neo.pojo.Performance;
import com.pmobrien.rest.neo.pojo.Workout;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class AthleteAccessor {

  public AthleteAccessor() {}
  
  public Collection<Athlete> getAllAthletes() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(Athlete.class)
    );
  }
  
  public Athlete getAthleteByName(String name) {
    return Sessions.returningSessionOperation(session -> {
      return session.queryForObject(
          Athlete.class,
          Queries.GET_ATHLETE_BY_NAME,
          new HashMap<String, String>() {{
            put("name", name);
          }}
        );
    });
  }
  
  public Collection<Workout> getWorkoutsForAthlete(UUID athleteUuid) {
    return Sessions.returningSessionOperation(session -> {
      return Lists.newArrayList(
          session.query(
              Workout.class,
              Queries.GET_WORKOUTS_FOR_ATHLETE,
              new HashMap<String, Object>() {{
                put("uuid", athleteUuid);
              }}
          )
      );
    });
  }
  
  public Collection<Performance> getPerformancesForAthleteByWorkout(UUID athleteUuid, UUID workoutUuid) {
    return Sessions.returningSessionOperation(session -> {
      return Lists.newArrayList(
          session.query(
              Performance.class,
              Queries.GET_PERFORMANCES_FOR_ATHLETE_BY_WORKOUT,
              new HashMap<String, Object>() {{
                put("athleteUuid", athleteUuid);
                put("workoutUuid", workoutUuid);
              }}
          )
      );
    });
  }
  
  private static class Queries {

    private static final String GET_ATHLETE_BY_NAME = new StringBuilder()
        .append("MATCH (athlete:Athlete { name: {name} })").append(System.lineSeparator())
        .append("RETURN athlete")
        .toString();
    
    private static final String GET_WORKOUTS_FOR_ATHLETE = new StringBuilder()
        .append("MATCH (athlete:Athlete { uuid: {uuid} })").append(System.lineSeparator())
        .append("MATCH (performance:Performance)<-[COMPLETED]-(athlete)").append(System.lineSeparator())
        .append("MATCH (performance)-[INSTANCE_OF]->(workout:Workout)").append(System.lineSeparator())
        .append("RETURN DISTINCT workout")
        .toString();
    
    private static final String GET_PERFORMANCES_FOR_ATHLETE_BY_WORKOUT = new StringBuilder()
        .append("MATCH (athlete:Athlete { uuid: {athleteUuid} })").append(System.lineSeparator())
        .append("MATCH (performance:Performance)<-[completed:COMPLETED]-(athlete)").append(System.lineSeparator())
        .append("MATCH (workout:Workout { uuid: {workoutUuid } })<-[instanceOf:INSTANCE_OF]-(performance)").append(System.lineSeparator())
        .append("RETURN performance, instanceOf, workout, completed, athlete")
        .toString();
  }
}
