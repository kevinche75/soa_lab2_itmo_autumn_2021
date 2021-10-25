package ru.itmo.servlet;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/discipline")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class DisciplineServlet {

    @GET
    @Path("/{discipline-id}/labwork/{labwork-id}/remove")
    public Response deleteLabWorkFromDiscipline(
            @PathParam("discipline-id") String disciplineId,
            @PathParam("labwork-id") String labWorkId){

    }
}
