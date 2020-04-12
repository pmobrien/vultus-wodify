package com.pmobrien.rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/athletes")
public interface IAthleteService {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAthletes();
}
