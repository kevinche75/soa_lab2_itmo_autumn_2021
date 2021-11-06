package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "labwork")
public class LabWork {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    private String name; //Поле не может быть null, Строка не может быть пустой

//    @Setter
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
//    @JoinTable(
//            name="discipline_labWorks",
//            joinColumns = @JoinColumn( name="labWork_id"),
//            inverseJoinColumns = @JoinColumn( name="discipline_id")
//    )
//    private Discipline discipline;

    public ru.itmo.stringEntity.ShortLabWork toShortLabWork(){
        return new ru.itmo.stringEntity.ShortLabWork(
                id.toString(),
                name
        );
    }
}