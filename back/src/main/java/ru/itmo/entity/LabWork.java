package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.itmo.converter.FieldConverter;
import ru.itmo.converter.XMLLocalDateTimeAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@XmlRootElement
@Table(name = "labwork")
public class LabWork {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlElement
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @XmlElement
    @NotBlank
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id")
    @XmlElement
    private Coordinates coordinates; //Поле не может быть null

    @NotNull
    @XmlElement
    @XmlJavaTypeAdapter(XMLLocalDateTimeAdapter.class)
    private java.time.LocalDateTime creationDate = java.time.LocalDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @NotNull
    @Positive
    @XmlElement
    private Float minimalPoint; //Значение поля должно быть больше 0

    @NotNull
    @Positive
    @XmlElement
    private Float maximumPoint; //Поле не может быть null, Значение поля должно быть больше 0

    @Positive
    @XmlElement
    private Long personalQualitiesMaximum; //Поле может быть null, Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    @XmlElement
    private Difficulty difficulty; //Поле может быть null

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    @XmlElement
    private Person author; //Поле не может быть null

    public static List<String> getAllFields(){
        Field[] fields = LabWork.class.getDeclaredFields();
        List<String> fieldList = Arrays
                .stream(fields)
                .map(Field::getName)
                .filter(field -> !field.equals("coordinates"))
                .collect(Collectors.toList());
        Arrays
                .stream(Coordinates.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> FieldConverter.addPrefixFieldConvert("coordinates", field.getName()))
                .forEach(fieldList::add);
        Arrays
                .stream(Person.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("location"))
                .map(field -> FieldConverter.addPrefixFieldConvert("author", field.getName()))
                .forEach(fieldList::add);
        Arrays
                .stream(Location.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> FieldConverter.addPrefixFieldConvert("location", field.getName()))
                .forEach(fieldList::add);
        return fieldList;
    }

    public void update(LabWork labWorkUpdate){
        this.name = labWorkUpdate.getName();
        this.coordinates.update(labWorkUpdate.getCoordinates());
        this.creationDate = labWorkUpdate.getCreationDate();
        this.minimalPoint = labWorkUpdate.getMinimalPoint();
        this.maximumPoint = labWorkUpdate.getMaximumPoint();
        this.personalQualitiesMaximum = labWorkUpdate.getPersonalQualitiesMaximum();
        this.difficulty = labWorkUpdate.getDifficulty();
        this.author.update(labWorkUpdate.getAuthor());
    }
}