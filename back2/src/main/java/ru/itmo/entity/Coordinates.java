package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Integer x; //Поле не может быть null

    @NotNull
    private double y;

    public void update(Coordinates coordinatesUpdate){
        this.x = coordinatesUpdate.getX();
        this.y = coordinatesUpdate.getY();
    }

    public ru.itmo.stringEntity.Coordinates toUnrealCoordinates(){
        return new ru.itmo.stringEntity.Coordinates(
          Long.toString(id),
          x.toString(),
          Double.toString(y)
        );
    }
}