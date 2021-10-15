package ru.itmo.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ServerResponse {

    @XmlElement
    String message;
}
