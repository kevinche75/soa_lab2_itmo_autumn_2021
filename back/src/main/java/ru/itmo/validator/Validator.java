package ru.itmo.validator;

import lombok.Getter;
import ru.itmo.converter.FieldConverter;
import ru.itmo.utils.LabWorkParams;

@Getter
public class Validator {

    public static ValidatorResult validateLabWork(ru.itmo.stringEntity.LabWork stringLabWork){
        ValidatorResult validatorResult = new ValidatorResult();

        FieldConverter.stringConvert(stringLabWork.getName(), "LabWork Name", validatorResult);
        Float labWorkMinimalPoint = FieldConverter.floatConvert(stringLabWork.getMinimalPoint(), "LabWork MinimalPoint", validatorResult);
        if(labWorkMinimalPoint != null && labWorkMinimalPoint < 0){
            validatorResult.addMessage("LabWork MinimalPoint must be more than 0");
        }
        Float labWorkMaximumPoint = FieldConverter.floatConvert(stringLabWork.getMaximumPoint(), "LabWork MaximalPoint", validatorResult);
        if(labWorkMaximumPoint != null && labWorkMaximumPoint < 0){
            validatorResult.addMessage("LabWork MaximumPoint must be more than 0");
        }
        Long labWorkPersonalQualitiesMaximum = FieldConverter.longConvert(stringLabWork.getPersonalQualitiesMaximum(), "LabWork PersonalQualitiesMaximum", validatorResult);
        if(labWorkPersonalQualitiesMaximum != null && labWorkPersonalQualitiesMaximum < 0){
            validatorResult.addMessage("LabWork PersonalQualitiesMaximum must be more than 0");
        }
        FieldConverter.difficultyConvert(stringLabWork.getDifficulty(), "LabWork Difficulty", validatorResult);
        if (stringLabWork.getCoordinates() != null){
            FieldConverter.intConvert(stringLabWork.getCoordinates().getX(), "Coordinates X", validatorResult);
            FieldConverter.doubleConvert(stringLabWork.getCoordinates().getY(), "Coordinates Y", validatorResult);
        } else {
            validatorResult.addMessage("Coordinates fields are not specified");
        }

        if (stringLabWork.getAuthor() != null){
            FieldConverter.stringConvert(stringLabWork.getAuthor().getName(), "Author Name", validatorResult);
            Float authorWeight = FieldConverter.floatConvert(stringLabWork.getAuthor().getWeight(), "Author Weight", validatorResult);
            if(authorWeight != null && authorWeight < 0){
                validatorResult.addMessage("Author Weight must be more than 0");
            }
            if (stringLabWork.getAuthor().getLocation() != null) {
                FieldConverter.floatConvert(stringLabWork.getAuthor().getLocation().getX(), "Location X", validatorResult);
                FieldConverter.intConvert(stringLabWork.getAuthor().getLocation().getY(), "Location Y", validatorResult);
                FieldConverter.intConvert(stringLabWork.getAuthor().getLocation().getZ(), "Location Z", validatorResult);
                FieldConverter.stringConvert(stringLabWork.getAuthor().getLocation().getName(), "Location Name", validatorResult);
            } else {
                validatorResult.addMessage("Location fields are not specified");
            }
        } else {
            validatorResult.addMessage("Author fields are not specified");
        }

        return validatorResult;
    }

    public static void validateCreationDate(ru.itmo.stringEntity.LabWork stringLabWork, ValidatorResult validatorResult){
        FieldConverter.localDateTimeConvert(stringLabWork.getCreationDate(), "LabWork CreationDate", LabWorkParams.DATE_PATTERN, validatorResult);
    }

    public static void validateId(ru.itmo.stringEntity.LabWork stringLabWork, ValidatorResult validatorResult){
        Long labWorkId = FieldConverter.longConvert(stringLabWork.getId(), "LabWork Id", validatorResult);
        if(labWorkId != null && labWorkId <= 0){
            validatorResult.addMessage("LabWork id must be more than 0");
        }
    }
}
