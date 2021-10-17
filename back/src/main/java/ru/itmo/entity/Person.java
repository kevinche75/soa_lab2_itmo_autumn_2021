package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@XmlRootElement
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlElement
    private Long id;

    @NotBlank
    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @Positive
    @XmlElement
    private Float weight; //Поле может быть null, Значение поля должно быть больше 0

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    @XmlElement
    private Location location; //Поле не может быть null

    public void update(Person personUpdate){
        this.name = personUpdate.getName();
        this.weight = personUpdate.getWeight();
        this.location.update(personUpdate.getLocation());
    }
}