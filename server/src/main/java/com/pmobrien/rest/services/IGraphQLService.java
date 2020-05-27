package com.pmobrien.rest.services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/graphql")
public interface IGraphQLService {

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response graphql();
}
