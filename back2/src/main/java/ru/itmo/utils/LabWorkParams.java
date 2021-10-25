package ru.itmo.utils;

import lombok.Getter;
import lombok.Setter;
import ru.itmo.converter.FieldConverter;
import ru.itmo.entity.*;
import ru.itmo.validator.ValidatorResult;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class LabWorkParams {

    String name;
    LocalDateTime creationDate;
    Float minimalPoint;
    @Setter
    Float maximumPoint;
    Long personalQualitiesMaximum;
    Difficulty difficulty;
    Integer coordinatesX;
    Double coordinatesY;
    String authorName;
    Float authorWeight;
    Float locationX;
    Integer locationY;
    Integer locationZ;
    String locationName;
    Integer pageIdx;
    Integer pageSize;
    String sortField;
    @Setter
    boolean lessMaximalPointFlag = false;

    private ValidatorResult validatorResult;

    public LabWorkParams(){
        validatorResult = new ValidatorResult();
    }

    public final static String DATE_PATTERN = "dd.MM.yyyy";

    public void setLabWorkParams(
            String name,
            String creationDate,
            String minimalPoint,
            String maximumPoint,
            String personalQualitiesMaximum,
            String difficulty,
            String coordinatesX,
            String coordinatesY,
            String authorName,
            String authorWeight,
            String locationX,
            String locationY,
            String locationZ,
            String locationName,
            String pageIdx,
            String pageSize,
            String sortField
    ) {
        this.name = name;
        this.creationDate = FieldConverter.localFilterDateTimeConvert(creationDate, "LabWork Creation Date", DATE_PATTERN, validatorResult);
        this.minimalPoint = FieldConverter.floatFilterConvert(minimalPoint, "LabWork MinimalPoint", validatorResult);
        this.maximumPoint = FieldConverter.floatFilterConvert(maximumPoint, "LabWork MaximalPoint", validatorResult);
        this.personalQualitiesMaximum = FieldConverter.longFilterConvert(personalQualitiesMaximum, "LabWork PersonalQualitiesMaximal", validatorResult);
        this.difficulty = FieldConverter.difficultyFilterConvert(difficulty, "LabWork Difficulty", validatorResult);
        this.coordinatesX = FieldConverter.intFilterConvert(coordinatesX, "Coordinates X", validatorResult);
        this.coordinatesY = FieldConverter.doubleFilterConvert(coordinatesY, "Coordinates Y", validatorResult);
        this.authorName = authorName;
        this.authorWeight = FieldConverter.floatFilterConvert(authorWeight, "Author Weight", validatorResult);
        this.locationX = FieldConverter.floatFilterConvert(locationX, "Location X", validatorResult);
        this.locationY = FieldConverter.intFilterConvert(locationY, "Location Y", validatorResult);
        this.locationZ = FieldConverter.intFilterConvert(locationZ, "Location Z", validatorResult);
        this.locationName = locationName;
        this.pageIdx = Math.max(FieldConverter.intConvert(pageIdx, 1), 1);
        this.pageSize = Math.max(FieldConverter.intConvert(pageSize, 10), 1);
        this.sortField = FieldConverter.sortFieldFilterConvert(sortField, LabWork.getAllFields(), validatorResult);
    }

    public void setMaximumPoint(String maximumPoint) {
        this.maximumPoint = FieldConverter.floatFilterConvert(maximumPoint, "LabWork MaximalPoint", validatorResult);;
    }

    public List<Predicate> getPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<LabWork> root,
            Join<LabWork, Coordinates> coordinatesJoin,
            Join<LabWork, Person> personJoin,
            Join<Person, Location> locationJoin
    ){
        List<Predicate> predicates = new ArrayList<>();
        if (this.name != null){
            predicates.add(criteriaBuilder.like(root.get("name"), FieldConverter.stringLikeConvert(this.name)));
        }
        if (this.creationDate != null){
            predicates.add(criteriaBuilder.equal(root.get("creationDate"), this.creationDate));
        }
        if (this.minimalPoint != null){
            predicates.add(criteriaBuilder.equal(root.get("minimalPoint"), this.minimalPoint));
        }
        if (this.maximumPoint != null){
            if(this.lessMaximalPointFlag){
                predicates.add(criteriaBuilder.lessThan(root.get("maximumPoint"), this.maximumPoint));
            } else {
                predicates.add(criteriaBuilder.equal(root.get("maximumPoint"), this.maximumPoint));
            }
        }
        if (this.personalQualitiesMaximum != null){
            predicates.add(criteriaBuilder.equal(root.get("personalQualitiesMaximum"), this.personalQualitiesMaximum));
        }
        if (this.difficulty != null){
            predicates.add(criteriaBuilder.equal(root.get("difficulty"), this.difficulty));
        }
        if (this.coordinatesX != null){
            predicates.add(criteriaBuilder.equal(coordinatesJoin.get("x"), this.coordinatesX));
        }
        if (this.coordinatesY != null){
            predicates.add(criteriaBuilder.equal(coordinatesJoin.get("y"), this.coordinatesY));
        }
        if (this.authorName != null){
            predicates.add(criteriaBuilder.like(personJoin.get("name"), FieldConverter.stringLikeConvert(this.authorName)));
        }
        if (this.authorWeight != null){
            predicates.add(criteriaBuilder.equal(personJoin.get("weight"), this.authorWeight));
        }
        if (this.locationX != null){
            predicates.add(criteriaBuilder.equal(locationJoin.get("x"), this.locationX));
        }
        if (this.locationY != null){
            predicates.add(criteriaBuilder.equal(locationJoin.get("y"), this.locationY));
        }
        if (this.locationZ != null){
            predicates.add(criteriaBuilder.equal(locationJoin.get("z"), this.locationZ));
        }
        if (this.locationName != null){
            predicates.add(criteriaBuilder.like(locationJoin.get("name"), FieldConverter.stringLikeConvert(this.locationName)));
        }
        return predicates;
    }
}