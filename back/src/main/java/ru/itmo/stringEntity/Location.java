package ru.itmo.stringEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@XmlRootElement
public class Location {

    @XmlElement
    private String id;

    @XmlElement
    private String x;

    @XmlElement
    private String y; //Поле не может быть null

    @XmlElement
    private String z;

    @XmlElement
    private String name; //Строка не может быть пустой, Поле не может быть null
}
