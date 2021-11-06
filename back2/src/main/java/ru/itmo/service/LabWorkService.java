package ru.itmo.service;

import ru.itmo.DAO.DisciplineDAO;
import ru.itmo.converter.FieldConverter;
import ru.itmo.converter.XMLConverter;
import ru.itmo.entity.Difficulty;
import ru.itmo.stringEntity.LabWork;
import ru.itmo.utils.ServerResponse;
import ru.itmo.validator.ValidatorResult;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LabWorkService {

    private DisciplineDAO dao;
    private XMLConverter xmlConverter;
    private Map<Integer, String> difficultyMap;

    public LabWorkService(){
        dao = new DisciplineDAO();
        xmlConverter = new XMLConverter();
        difficultyMap = Arrays
                .stream(Difficulty.values())
                .collect(Collectors.toMap(Difficulty::ordinal, Difficulty::toString));
    }

    public Response getInfo(int code, String message) {
        ServerResponse serverResponse = new ServerResponse(message);
        return Response.status(code).entity(xmlConverter.toStr(serverResponse)).build();
    }

    public Response increaseLabWorkDifficulty(String id, String stringSteps) {
        try {
            ValidatorResult validatorResult = new ValidatorResult();
            FieldConverter.longConvert(id, "Discipline Id", validatorResult);
            Integer steps = FieldConverter.intConvert(stringSteps, "Steps", validatorResult);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            Response labWorkResponse = dao.getTarget().path(id).request().accept(MediaType.APPLICATION_XML).get();
            if (labWorkResponse.getStatus() != 200){
                ServerResponse serverResponse = labWorkResponse.readEntity(ServerResponse.class);
                return getInfo(labWorkResponse.getStatus(), serverResponse.getMessage());
            }
            String strLab = labWorkResponse.readEntity(String.class);
            LabWork labWork = xmlConverter.fromStr(strLab, LabWork.class);
            int difficulty = Difficulty.valueOf(labWork.getDifficulty()).ordinal();
            int resultDifficulty = difficulty + steps;
            if (!difficultyMap.containsKey(resultDifficulty)){
                return getInfo(400, "Increased Difficulty out of bounds");
            }
            labWork.setDifficulty(difficultyMap.get(resultDifficulty));
            Response changeDifficultyResponse = dao.getTarget().path(id).request().accept(MediaType.APPLICATION_XML).put(Entity.entity(labWork, MediaType.APPLICATION_XML));
            if (changeDifficultyResponse.getStatus() != 200){
                ServerResponse serverResponse = changeDifficultyResponse.readEntity(ServerResponse.class);
                return getInfo(changeDifficultyResponse.getStatus(), serverResponse.getMessage());
            }
        } catch (Exception e){
            e.printStackTrace();
            return getInfo(500, "Server error, try again");
        }
        return getInfo(200, "Success");
    }
}
