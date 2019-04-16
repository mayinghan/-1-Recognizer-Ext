package tests;
//based on tutorial: https://www.tutorialspoint.com/java_xml/java_stax_parse_document.htm
import $1.*;
import java.io.*;
import java.util.*;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * This file is for reading XML files
 */

public class XmlParser {

    /**
     *
     * @param filename: filepath
     * @return a UserInput object (gesture)
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public UserInput readFile(String filename) throws FileNotFoundException, XMLStreamException {
        List<Point> userPoint = new ArrayList<>();
        String name = "";
        int user = -1;
        String speed = "";
        int id = -1;
        UserInput ui = null;

        double x = 0.0, y = 0.0;

        InputStream in = new FileInputStream(filename);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
        while(eventReader.hasNext()) {
            //System.out.println("reader: " + eventReader);
            XMLEvent event = eventReader.nextEvent();
            //System.out.println(event);
            if(event.isStartElement()) {
                StartElement se = event.asStartElement();
                //System.out.println("Attributes " + se.getName().getLocalPart());
                if(se.getName().getLocalPart().equalsIgnoreCase("Gesture")) {
                    Iterator<Attribute> att = se.getAttributes();
                    while(att.hasNext()) {
                        Attribute a = att.next();
                        if(a.getName().toString().equalsIgnoreCase("Name")) {
                            name = a.getValue().substring(0, a.getValue().length() - 2);
                           // System.out.println("name "+name);
                        } else if(a.getName().toString().equalsIgnoreCase("Subject")) {
                            user = Integer.parseInt(a.getValue());
                        } else if(a.getName().toString().equalsIgnoreCase("Speed")) {
                            speed = a.getValue();
                        } else if(a.getName().toString().equalsIgnoreCase("Number")) {
                            id = Integer.parseInt(a.getValue());
                        }
                    }
                } else if(se.getName().getLocalPart().equalsIgnoreCase("Point")) {
                    Iterator<Attribute> pt = se.getAttributes();
                    while(pt.hasNext()) {
                        Attribute a = pt.next();
                        if(a.getName().toString().equals("X")) {
                            x = Double.parseDouble(a.getValue());
                        }
                        if(a.getName().toString().equals("Y")) {
                            y = Double.parseDouble(a.getValue());
                        }
                    }
                    //System.out.println("x: " + x + " y: " + y);
                    userPoint.add(new Point(x, y, id));
                }
            }
            if(event.isEndElement()) {
                EndElement ee = event.asEndElement();
                if(ee.getName().getLocalPart().equals("Gesture")) {
                    break;
                }
            }
//            System.out.println("name "+name);
//            System.out.println("user "+user);
            //ui = new UserInput(name, userPoint, user, speed);
        }
        ui = new UserInput(name, userPoint, user, speed, id);

        return ui;
    }
}
