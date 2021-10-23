package ru.itmo.utils;

import lombok.AllArgsConstructor;
import ru.itmo.stringEntity.LabWork;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "labworks_result")
public class LabWorksResult {

    @XmlElement
    private final long totalLabWorks;
    @XmlElementWrapper(name = "labworks")
    @XmlElement(name = "labwork")
    private final List<LabWork> list;
    public LabWorksResult(){
        this.totalLabWorks = 0;
        this.list = new ArrayList<>();
    }

    public LabWorksResult(long totalLabWorks, List<ru.itmo.entity.LabWork> labWorks){
        this.totalLabWorks = totalLabWorks;
        this.list = labWorks
                .stream()
                .map(ru.itmo.entity.LabWork::toUnrealLabWork)
                .collect(Collectors.toList());
    }
}
