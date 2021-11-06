package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "getLabWorkDiscipline",
                query = "select d " +
                        "from Discipline d " +
                        "join LabWork l " +
                        "where l.id = :labId "
        )
})

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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinTable(
            name="discipline_labWorks",
            joinColumns = @JoinColumn( name="discipline_id"),
            inverseJoinColumns = @JoinColumn( name="labWork_id")
    )
    private List<LabWork> labWorks = new ArrayList<>();

    public ru.itmo.stringEntity.Discipline toUnrealDiscipline(){
        return new ru.itmo.stringEntity.Discipline(
          Long.toString(id),
          name
        );
    }
}
