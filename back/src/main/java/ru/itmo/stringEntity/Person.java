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
public class Person {

    @XmlElement
    private String id;

    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой

    @XmlElement
    private String weight; //Поле может быть null, Значение поля должно быть больше 0

    @XmlElement
    private Location location; //Поле не может быть null
}
