package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private float x;

    @NotNull
    private Integer y; //Поле не может быть null

    private int z;

    @NotBlank
    private String name; //Строка не может быть пустой, Поле не может быть null

    public void update(Location locationUpdate){
        this.x = locationUpdate.getX();
        this.y = locationUpdate.getY();
        this.z = locationUpdate.getZ();
        this.name = locationUpdate.getName();
    }

    public ru.itmo.stringEntity.Location toUnrealLocation(){
        return new ru.itmo.stringEntity.Location(
                Long.toString(id),
                Float.toString(x),
                y.toString(),
                Integer.toString(z),
                name
        );
    }
}