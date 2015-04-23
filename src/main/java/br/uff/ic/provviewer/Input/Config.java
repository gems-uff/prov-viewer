/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Vertex.ColorScheme.ColorScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.DefaultScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.ProvScheme;
import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
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
    
    public String demoPath = "/Car_Tutorial_config.xml";
    //public String demoPath = "/2D_Provenance_config.xml";
    //public String demoPath = "/Angry_Robots_config.xml";
    //public String demoPath = "/config.xml";
    //public String demoPath = "/map_config.xml";
    //public String demoPath = "/bus_config.xml";
    
    //Filter List
    public static List<EdgeType> edgetype = new ArrayList<EdgeType>();

    //Modes
    public static Collection<ColorScheme> vertexModes = new ArrayList<ColorScheme>();
    
    //Temporal Layout
    public static String layoutSpecialVertexType;
    // Coordinates Layout
    public static String layoutAxis_X;
    public static String layoutAxis_Y;
    //BackGround for Coordinates Layout
    public static String imageLocation;
    public static double imageOffsetX;
    public static double imageOffsetY;
    
    public static double spatialLayoutPosition;
    public static double coordinatesScale;
    public static double scale;
    public static boolean showEntityDate;
    public static boolean showEntityLabel;
    //Vertex Stroke variables
    public static List<String> vertexStrokevariables = new ArrayList<String>();
    //ActivityVertex
    //All 3 arrays must have the same size
    public static List<String> actVerAtt = new ArrayList<String>();
    public static List<String> actVerValue = new ArrayList<String>();
    public static List<Paint> actVerColor = new ArrayList<Paint>();

    public void Initialize() {
        URL location = this.getClass().getResource(demoPath);
        File fXmlFile = new File(location.getFile());
//        File fXmlFile = new File("2D_Provenance_config.xml");
        System.out.println(fXmlFile.getPath());
        Initialize(fXmlFile);    
        ComputeCoordScale();

    }
    
    public static double width;
    public static double height;
    public static void ComputeCoordScale()
    {
        final ImageIcon icon = new ImageIcon(Config.class.getResource(imageLocation));
        width = icon.getIconWidth();
        height = icon.getIconHeight();
        coordinatesScale = (width * 0.5);
        coordinatesScale = coordinatesScale * 100;
        if(spatialLayoutPosition != 0)
            coordinatesScale = coordinatesScale / spatialLayoutPosition;
        coordinatesScale = coordinatesScale / 100;
    }
    public static void Initialize(File fXmlFile) {
        try {
            edgetype = new ArrayList<EdgeType>();
            vertexModes = new ArrayList<ColorScheme>();
            layoutSpecialVertexType = "";
            scale = 1.0;
            vertexStrokevariables = new ArrayList<String>();
            actVerAtt = new ArrayList<String>();
            actVerValue = new ArrayList<String>();
            actVerColor = new ArrayList<Paint>();
//            URL location = Config.class.getResource("/config.xml");
//
//            File fXmlFile = new File(location.getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            //Temporal Layout Backbone
            NodeList nList = doc.getElementsByTagName("layoutbackbone");
            layoutSpecialVertexType = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("layoutAxis_X");
            layoutAxis_X = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("layoutAxis_Y");
            layoutAxis_Y = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("imageLocation");
            imageLocation = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("imageOffset_X");
            imageOffsetX = Double.parseDouble(nList.item(0).getTextContent());
            nList = doc.getElementsByTagName("imageOffset_Y");
            imageOffsetY = Double.parseDouble(nList.item(0).getTextContent());    
            nList = doc.getElementsByTagName("spatialLayoutPosition");
            spatialLayoutPosition = Double.parseDouble(nList.item(0).getTextContent());
            nList = doc.getElementsByTagName("temporalLayoutscale");
            scale = Double.parseDouble(nList.item(0).getTextContent());
            nList = doc.getElementsByTagName("showentitydate");
            showEntityDate = Boolean.parseBoolean(nList.item(0).getTextContent());
            nList = doc.getElementsByTagName("showentitylabel");
            showEntityLabel = Boolean.parseBoolean(nList.item(0).getTextContent());

            //Edge Types
            nList = doc.getElementsByTagName("edgetype");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    EdgeType etype = new EdgeType();
                    etype.type = eElement.getElementsByTagName("edge").item(0).getTextContent();
                    etype.stroke = eElement.getElementsByTagName("edgestroke").item(0).getTextContent();
                    etype.collapse = eElement.getElementsByTagName("collapsefunction").item(0).getTextContent();
                    edgetype.add(etype);
                }
            }
            //Vertex Stroke Types
            nList = doc.getElementsByTagName("vertexstroke");
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
            DefaultScheme defaultScheme = new DefaultScheme("Default");
            vertexModes.add(defaultScheme);
            ProvScheme provScheme = new ProvScheme("Prov");
            vertexModes.add(provScheme);

            nList = doc.getElementsByTagName("colorscheme");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String attribute = eElement.getElementsByTagName("attribute").item(0).getTextContent();
                    String values = "empty";
                    String maxvalue = null;
                    String minvalue = null;
                    String restrictedAttribute = null;
                    String restrictedValue = null;
                    boolean limited = false;
                    boolean restricted = false;
                    if (!eElement.getElementsByTagName("values").item(0).getTextContent().isEmpty()) {
                        values = eElement.getElementsByTagName("values").item(0).getTextContent();
                    }
                    NodeList goodattribute = eElement.getElementsByTagName("goodvalue");
                    if(goodattribute != null && goodattribute.getLength() > 0)
                    {
                        if (!eElement.getElementsByTagName("goodvalue").item(0).getTextContent().isEmpty()) {
                            maxvalue = eElement.getElementsByTagName("goodvalue").item(0).getTextContent();
                            limited = true;
                        }
                    }
                    NodeList badattribute = eElement.getElementsByTagName("badvalue");
                    if(badattribute != null && badattribute.getLength() > 0)
                    {
                        if (!eElement.getElementsByTagName("badvalue").item(0).getTextContent().isEmpty()) {
                            minvalue = eElement.getElementsByTagName("badvalue").item(0).getTextContent();
                            limited = true;
                        }
                    }
                    NodeList restrictedAtt = eElement.getElementsByTagName("restrictedAttribute");
                    if(restrictedAtt != null && restrictedAtt.getLength() > 0)
                    {
                        if (!eElement.getElementsByTagName("restrictedAttribute").item(0).getTextContent().isEmpty()) {
                            restrictedAttribute = eElement.getElementsByTagName("restrictedAttribute").item(0).getTextContent();
                            restricted = true;
                        }
                    }
                    NodeList restrictedVal = eElement.getElementsByTagName("restrictedValue");
                    if(restrictedVal != null && restrictedVal.getLength() > 0)
                    {
                        if (!eElement.getElementsByTagName("restrictedValue").item(0).getTextContent().isEmpty()) {
                            restrictedValue = eElement.getElementsByTagName("restrictedValue").item(0).getTextContent();
                            restricted = true;
                        }
                    }

                    Class cl = Class.forName("br.uff.ic.provviewer.Vertex.ColorScheme." + eElement.getElementsByTagName("class").item(0).getTextContent());
                    if(restricted)
                    {
                        Constructor con = cl.getConstructor(String.class, String.class, String.class, String.class, boolean.class, String.class, String.class);
                        ColorScheme attMode = (ColorScheme) con.newInstance(attribute, values, maxvalue, minvalue, limited, restrictedAttribute, restrictedValue);
                        vertexModes.add(attMode);
                    }
                    else
                    {
                        Constructor con = cl.getConstructor(String.class, String.class, String.class, String.class, boolean.class);
                        ColorScheme attMode = (ColorScheme) con.newInstance(attribute, values, maxvalue, minvalue, limited);
                        vertexModes.add(attMode);
                    }
                    
                }
            }

            //Activity Variables
            nList = doc.getElementsByTagName("activitycolor");
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
        String[] types = new String[edgetype.size()];
        for(int x = 0; x < types.length; x++)
        {
            types[x] = edgetype.get(x).type;
        }
        GraphFrame.FilterList.setListData(types);

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
