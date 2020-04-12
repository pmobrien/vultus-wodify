package com.pmobrien.rest.neo.accessors;

import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.Athlete;
import java.util.Collection;
import java.util.HashMap;

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
  
  private static class Queries {

    private static final String GET_ATHLETE_BY_NAME = new StringBuilder()
        .append("MATCH (athlete:Athlete { name: {name} })").append(System.lineSeparator())
        .append("RETURN athlete")
        .toString();
  }
}
