package com.pmobrien.rest.neo.accessors;

import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.Workout;

public class WorkoutAccessor {

  public WorkoutAccessor() {}
  
  public Workout getWorkout() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(Workout.class).toArray(new Workout[] {})[0]
    );
  }
}
