/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Input;

import br.uff.ic.Prov_Viewer.GraphFrame;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.DefaultMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.ColorScheme;
import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kohwalter
 */
public class Config {
    //Filter List

    public static List<String> edgetype = new ArrayList<String>();
    //Edge Stroke
    public static List<Boolean> edgeStroke = new ArrayList<Boolean>();
    ;   
    //Edge
    public static List<Boolean> edgecollapse = new ArrayList<Boolean>();
    ;
    //Modes
    public static Collection<ColorScheme> vertexModes = new ArrayList<ColorScheme>();
    //Temporal Layout
    public static String layoutSpecialVertexType;
    //Vertex Stroke variables
    public static List<String> vertexStrokevariables = new ArrayList<String>();
    //ActivityVertex
    //All 3 arrays must have the same size
    public static List<String> actVerAtt = new ArrayList<String>();
    public static List<String> actVerValue = new ArrayList<String>();
    public static List<Paint> actVerColor = new ArrayList<Paint>();

    public static void Initialize() {
        try {
            URL location = Config.class.getResource("/configSDM.xml");

//            URL location = Config.class.getProtectionDomain().getCodeSource().getLocation();
//            File fXmlFile = new File(location.getFile() + "/br/uff/ic/Prov_Viewer/Input/configSDM.xml");
            File fXmlFile = new File(location.getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            //Temporal Layout Backbone
            NodeList nList = doc.getElementsByTagName("layoutSpecialVertexType");
            layoutSpecialVertexType = nList.item(0).getTextContent();

            //Edge Types
            nList = doc.getElementsByTagName("edgetypes");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    edgetype.add(eElement.getElementsByTagName("edge").item(0).getTextContent());
                    edgeStroke.add(Boolean.parseBoolean(eElement.getElementsByTagName("edgestroke").item(0).getTextContent()));
                    edgecollapse.add(Boolean.parseBoolean(eElement.getElementsByTagName("collapsefunction").item(0).getTextContent()));
                }
            }
            //Vertex Stroke Types
            nList = doc.getElementsByTagName("vertexstroketype");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String list = "";
                    if (!eElement.getElementsByTagName("attribute").item(0).getTextContent().isEmpty()) {
                        list = eElement.getElementsByTagName("attribute").item(0).getTextContent();
                        list += " " + eElement.getElementsByTagName("values").item(0).getTextContent();
                    }
                    vertexStrokevariables.add(list);
                }
            }

            //Vertex Color Schemes
            //Default mode is always set, no matter the config.xml
            DefaultMode def = new DefaultMode("Default");
            vertexModes.add(def);

            nList = doc.getElementsByTagName("displaymode");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String attribute = eElement.getElementsByTagName("attribute").item(0).getTextContent();
                    String values = "empty";
                    double invGreenT = 0;
                    double InvYellowT = 0;
                    if (!eElement.getElementsByTagName("values").item(0).getTextContent().isEmpty()) {
                        values = eElement.getElementsByTagName("values").item(0).getTextContent();
                    }
                    if (!eElement.getElementsByTagName("InvGreenT").item(0).getTextContent().isEmpty()) {
                        invGreenT = Double.parseDouble(eElement.getElementsByTagName("InvGreenT").item(0).getTextContent());
                    }
                    if (!eElement.getElementsByTagName("InvYellowT").item(0).getTextContent().isEmpty()) {
                        InvYellowT = Double.parseDouble(eElement.getElementsByTagName("InvYellowT").item(0).getTextContent());
                    }

                    Class cl = Class.forName("br.uff.ic.Prov_Viewer.Vertex.VisualizationModes." + eElement.getElementsByTagName("modefunction").item(0).getTextContent());
                    Constructor con = cl.getConstructor(String.class, String.class, double.class, double.class);
                    ColorScheme attMode = (ColorScheme) con.newInstance(attribute, values, invGreenT, InvYellowT);
                    vertexModes.add(attMode);
                }
            }

            //Activity Variables
            nList = doc.getElementsByTagName("activityVariables");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    actVerAtt.add(eElement.getElementsByTagName("attribute").item(0).getTextContent());
                    actVerValue.add(eElement.getElementsByTagName("value").item(0).getTextContent());
                    int r = Integer.parseInt(eElement.getElementsByTagName("r").item(0).getTextContent());
                    int g = Integer.parseInt(eElement.getElementsByTagName("g").item(0).getTextContent());
                    int b = Integer.parseInt(eElement.getElementsByTagName("b").item(0).getTextContent());
                    Paint color = new Color(r, g, b);
                    actVerColor.add(color);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initialize Interface Filters
        GraphFrame.FilterList.setListData(edgetype.toArray());

        String[] items = new String[vertexModes.size()];
        int j = 0;
        for (ColorScheme mode : vertexModes) {
            items[j] = mode.GetName();
            j++;
        }

        GraphFrame.StatusFilterBox.setModel(
                new DefaultComboBoxModel(items));

    }
}
