package com.pmobrien.rest.services.impl;

import com.pmobrien.rest.neo.accessors.AthleteAccessor;
import com.pmobrien.rest.services.IAthleteService;
import javax.ws.rs.core.Response;

public class AthleteService implements IAthleteService {

  @Override
  public Response getAthletes() {
    return Response.ok(new AthleteAccessor().getAllAthletes()).build();
  }
}
