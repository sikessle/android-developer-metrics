package de.htwg.tqm.server.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api")
public final class TqmApi {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return "\n This is a simple REST API via HTTPServer";
    }

}
