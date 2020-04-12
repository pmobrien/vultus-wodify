package com.pmobrien.rest.neo.accessors;

import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.Workout;
import java.util.Collection;
import java.util.HashMap;

public class WorkoutAccessor {

  public WorkoutAccessor() {}
  
  public Workout getWorkoutByName(String name) {
    return Sessions.returningSessionOperation(
        session -> session.queryForObject(
            Workout.class,
            Queries.GET_WORKOUT_BY_NAME,
            new HashMap<String, String>() {{
              put("name", name);
            }}
        )
    );
  }
  
  public Collection<Workout> getAllWorkouts() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(Workout.class)
    );
  }
  
  private static class Queries {

    private static final String GET_WORKOUT_BY_NAME = new StringBuilder()
        .append("MATCH (workout:Workout { name: {name} })").append(System.lineSeparator())
        .append("RETURN workout")
        .toString();
  }
}
