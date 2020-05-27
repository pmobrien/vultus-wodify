package com.pmobrien.rest.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmobrien.rest.services.IGraphQLService;
import graphql.GraphQL;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import javax.ws.rs.core.Response;

public class GraphQLService implements IGraphQLService {

  @Override
  public Response graphql() {
    RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
        .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
        .build();

    GraphQL build = GraphQL.newGraphQL(
        new SchemaGenerator().makeExecutableSchema(new SchemaParser().parse("type Query{hello: String}"), runtimeWiring)
    ).build();

    
    try {
      return Response.ok(new ObjectMapper().writeValueAsString(build.execute("{hello}").toSpecification())).build();
    } catch(JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
