/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.utility.IO.BasePath;
import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.ColorScheme.ColorScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.DefaultScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.DefaultVertexColorScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.ProvScheme;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class responsible for configuring the tool to a specific domain
 *
 * @author Kohwalter
 */
public class Config {

    //Filter List
    public List<EdgeType> edgetype = new ArrayList<>();

    //Modes
    public Collection<ColorScheme> vertexModes = new ArrayList<>();

    //Temporal Layout
    public String layoutSpecialVertexType;
    
    // Default Layout
    public String defaultLayout = "SpatialLayout";

    // Coordinates Layout
    public String layoutAxis_X;
    public String layoutAxis_Y;

    //BackGround for Coordinates Layout
    public String imageLocation;
    public double imageOffsetX;
    public double imageOffsetY;
    public boolean orthogonal = true;
    public double googleZoomLevel;

    public double spatialLayoutPosition;
    public double coordinatesScale;
    public double scale;
//    public boolean showEntityDate;
//    public boolean showEntityLabel;

    public double width;
    public double height;

    //Vertex Stroke variables
    public List<String> vertexStrokevariables = new ArrayList<>();

    //ActivityVertex
    //All 3 arrays must have the same size
    public List<String> actVerAtt = new ArrayList<>();
    public List<String> actVerValue = new ArrayList<>();
    public List<Paint> actVerColor = new ArrayList<>();

    /**
     * Method to configure the tool for the first time using the default graph
     * and configuration
     *
     * @param variables
     */
    public void Initialize(Variables variables) {
        System.out.println("Config: " + BasePath.getBasePathForClass(Config.class) + variables.configDemo);
        File fXmlFile = new File(BasePath.getBasePathForClass(Config.class) + variables.configDemo);
        Initialize(fXmlFile);

    }

    /**
     * Method to compute the graph scale for the Spatial Layout
     */
    public void ComputeCoordScale() {
        final ImageIcon icon = new ImageIcon(BasePath.getBasePathForClass(Config.class) + imageLocation);
        width = icon.getIconWidth();
        height = icon.getIconHeight();
        if(width > 0) {
            coordinatesScale = (width * 0.5);
            coordinatesScale = coordinatesScale * 100;
            if (spatialLayoutPosition != 0) {
                coordinatesScale = coordinatesScale / spatialLayoutPosition;
            }
            coordinatesScale = coordinatesScale / 100;
        }
        else
            coordinatesScale = -50;
    }

    public void DetectEdges(Collection<Edge> edges) {
        Map<String, EdgeType> newEdges = new HashMap<>();
        for (Edge edge : edges) {
            boolean isNewType = true;
            boolean isNewLabel = true;
            for (EdgeType e : edgetype) {
                if (edge.getType().equalsIgnoreCase(e.type)) {
                    isNewType = false;
                }
                if (edge.getLabel().equalsIgnoreCase(e.type)) {
                    isNewLabel = false;
                }
            }
            if (isNewType) {
                EdgeType newEdge = new EdgeType();
                newEdge.type = edge.getType();
                newEdge.stroke = "MAX";
                newEdge.collapse = "SUM";
                newEdges.put(newEdge.type, newEdge);
            }
            if (isNewLabel) {
                EdgeType newEdge = new EdgeType();
                newEdge.type = edge.getLabel();
                newEdge.stroke = "MAX";
                newEdge.collapse = "SUM";
                newEdges.put(newEdge.type, newEdge);
            }
        }
        edgetype.addAll(newEdges.values());
        InterfaceEdgeFilters();
        GraphFrame.FilterList.setSelectedIndex(0);

    }

    public void DetectVertexModes(Collection<Object> vertices) {
        Map<String, String> attributeList = new HashMap<>();
        Map<String, ColorScheme> newAttributes = new HashMap<>();
        for (Object v : vertices) {
            attributeList.putAll(((Vertex) v).attributeList());
        }
        for (String att : attributeList.values()) {
            boolean isNew = true;
            for (ColorScheme color : vertexModes) {
                if (att.equalsIgnoreCase(color.attribute) && color.restrictedAttribute == null) {
                    isNew = false;
                }
            }
            if (isNew) {
                DefaultVertexColorScheme attMode = new DefaultVertexColorScheme(att);
                newAttributes.put(att, attMode);
            }
        }

        vertexModes.addAll(newAttributes.values());
        InterfaceStatusFilters();
    }

    /**
     * Method to configure the tool
     *
     * @param fXmlFile is the xml file that contains the configuration for the
     * tool
     */
    public void Initialize(File fXmlFile) {
        try {
            edgetype = new ArrayList<>();
            vertexModes = new ArrayList<>();
            layoutSpecialVertexType = "";
            scale = 1.0;
            vertexStrokevariables = new ArrayList<>();
            actVerAtt = new ArrayList<>();
            actVerValue = new ArrayList<>();
            actVerColor = new ArrayList<>();

            EdgeType allEdges = new EdgeType();
            allEdges.type = "All Edges";
            allEdges.stroke = "MAX";
            allEdges.collapse = "SUM";
            edgetype.add(allEdges);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            //Temporal Layout Backbone
            NodeList nList = doc.getElementsByTagName("layoutbackbone");
            layoutSpecialVertexType = nList.item(0).getTextContent();
            
            // To avoid empty backbone
            if(layoutSpecialVertexType.equalsIgnoreCase(""))
                layoutSpecialVertexType = "Default";
            
            nList = doc.getElementsByTagName("default_layout");
            if(nList.item(0) != null) {
                defaultLayout = nList.item(0).getTextContent();
            }
            
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
            nList = doc.getElementsByTagName("googleZoomLevel");
            googleZoomLevel = Double.parseDouble(nList.item(0).getTextContent());
            if(googleZoomLevel != 0)
                orthogonal = false;
            
            nList = doc.getElementsByTagName("temporalLayoutscale");
            scale = Double.parseDouble(nList.item(0).getTextContent());

            ComputeCoordScale();
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
                    if (goodattribute != null && goodattribute.getLength() > 0) {
                        if (!eElement.getElementsByTagName("goodvalue").item(0).getTextContent().isEmpty()) {
                            maxvalue = eElement.getElementsByTagName("goodvalue").item(0).getTextContent();
                            limited = true;
                        }
                    }
                    NodeList badattribute = eElement.getElementsByTagName("badvalue");
                    if (badattribute != null && badattribute.getLength() > 0) {
                        if (!eElement.getElementsByTagName("badvalue").item(0).getTextContent().isEmpty()) {
                            minvalue = eElement.getElementsByTagName("badvalue").item(0).getTextContent();
                            limited = true;
                        }
                    }
                    NodeList restrictedAtt = eElement.getElementsByTagName("restrictedAttribute");
                    if (restrictedAtt != null && restrictedAtt.getLength() > 0) {
                        if (!eElement.getElementsByTagName("restrictedAttribute").item(0).getTextContent().isEmpty()) {
                            restrictedAttribute = eElement.getElementsByTagName("restrictedAttribute").item(0).getTextContent();
                            restricted = true;
                        }
                    }
                    NodeList restrictedVal = eElement.getElementsByTagName("restrictedValue");
                    if (restrictedVal != null && restrictedVal.getLength() > 0) {
                        if (!eElement.getElementsByTagName("restrictedValue").item(0).getTextContent().isEmpty()) {
                            restrictedValue = eElement.getElementsByTagName("restrictedValue").item(0).getTextContent();
                            restricted = true;
                        }
                    }

                    Class cl = Class.forName("br.uff.ic.provviewer.Vertex.ColorScheme." + eElement.getElementsByTagName("class").item(0).getTextContent());
                    if (restricted) {
                        Constructor con = cl.getConstructor(String.class, String.class, String.class, String.class, boolean.class, String.class, String.class);
                        ColorScheme attMode = (ColorScheme) con.newInstance(attribute, values, maxvalue, minvalue, limited, restrictedAttribute, restrictedValue);
                        vertexModes.add(attMode);
                    } else {
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
        InterfaceEdgeFilters();
        InterfaceStatusFilters();
    }

    /**
     * Function to update the edge list in the GraphFrame interface
     */
    private void InterfaceEdgeFilters() {
        //Initialize Interface Filters
        String[] types = new String[edgetype.size()];
        for (int x = 0; x < types.length; x++) {
            types[x] = edgetype.get(x).type;
        }
        GraphFrame.FilterList.setListData(types);
    }

    /**
     * Function to update the status filter list in the GraphFrame interface
     */
    private void InterfaceStatusFilters() {
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
