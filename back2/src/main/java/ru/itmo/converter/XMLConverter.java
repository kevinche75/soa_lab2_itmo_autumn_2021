package ru.itmo.converter;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class XMLConverter implements Converter {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> String listToStr(List<T> list, String name, T[] array) {
        try {
            JAXBContext jc = JAXBContext.newInstance(array.getClass());
            JAXBElement root = new JAXBElement(new QName(name),
                    array.getClass(), list.toArray(array));
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(root, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public <T> String toStr(T object) {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(object, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public <T> T fromStr(String str, Class<T> tClass) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(tClass);

        Unmarshaller unmarshaller = jc.createUnmarshaller();

        return (T) unmarshaller.unmarshal(new StringReader(str));
    }
}
