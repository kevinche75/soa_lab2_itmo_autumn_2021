package ru.itmo.servlet;

import ru.itmo.service.DisciplineService;
import ru.itmo.stringEntity.Discipline;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/disciplines")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class DisciplineServlet {

    private DisciplineService service;

    public DisciplineServlet(){
        service = new DisciplineService();
    }

    @GET
    public Response getDisciplines(){
        return service.getDisciplines();
    }

    @GET
    @Path("/{id}")
    public Response getDiscipline(@PathParam("id")String disciplineId){
        return service.getDiscipline(disciplineId);
    }

    @GET
    @Path("/{id}/labworks")
    public Response getDisciplineLabWorks(@PathParam("id") String disciplineId){
        return service.getDisciplineLabWorks(disciplineId);
    }

    @POST
    public Response createDiscipline(Discipline strDiscipline){
        return service.createDiscipline(strDiscipline);
    }

    @DELETE
    @Path("/{discipline-id}/labwork/{labwork-id}/remove")
    public Response deleteLabWorkFromDiscipline(
            @PathParam("discipline-id") String disciplineId,
            @PathParam("labwork-id") String labWorkId){
        return service.removeLabWorkFromDiscipline(disciplineId, labWorkId);
    }

    @POST
    @Path("/{id}/{labwork-id}")
    public Response addLabWorkToDiscipline(@PathParam("id") String disciplineId, @PathParam("labwork-id") String labWorkId){
        return service.addLabWorkToDiscipline(disciplineId, labWorkId);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok()
                .build();
    }
}
