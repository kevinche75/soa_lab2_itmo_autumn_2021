package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @Positive
    private Float weight; //Поле может быть null, Значение поля должно быть больше 0

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location; //Поле не может быть null

    public void update(Person personUpdate){
        this.name = personUpdate.getName();
        this.weight = personUpdate.getWeight();
        this.location.update(personUpdate.getLocation());
    }

    public ru.itmo.stringEntity.Person toUnrealPerson(){
        return new ru.itmo.stringEntity.Person(
          Long.toString(id),
          name,
          weight.toString(),
                location.toUnrealLocation()
        );
    }
}