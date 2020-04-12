package com.pmobrien.rest.neo.accessors;

import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.Athlete;
import java.util.Collection;

public class AthleteAccessor {

  public AthleteAccessor() {}
  
  public Athlete getAthlete() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(Athlete.class).toArray(new Athlete[] {})[0]
    );
  }
  
  public Collection<Athlete> getAllAthletes() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(Athlete.class)
    );
  }
}
