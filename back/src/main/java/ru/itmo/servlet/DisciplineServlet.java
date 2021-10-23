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

    @POST
    public Response createDiscipline(Discipline strDiscipline){
        return service.createDiscipline(strDiscipline);
    }
}
