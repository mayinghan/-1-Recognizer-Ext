package tests;

import java.io.*;
import javax.xml.stream.XMLStreamException;

public class ThresholdTester {
    /**
     * this class will read in all the 1D (line) gestures to tune the threshold for 1D gestures
     */

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException, IOException {
        String[] user = {"10-stylus-", "11-stylus-", "12-stylus-", "22-stylus-", "28-stylus-", "41-finger-", "58-finger-", "61-finger-", "66-finger-", "68-stylus-", "71-stylus-", "73-finger-",
                            "75-finger-", "77-finger-", "85-finger-", "88-stylus-", "94-finger-", "95-stylus-", "98-stylus-", "99-finger-"};
        String[] speed = {"FAST", "MEDIUM", "SLOW"};

        FileWriter fw = new FileWriter("threshold.csv");
        BufferedWriter bufw = new BufferedWriter(fw);
        //import XMLParser
        XmlParser pars = new XmlParser();
        for(int i = 0; i < user.length; i++) {
            //iterate through the users
            for(int j = 0; j < speed.length; j++) {
                //iterate through the speeds
                for(int k = 1; k <= 10; k++) {
                    //iterate through 10 inputs for each category
                    String filepath = "src/mmg/" + user[i] + speed[j] + "/" + user[i] + speed[j].toLowerCase() + "-line-" + (k < 10 ? "0" + k : k) + ".xml";
                    UserInput lineGesture = pars.readFile(filepath);
                    System.out.println(lineGesture.user + " " + lineGesture.name + "'s total turining angle is " + lineGesture.getTotalAngle());
                    bufw.write(lineGesture.user + "," + lineGesture.speed + "," + lineGesture.getTotalAngle());
                    bufw.newLine();
                }
            }
        }

        bufw.flush();
        bufw.close();
    }
}
