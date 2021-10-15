package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@XmlRootElement
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlElement
    private long id;

    @XmlElement
    @NotNull
    private Integer x; //Поле не может быть null

    @XmlElement
    @NotNull
    private double y;

    public void update(Coordinates coordinatesUpdate){
        this.x = coordinatesUpdate.getX();
        this.y = coordinatesUpdate.getY();
    }
}