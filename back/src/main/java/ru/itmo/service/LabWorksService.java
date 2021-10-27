package ru.itmo.service;

import ru.itmo.DAO.LabWorksDAO;
import ru.itmo.converter.FieldConverter;
import ru.itmo.converter.XMLConverter;
import ru.itmo.entity.LabWork;
import ru.itmo.utils.LabWorkParams;
import ru.itmo.utils.LabWorksResult;
import ru.itmo.utils.ServerResponse;
import ru.itmo.validator.Validator;
import ru.itmo.validator.ValidatorResult;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.util.Optional;

public class LabWorksService {

    private XMLConverter xmlConverter;
    private LabWorksDAO dao;

    public LabWorksService() {
        xmlConverter = new XMLConverter();
        dao = new LabWorksDAO();
    }

    public Response getInfo(int code, String message) {
        ServerResponse serverResponse = new ServerResponse(message);
        return Response.status(code).entity(xmlConverter.toStr(serverResponse)).build();
    }

    public Response getAllLabWorks(LabWorkParams params) {
        if (!params.getValidatorResult().isStatus()) {
            return getInfo(400, params.getValidatorResult().getMessage());
        }
        try {
            LabWorksResult labWorksResult = dao.getAllLabWorks(params);
            return Response.ok(xmlConverter.toStr(labWorksResult)).build();
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public Response getLabWork(String str_id) {
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(str_id, "LabWork Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            Optional<LabWork> labWork = dao.getLabWork(id);
            if (labWork.isPresent()) {
                return Response.ok(xmlConverter.toStr(labWork.get())).build();
            } else {
                return getInfo(404, "No labWork with such id: " + id);
            }
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public Response getMinName() {
        try {
            LabWork lab = dao.getMinName();
            if (lab == null) {
                return getInfo( 404, "No labs");
            }
            return Response.ok(xmlConverter.toStr(xmlConverter.toStr(lab))).build();
        } catch (Exception e) {
            return getInfo( 500, "Server error, try again");
        }
    }

    public Response countPersonalQualitiesMaximum(String str_pqm) {
        ValidatorResult validatorResult = new ValidatorResult();
        Long pqm = FieldConverter.longConvert(str_pqm, "LabWork Personal Qualities Maximum", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            Long count = dao.countPQM(pqm);
            if (count != null) {
                return getInfo(200, "Count of labs with PQM: " + str_pqm + " is: " + count);
            } else {
                return getInfo(500, "Server error, try again");
            }
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }


    public Response createLabWork(String xmlStr) {
        try {
            ru.itmo.stringEntity.LabWork stringLabWork = xmlConverter.fromStr(xmlStr, ru.itmo.stringEntity.LabWork.class);
            ValidatorResult validatorResult = Validator.validateLabWork(stringLabWork);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            LabWork labWork = xmlConverter.fromStr(xmlStr, LabWork.class);
            Long id = dao.createLabWork(labWork);
            return Response.ok().build();
        } catch (JAXBException e) {
            return getInfo(400, "Unknown data structure");
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public Response updateLabWork(String str_id, String xmlStr) {
        try {
            ru.itmo.stringEntity.LabWork stringLabWorkUpdate = xmlConverter.fromStr(xmlStr, ru.itmo.stringEntity.LabWork.class);
            ValidatorResult validatorResult = Validator.validateLabWork(stringLabWorkUpdate);
            Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);
            Validator.validateCreationDate(stringLabWorkUpdate, validatorResult);
            Validator.validateId(stringLabWorkUpdate, validatorResult);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            LabWork labWorkUpdate = xmlConverter.fromStr(xmlStr, LabWork.class);
            Optional<LabWork> lab = dao.getLabWork(id);
            if (lab.isPresent()) {
                LabWork labWorkPresent = lab.get();
                labWorkPresent.update(labWorkUpdate);
                dao.updateLabWork(labWorkPresent);
                return Response.ok().build();
            } else
                return getInfo(404, "No LabWork with such id: " + labWorkUpdate.getId());
        } catch (JAXBException e) {
            return getInfo(400, "Can't understand data structure");
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public Response deleteLabWork(String str_id) {
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);

        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }

        boolean result = dao.deleteLabWork(id, validatorResult);
        if (!result)
            return getInfo(validatorResult.getCode(), validatorResult.getMessage());
        else
            return Response.ok().build();
    }
}
