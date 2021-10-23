package ru.itmo.service;

import ru.itmo.DAO.DisciplineDAO;
import ru.itmo.converter.XMLConverter;
import ru.itmo.entity.Discipline;
import ru.itmo.utils.DisciplineResult;
import ru.itmo.utils.ServerResponse;
import ru.itmo.validator.Validator;
import ru.itmo.validator.ValidatorResult;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

public class DisciplineService {

    private XMLConverter xmlConverter;
    private DisciplineDAO dao;

    public DisciplineService() {
        xmlConverter = new XMLConverter();
        dao = new DisciplineDAO();
    }

    public Response getInfo(int code, String message) {
        ServerResponse serverResponse = new ServerResponse(message);
        return Response.status(code).entity(xmlConverter.toStr(serverResponse)).build();
    }

    public Response getDisciplines(){
        try {
            DisciplineResult disciplineResult = dao.getAllDisciplines();
            return Response.ok(disciplineResult).header("Access-Control-Allow-Origin", "*").build();
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public Response createDiscipline(ru.itmo.stringEntity.Discipline stringDiscipline){
        try {
            ValidatorResult validatorResult = Validator.validateDiscipline(stringDiscipline);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            Discipline discipline = stringDiscipline.toRealDiscipline();
            Long id = dao.createDiscipline(discipline);

            return Response.ok().header("Access-Control-Allow-Origin", "*").build();
        }catch (PersistenceException e){
            return getInfo(400, "Discipline has already existed");
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }
}
