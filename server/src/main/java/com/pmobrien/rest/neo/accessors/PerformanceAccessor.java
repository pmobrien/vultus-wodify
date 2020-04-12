package com.pmobrien.rest.neo.accessors;

import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.pojo.Performance;

public class PerformanceAccessor {

  public PerformanceAccessor() {}
  
  public Performance getPerformance() {
    return Sessions.returningSessionOperation(
        session -> session.loadAll(Performance.class).toArray(new Performance[] {})[0]
    );
  }
}
