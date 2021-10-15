package ru.itmo.stringEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@XmlRootElement
public class LabWork {

    @XmlElement
    private String id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой

    @XmlElement
    private Coordinates coordinates; //Поле не может быть null

    @XmlElement
    private String creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @XmlElement
    private String minimalPoint; //Значение поля должно быть больше 0

    @XmlElement
    private String maximumPoint; //Поле не может быть null, Значение поля должно быть больше 0

    @XmlElement
    private String personalQualitiesMaximum; //Поле может быть null, Значение поля должно быть больше 0

    @XmlElement
    private String difficulty; //Поле может быть null

    @XmlElement
    private Person author; //Поле не может быть null
}
