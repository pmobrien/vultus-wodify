package com.pmobrien.rest.neo.accessors;

import com.google.common.collect.Lists;
import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.Performance;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PerformanceAccessor {

  public PerformanceAccessor() {}
  
  public Collection<Performance> getAllPerformances() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(Performance.class)
    );
  }
  
  public Collection<Performance> getPerformancesByWorkoutUuid(UUID workoutUuid) {
    return Sessions.returningSessionOperation(
        session -> Lists.newArrayList(
            session.query(
                Performance.class,
                Queries.GET_PERFORMANCE_BY_WORKOUT_UUID,
                new HashMap<String, Object>() {{
                  put("uuid", workoutUuid);
                }}
            )
        )
    );
  }
  
  private static class Queries {

    private static final String GET_PERFORMANCE_BY_WORKOUT_UUID = new StringBuilder()
        .append("MATCH (performance:Performance)").append(System.lineSeparator())
        .append("MATCH (performance)-[instance_of:INSTANCE_OF]->(workout:Workout { uuid: {uuid} })").append(System.lineSeparator())
        .append("OPTIONAL MATCH (performance)<-[completed:COMPLETED]-(athlete:Athlete)").append(System.lineSeparator())
        .append("RETURN performance, instance_of, workout, completed, athlete")
        .toString();
  }
}
