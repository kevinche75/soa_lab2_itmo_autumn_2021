package ru.itmo.servlet;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/labwork")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class LabWorkServlet {

    @GET
    @Path("/{labwork-id}/difficulty/increase/{steps-count}")
    public Response increaseLabWorkDifficulty(
            @PathParam("labwork-id") String labWorkId,
            @PathParam("steps-count") String steps){

    }
}
