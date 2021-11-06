package ru.itmo.servlet;

import ru.itmo.service.LabWorksService;
import ru.itmo.utils.LabWorkParams;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/labworks")
public class LabWorkServlet {

    private static final String NAME_PARAM = "name";
    private static final String CREATION_DATE_PARAM = "creationDate";
    private static final String MINIMAL_POINT_PARAM = "minimalPoint";
    private static final String MAXIMAL_POINT_PARAM = "maximumPoint";
    private static final String PERSONAL_QUALITIES_MAXIMUM_PARAM = "personalQualitiesMaximum";
    private static final String DIFFICULTY_PARAM = "difficulty";
    private static final String COORDINATES_X_PARAM = "coordinatesX";
    private static final String COORDINATES_Y_PARAM = "coordinatesY";

    private static final String PAGE_IDX_PARAM = "pageIdx";
    private static final String PAGE_SIZE_PARAM = "pageSize";
    private static final String SORT_FIELD_PARAM = "sortField";

    private static final String PERSON_NAME_PARAM = "authorName";
    private static final String PERSON_WEIGHT_PARAM = "authorWeight";

    private static final String LOCATION_X_PARAM = "locationX";
    private static final String LOCATION_Y_PARAM = "locationY";
    private static final String LOCATION_Z_PARAM = "locationZ";
    private static final String LOCATION_NAME_PARAM = "locationName";

    private static final String MINIMAL_NAME_FLAG = "min_name";
    private static final String COUNT_PERSONAL_QUALITIES_MAXIMUM_FLAG = "count_personal_maximum/{personal_qualities_maximum}";
    private static final String LESS_MAXIMUM_POINT_FLAG = "less_maximum_point/{maximum_point}";

    private LabWorksService service;
    @Context
    ServletContext servletContext;

    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    private LabWorkParams getLabWorksParams(MultivaluedMap<String, String> map){
        LabWorkParams params = new LabWorkParams();
        params.setLabWorkParams(
                map.getFirst(NAME_PARAM),
                map.getFirst(CREATION_DATE_PARAM),
                map.getFirst(MINIMAL_POINT_PARAM),
                map.getFirst(MAXIMAL_POINT_PARAM),
                map.getFirst(PERSONAL_QUALITIES_MAXIMUM_PARAM),
                map.getFirst(DIFFICULTY_PARAM),
                map.getFirst(COORDINATES_X_PARAM),
                map.getFirst(COORDINATES_Y_PARAM),
                map.getFirst(PERSON_NAME_PARAM),
                map.getFirst(PERSON_WEIGHT_PARAM),
                map.getFirst(LOCATION_X_PARAM),
                map.getFirst(LOCATION_Y_PARAM),
                map.getFirst(LOCATION_Z_PARAM),
                map.getFirst(LOCATION_NAME_PARAM),
                map.getFirst(PAGE_IDX_PARAM),
                map.getFirst(PAGE_SIZE_PARAM),
                map.getFirst(SORT_FIELD_PARAM)
        );
        return params;
    }

    public LabWorkServlet() {
        service = new LabWorksService();
    }

    @GET
    public Response getLabWorks(@Context UriInfo ui) {
        MultivaluedMap<String, String> map = ui.getQueryParameters();
        LabWorkParams filterParams = getLabWorksParams(map);
        return service.getAllLabWorks(filterParams);
    }

    @GET
    @Path("/{id}")
    public Response getLabWork(@PathParam("id") String id){
        return service.getLabWork(id);
    }

    @GET
    @Path(LESS_MAXIMUM_POINT_FLAG)
    public Response getLessMaximumPointFlag(@Context UriInfo ui, @PathParam("maximum_point") String maximum_point){
        MultivaluedMap<String, String> map = ui.getQueryParameters();
        LabWorkParams filterParams = getLabWorksParams(map);
        filterParams.setLessMaximalPointFlag(true);
        filterParams.setMaximumPoint(maximum_point);
        return service.getAllLabWorks(filterParams);
    }

    @GET
    @Path(MINIMAL_NAME_FLAG)
    public Response getMinimalNameLabWork(){
        return service.getMinName();
    }

    @GET
    @Path(COUNT_PERSONAL_QUALITIES_MAXIMUM_FLAG)
    public Response countPersonalQualitiesMaximumLabWorks(@PathParam("personal_qualities_maximum") String personal_qualities_maximum){
        return service.countPersonalQualitiesMaximum(personal_qualities_maximum);
    }

    @POST
    public Response createLabWork(String labWork){
        return service.createLabWork(labWork);
    }

    @PUT
    @Path("/{id}")
    public Response changeLabWork(@PathParam("id") String id, String labWork){
        return service.updateLabWork(id, labWork);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLabWork(@PathParam("id") String id){
        return service.deleteLabWork(id);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok()
                .build();
    }
}
