package tests;

import $1.Recognizer;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import $1.*;

import javax.xml.stream.XMLStreamException;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException, XMLStreamException, IOException {


//      read in xmls
//      map : user -> speed -> name -> id -> point
        String[] nameList = {"arrow", "caret", "check", "circle", "delete_mark", "left_curly_brace", "left_sq_bracket", "pigtail", "question_mark", "rectangle", "right_curly_brace", "right_sq_bracket", "star", "triangle", "v", "x"};
        String[] user = {"s00", "s01", "s02", "s03", "s04", "s05", "s06", "s07", "s08", "s09", "s10", "s11"};
        String[] id = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
        List<String> idList = new ArrayList<>(Arrays.asList(id));
        XmlParser pars = new XmlParser();

        FileWriter fw = new FileWriter("result2.txt");
        BufferedWriter bufw = new BufferedWriter(fw);
        for(int i = 2; i <= 11; i++) {
            for(int j = 0; j < 3; j++) {
                String speed = "";
                if(j == 0) {
                    speed = "slow";
                } else if(j == 1) {
                    speed = "medium";
                } else {
                    speed = "fast";
                }


                for (int e = 1; e <= 9; e++) {
                    //System.out.println(e);
                    //1-100
                    for (int t = 1; t <= 100; t++) {
                        //System.out.println(t);
                        Set<UserInput> testSet = new HashSet<>();
                        Set<UserInput> candSet = new HashSet<>();
                        //List<UserInput> uiCollection = new ArrayList<>(); //size == 10
                        //for each gesture type 1-16
                        for (int k = 0; k < 16; k++) {
                            //generate point list for one specific user's gesture type at a given speed
                            for (int a = 0; a < e; a++) {
                                int rand = (int)(Math.random() * idList.size());
                                String idn = idList.get(rand);
                                idList.remove(rand);
                                String filepath = "src/tests/xml_logs/" + user[i] + "/" + speed + "/" + nameList[k] + idn + ".xml";
                                UserInput ui = pars.readFile(filepath);
                                testSet.add(ui);
                            }

                            int rand = (int)(Math.random() * idList.size());
                            String idn = idList.get(rand);
                            String fileP = "src/tests/xml_logs/" + user[i] + "/" + speed + "/" + nameList[k] + idn + ".xml";
                            candSet.add(pars.readFile(fileP));
                            idList = new ArrayList<>(Arrays.asList(id));
                        }

                        //for each c in cs
                        for (UserInput c : candSet) {
                            //treemap key->score value->resultName
                            //key sorted in descending order
                            SortedMap<Double, UserInput> sortedMap = new TreeMap<>(new Comparator<Double>() {
                                public int compare(Double a1, Double a2) {
                                    return a2.compareTo(a1);
                                }
                            });
                            Recognizer recognizer = new Recognizer(true);
                            for (UserInput ts : testSet) {
                                Result res = recognizer.Recognize(c, ts);
                                sortedMap.put(res.score, res.ui);
                            }

                            UserInput recRes = sortedMap.get(sortedMap.firstKey());
                            int correct = -1;
                            if (recRes.name.equalsIgnoreCase(c.name)) {
                                correct = 1;
                            } else {
                                correct = 0;
                            }
                            //for console output test purpose
//                            System.out.println("s" + i + "," + speed + "," + c.name + "," + t + "," + e + "," + testSet.size() + ","
//                                    + printSetContent(testSet) + "," + c.user + "-" + c.name + "-" + c.id + "," + recRes.name + "," + correct
//                                    + "," + sortedMap.firstKey() + "," + recRes.user + "-" + recRes.name + "-" + recRes.id + "," + getNbestList(sortedMap));

                            bufw.write("s" + i + "," + speed + "," + c.name + "," + t + "," + e + "," + testSet.size() + ","
                                    + printSetContent(testSet) + "," + c.user + "-" + c.name + "-" + c.id + "," + recRes.name + "," + correct
                                    + "," + sortedMap.firstKey() + "," + recRes.user + "-" + recRes.name + "-" + recRes.id + "," + getNbestList(sortedMap));
                            bufw.newLine();
                        }
                        testSet.clear();
                        candSet.clear();
                    }
                }
            }
        }


            bufw.flush();
            bufw.close();



    }

    public static String printSetContent(Set<UserInput> s) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(UserInput ui : s) {
            sb.append(ui.user+"-"+ui.name);
            sb.append("-" + ui.id+";");
        }
        return sb.deleteCharAt(sb.length() - 1).append("}").toString();
    }

    public static String getNbestList(SortedMap<Double, UserInput> tm) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        for(Entry<Double, UserInput> e : tm.entrySet()) {
            if(i == 20) {
                break;
            }
            UserInput curr = e.getValue();
            sb.append(curr.user+"-"+curr.name+"-"+curr.id+"-"+e.getKey()+";");
            i++;

        }
        return sb.deleteCharAt(sb.length() - 1).append("}").toString();
    }
}
