package ru.itmo.utils;

import ru.itmo.stringEntity.Discipline;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "disciplines_result")
public class DisciplineResult {

    @XmlElement
    private final long totalDisciplines;

    @XmlElementWrapper(name = "disciplines")
    @XmlElement(name = "discipline")
    private final List<Discipline> list;
    public DisciplineResult(long totalDisciplines, List<ru.itmo.entity.Discipline> list){
        this.totalDisciplines = totalDisciplines;
        this.list = list
                .stream()
                .map(ru.itmo.entity.Discipline::toUnrealDiscipline)
                .collect(Collectors.toList());
    }

    public DisciplineResult(){
        totalDisciplines = 0;
        list = new ArrayList<>();
    }
}
