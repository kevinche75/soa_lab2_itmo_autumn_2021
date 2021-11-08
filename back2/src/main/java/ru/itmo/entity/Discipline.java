package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String name;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="discipline_labWorks",
            joinColumns = @JoinColumn( name="discipline_id")
    )
    @Column(name = "labWork_id")
    private List<Long> labWorks = new ArrayList<>();

    public ru.itmo.stringEntity.Discipline toUnrealDiscipline(){
        return new ru.itmo.stringEntity.Discipline(
          Long.toString(id),
          name
        );
    }
}
