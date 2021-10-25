package ru.itmo.stringEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.itmo.converter.FieldConverter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@XmlRootElement
public class Discipline {

    @XmlElement
    private String id;

    @XmlElement
    private String name;

    public ru.itmo.entity.Discipline toRealDiscipline(){
        return new ru.itmo.entity.Discipline(
                FieldConverter.longConvert(id),
                name,
                null
        );
    }
}
