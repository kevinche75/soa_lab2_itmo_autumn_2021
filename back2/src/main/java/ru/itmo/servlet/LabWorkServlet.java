package ru.itmo.servlet;

import ru.itmo.service.LabWorkService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/labworks")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class LabWorkServlet {

    private LabWorkService service;

    public LabWorkServlet(){
        service = new LabWorkService();
    }

    @PUT
    @Path("/{labwork-id}/difficulty/increase/{steps-count}")
    public Response increaseLabWorkDifficulty(
            @PathParam("labwork-id") String labWorkId,
            @PathParam("steps-count") String steps){
        return service.increaseLabWorkDifficulty(labWorkId, steps);
    }
}
