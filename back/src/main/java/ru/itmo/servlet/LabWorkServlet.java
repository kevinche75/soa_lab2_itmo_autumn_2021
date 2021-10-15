package ru.itmo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.service.LabWorksService;
import ru.itmo.utils.LabWorkParams;

import java.io.IOException;

@WebServlet("/labworks/*")
public class LabWorkServlet extends HttpServlet {

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
    private static final String COUNT_PERSONAL_QUALITIES_MAXIMUM_FLAG = "count_personal_maximum";
    private static final String LESS_MAXIMUM_POINT_FLAG = "less_maximum_point";

    private LabWorksService service;

    private LabWorkParams getLabWorksParams(HttpServletRequest request){
        LabWorkParams params = new LabWorkParams();
        params.setLabWorkParams(
                request.getParameter(NAME_PARAM),
                request.getParameter(CREATION_DATE_PARAM),
                request.getParameter(MINIMAL_POINT_PARAM),
                request.getParameter(MAXIMAL_POINT_PARAM),
                request.getParameter(PERSONAL_QUALITIES_MAXIMUM_PARAM),
                request.getParameter(DIFFICULTY_PARAM),
                request.getParameter(COORDINATES_X_PARAM),
                request.getParameter(COORDINATES_Y_PARAM),
                request.getParameter(PERSON_NAME_PARAM),
                request.getParameter(PERSON_WEIGHT_PARAM),
                request.getParameter(LOCATION_X_PARAM),
                request.getParameter(LOCATION_Y_PARAM),
                request.getParameter(LOCATION_Z_PARAM),
                request.getParameter(LOCATION_NAME_PARAM),
                request.getParameter(PAGE_IDX_PARAM),
                request.getParameter(PAGE_SIZE_PARAM),
                request.getParameter(SORT_FIELD_PARAM)
        );
        return params;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        service = new LabWorksService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null){
            LabWorkParams filterParams = getLabWorksParams(req);
            service.getAllLabWorks(filterParams, resp);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length > 1) {
                switch (parts[1]) {
                    case LESS_MAXIMUM_POINT_FLAG:
                        LabWorkParams filterParams = getLabWorksParams(req);
                        filterParams.setLessMaximalPointFlag(true);
                        service.getAllLabWorks(filterParams, resp);
                        break;
                    case MINIMAL_NAME_FLAG:
                        service.getMinName(resp);
                        break;
                    case COUNT_PERSONAL_QUALITIES_MAXIMUM_FLAG:
                        service.countPersonalQualitiesMaximum(parts[2], resp);
                        break;
                    default:
                        service.getLabWork(parts[1], resp);
                        break;
                }
            } else {
                service.getInfo(resp, 400, "Unknown query");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        service.createLabWork(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/ml");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String[] parts = pathInfo.split("/");
            if (parts.length > 1) {
                service.updateLabWork(parts[1], req, resp);
            } else {
                service.getInfo(resp, 400, "Unknown query");
            }
        } else {
            service.getInfo(resp, 400, "Unknown query");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String[] parts = pathInfo.split("/");
            if (parts.length > 1) {
                service.deleteLabWork(parts[1], resp);
            } else {
                service.getInfo(resp, 400, "Unknown query");
            }
        } else {
            service.getInfo(resp, 400, "Unknown query");
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        resp.setStatus(200);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }
}
