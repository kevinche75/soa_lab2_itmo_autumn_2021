package ru.itmo.validator;

import lombok.Getter;
import ru.itmo.converter.FieldConverter;
import ru.itmo.stringEntity.Discipline;

@Getter
public class Validator {

    public static ValidatorResult validateDiscipline(Discipline strDiscipline){
        ValidatorResult validatorResult = new ValidatorResult();
        FieldConverter.stringConvert(strDiscipline.getName(), "Discipline Name", validatorResult);
        return validatorResult;
    }
}
