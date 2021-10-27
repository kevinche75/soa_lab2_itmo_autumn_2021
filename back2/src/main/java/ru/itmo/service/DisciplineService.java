package ru.itmo.service;

import ru.itmo.DAO.DisciplineDAO;
import ru.itmo.converter.FieldConverter;
import ru.itmo.converter.XMLConverter;
import ru.itmo.entity.Discipline;
import ru.itmo.utils.DisciplineResult;
import ru.itmo.utils.LabWorksResult;
import ru.itmo.utils.ServerResponse;
import ru.itmo.validator.Validator;
import ru.itmo.validator.ValidatorResult;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class DisciplineService {

    private XMLConverter xmlConverter;
    private DisciplineDAO dao;

    public DisciplineService() {
        xmlConverter = new XMLConverter();
        dao = new DisciplineDAO();
    }

    public Response getInfo(int code, String message) {
        ServerResponse serverResponse = new ServerResponse(message);
        return Response
                .status(code)
                .entity(xmlConverter.toStr(serverResponse))
                .build();
    }

    public Response getDisciplines(){
        try {
            DisciplineResult disciplineResult = dao.getAllDisciplines();
            return Response.ok(disciplineResult)
                    .build();
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public Response getDiscipline(String stringId){
        try {
            ValidatorResult validatorResult = new ValidatorResult();
            Long id = FieldConverter.longConvert(stringId, "Discipline Id", validatorResult);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            Optional<Discipline> discipline = dao.getDiscipline(id);
            if (discipline.isPresent()){
                return Response.ok(xmlConverter.toStr(discipline.get().toUnrealDiscipline())).build();
            } else {
                return getInfo(400, String.format("No discipline with id: %s", stringId));
            }
        } catch (Exception e){
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

    public Response getDisciplineLabWorks(String stringDisciplineId){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(stringDisciplineId, "Discipline Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        LabWorksResult result = null;
        try {
            result = dao.getDisciplineLabWorks(id);
        } catch (EntityNotFoundException e){
            return getInfo(400, e.getMessage());
        }
        return Response.ok(xmlConverter.toStr(result)).build();
    }

    public Response addLabWorkToDiscipline(String stringDisciplineId, String stringLabWorkId){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(stringDisciplineId, "Discipline Id", validatorResult);
        Long labWorkId = FieldConverter.longConvert(stringLabWorkId, "LabWork Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            dao.addLabWork(id, labWorkId);
        } catch (EntityNotFoundException e){
            return getInfo(400, e.getMessage());
        } catch (EntityExistsException e){
            return getInfo(400, e.getMessage());
        }
        return getInfo(200, "Success");
    }

    public Response removeLabWorkFromDiscipline(String stringDisciplineId, String stringLabWorkId){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(stringDisciplineId, "Discipline Id", validatorResult);
        Long labWorkId = FieldConverter.longConvert(stringLabWorkId, "LabWork Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            dao.deleteLabWorkFromDiscipline(id, labWorkId);
        } catch (EntityNotFoundException e){
            return getInfo(400, e.getMessage());
        }
        return Response.ok().build();
    }
}