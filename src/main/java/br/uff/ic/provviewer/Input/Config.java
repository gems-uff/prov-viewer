/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.utility.IO.BasePath;
import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.ColorScheme.ColorScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexColorScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.DefaultVertexColorScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.ProvScheme;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexGraphGrayScaleScheme;
import br.uff.ic.utility.AttValueColor;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Color;
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

    public int vertexSize = 40;

    //Filter List
    public List<EdgeType> edgetype = new ArrayList<>();
    public List<String> vertexLabelFilter = new ArrayList<>();

    //Modes
    public Map<String, ColorScheme> vertexModes = new HashMap<>();

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
    public String timeScale = "";
    public boolean considerEdgeLabelForMerge = false;

    //Vertex Stroke variables
    public List<String> vertexStrokevariables = new ArrayList<>();

    //ActivityVertex
    //All 3 arrays must have the same size
    public List<AttValueColor> activityVC = new ArrayList<>();
    public List<AttValueColor> entityVC = new ArrayList<>();
    public List<AttValueColor> agentVC = new ArrayList<>();

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
    
    public void resetVertexModeInitializations() {
        for (ColorScheme vm : vertexModes.values()) {
            vm.resetInitialization();
        }
    }

    /**
     * Method to compute the graph scale for the Spatial Layout
     */
    public void ComputeCoordScale() {
        final ImageIcon icon = new ImageIcon(BasePath.getBasePathForClass(Config.class) + imageLocation);
        width = icon.getIconWidth();
        height = icon.getIconHeight();
        if (width > 0) {
            coordinatesScale = (width * 0.5);
            coordinatesScale = coordinatesScale * 100;
            if (spatialLayoutPosition != 0) {
                coordinatesScale = coordinatesScale / spatialLayoutPosition;
            }
            coordinatesScale = coordinatesScale / 100;
        } else {
            coordinatesScale = -50;
        }
    }

    public void DetectEdges(Collection<Edge> edges) {
        Map<String, EdgeType> newEdges = new HashMap<>();
        int colorCount = 0;
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
                newEdge.edgeColor = Utils.getColor(colorCount);
                colorCount++;
                newEdges.put(newEdge.type, newEdge);
            }
            if (isNewLabel) {
                EdgeType newEdge = new EdgeType();
                newEdge.type = edge.getLabel();
                newEdge.stroke = "MAX";
                newEdge.collapse = "SUM";
                newEdge.edgeColor = Utils.getColor(colorCount);
                colorCount++;
                newEdges.put(newEdge.type, newEdge);
            }
        }
        edgetype.addAll(newEdges.values());
        InterfaceEdgeFilters();
        GraphFrame.edgeFilterList.setSelectedIndex(0);

    }

    public void DetectVertexModes(Collection<Object> vertices) {
        Map<String, String> attributeList = new HashMap<>();
        Map<String, ColorScheme> newAttributes = new HashMap<>();
        for (Object v : vertices) {
            attributeList.putAll(((Vertex) v).attributeList());
        }
        for (String att : attributeList.values()) {
//            boolean isNew = true;
            if(!vertexModes.containsKey(att)) {
                DefaultVertexColorScheme attMode = new DefaultVertexColorScheme(att);
                newAttributes.put(att, attMode);
            }
//            for (ColorScheme color : vertexModes.values()) {
//                if (att.equalsIgnoreCase(color.attribute) && color.restrictedAttribute == null) {
//                    isNew = false;
//                }
//            }
//            if (isNew) {
//                DefaultVertexColorScheme attMode = new DefaultVertexColorScheme(att);
//                newAttributes.put(att, attMode);
//            }
        }

        vertexModes.putAll(newAttributes);//.addAll(newAttributes.values());
        InterfaceStatusFilters();
    }
    
    

//    public void DetectVertexAttributeFilterValues(Collection<Object> vertices) {
//        Map<String, String> valueList = new HashMap<>();
//        Map<String, ColorScheme> newAttributes = new HashMap<>();
//        for (Object v : vertices) {
//            valueList.put(((Vertex) v).getAttributeValue(vertexAttributeFilter), ((Vertex) v).getAttributeValue(vertexAttributeFilter));
//        }
//
//        vertexLabelFilter.addAll(valueList.values());
//        InterfaceVertexFilters();
//        GraphFrame.vertexFilterList.setSelectedIndex(0);
//    }
    /**
     * Method to configure the tool
     *
     * @param fXmlFile is the xml file that contains the configuration for the
     * tool
     */
    public void Initialize(File fXmlFile) {
        try {
            edgetype = new ArrayList<>();
            vertexLabelFilter = new ArrayList<>();
            vertexModes = new HashMap<>();
            layoutSpecialVertexType = "";
            scale = 1.0;
            vertexStrokevariables = new ArrayList<>();
            activityVC = new ArrayList<>();
            entityVC = new ArrayList<>();
            agentVC = new ArrayList<>();

            EdgeType allEdges = new EdgeType();
            allEdges.type = "All Edges";
            allEdges.stroke = "MAX";
            allEdges.collapse = "SUM";
            edgetype.add(allEdges);

            String allvertices = "All Vertices";
            vertexLabelFilter.add(allvertices);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("vertexSize");
            if (nList != null && nList.getLength() > 0 && !nList.item(0).getTextContent().equalsIgnoreCase("")) {
                vertexSize = Integer.parseInt(nList.item(0).getTextContent());
            }

            nList = doc.getElementsByTagName("timeScale");
            if (nList != null && nList.getLength() > 0) {
                timeScale = nList.item(0).getTextContent().toLowerCase();
            }

            nList = doc.getElementsByTagName("default_layout");
            if (nList.item(0) != null) {
                defaultLayout = nList.item(0).getTextContent();
            }
            nList = doc.getElementsByTagName("considerEdgeLabelForMerge");
            if (nList.item(0) != null) {
                considerEdgeLabelForMerge = Boolean.parseBoolean(nList.item(0).getTextContent());
            }

            // Temporal Layout parameters
            nList = doc.getElementsByTagName("temporalLayoutbackbone");
            layoutSpecialVertexType = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("temporalLayoutscale");
            if (nList.item(0).getTextContent().equals("")) {
                scale = 1;
            } else {
                scale = Double.parseDouble(nList.item(0).getTextContent());
            }
            // To avoid empty backbone
            if (layoutSpecialVertexType.equalsIgnoreCase("")) {
                layoutSpecialVertexType = "Default";
            }

            // Spatial Layout parameters
            nList = doc.getElementsByTagName("layoutAxis_X");
            layoutAxis_X = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("layoutAxis_Y");
            layoutAxis_Y = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("imageLocation");
            imageLocation = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("imageOffset_X");
            if (nList.item(0).getTextContent().equals("")) {
                imageOffsetX = 0;
            } else {
                imageOffsetX = Double.parseDouble(nList.item(0).getTextContent());
            }

            nList = doc.getElementsByTagName("imageOffset_Y");
            if (nList.item(0).getTextContent().equals("")) {
                imageOffsetY = 0;
            } else {
                imageOffsetY = Double.parseDouble(nList.item(0).getTextContent());
            }

            nList = doc.getElementsByTagName("spatialLayoutPosition");
            if (nList.item(0).getTextContent().equals("")) {
                spatialLayoutPosition = 0;
            } else {
                spatialLayoutPosition = Double.parseDouble(nList.item(0).getTextContent());
            }

            nList = doc.getElementsByTagName("zoomLevel");
            if (nList.item(0).getTextContent().equals("")) {
                googleZoomLevel = 0;
            } else {
                googleZoomLevel = Double.parseDouble(nList.item(0).getTextContent());
            }
            if (googleZoomLevel != 0) {
                orthogonal = false;
            }

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
                    if (eElement.getElementsByTagName("isInverted") != null && eElement.getElementsByTagName("isInverted").getLength() > 0) {
                        if (!eElement.getElementsByTagName("isInverted").item(0).getTextContent().isEmpty()) {
                            etype.isInverted = Boolean.parseBoolean(eElement.getElementsByTagName("isInverted").item(0).getTextContent());
                        }
                    }
                    Color color;
                    if (eElement.getElementsByTagName("r").item(0) != null) {
                        int r = Integer.parseInt(eElement.getElementsByTagName("r").item(0).getTextContent());
                        int g = Integer.parseInt(eElement.getElementsByTagName("g").item(0).getTextContent());
                        int b = Integer.parseInt(eElement.getElementsByTagName("b").item(0).getTextContent());
                        color = new Color(r, g, b);
                    } else {
                        color = new Color(0, 0, 0);
                    }
                    etype.edgeColor = color;
                    edgetype.add(etype);
                }
            }

            //Vertex Label Filters
            nList = doc.getElementsByTagName("vertexAttributeFilter");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String vertexFilter = new String();
                    vertexFilter = eElement.getElementsByTagName("name").item(0).getTextContent();
                    vertexFilter += ": ";
                    vertexFilter += eElement.getElementsByTagName("value").item(0).getTextContent();
                    vertexLabelFilter.add(vertexFilter);
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
            ProvScheme provScheme = new ProvScheme("Prov");
            vertexModes.put("Prov", provScheme);
            VertexGraphGrayScaleScheme graphScheme = new VertexGraphGrayScaleScheme("Graph");
            vertexModes.put("GraphFiles", graphScheme);

            nList = doc.getElementsByTagName("colorscheme");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String attribute = eElement.getElementsByTagName("attribute").item(0).getTextContent();
                    boolean isInverted = false;
                    boolean isZeroWhite = false;
                    String values = "empty";
                    String maxvalue = null;
                    String minvalue = null;
                    String restrictedAttribute = null;
                    String restrictedValue = null;
                    boolean limited = false;
                    boolean restricted = false;
                    if (eElement.getElementsByTagName("trafficLightType") != null && eElement.getElementsByTagName("trafficLightType").getLength() > 0) {
                        if (!eElement.getElementsByTagName("trafficLightType").item(0).getTextContent().isEmpty()) {
                            if (eElement.getElementsByTagName("trafficLightType").item(0).getTextContent().equalsIgnoreCase("type2")) {
                                isZeroWhite = true;
                            }
                        }
                    }
                    if (eElement.getElementsByTagName("isInverted") != null && eElement.getElementsByTagName("isInverted").getLength() > 0) {
                        if (!eElement.getElementsByTagName("isInverted").item(0).getTextContent().isEmpty()) {
                            isInverted = Boolean.parseBoolean(eElement.getElementsByTagName("isInverted").item(0).getTextContent());
                        }
                    }
                    if (eElement.getElementsByTagName("values") != null && eElement.getElementsByTagName("values").getLength() > 0) {
                        if (!eElement.getElementsByTagName("values").item(0).getTextContent().isEmpty()) {
                            values = eElement.getElementsByTagName("values").item(0).getTextContent();
                        }
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
                        Constructor con = cl.getConstructor(boolean.class, boolean.class, String.class, String.class, String.class, String.class, boolean.class, String.class, String.class);
                        ColorScheme attMode = (ColorScheme) con.newInstance(isZeroWhite, isInverted, attribute, values, maxvalue, minvalue, limited, restrictedAttribute, restrictedValue);
                        vertexModes.put(attribute, attMode);
                    } else {
                        Constructor con = cl.getConstructor(boolean.class, boolean.class, String.class, String.class, String.class, String.class, boolean.class);
                        ColorScheme attMode = (ColorScheme) con.newInstance(isZeroWhite, isInverted, attribute, values, maxvalue, minvalue, limited);
                        vertexModes.put(attribute, attMode);
                    }

                }
            }
            VertexColorScheme vertexColorScheme;
            nList = doc.getElementsByTagName("vertexcolor");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                activityVC = new ArrayList<>();
                entityVC = new ArrayList<>();
                agentVC = new ArrayList<>();
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nNode;
                    NodeList innerList;
                    String generalname = e.getElementsByTagName("generalname").item(0).getTextContent();
                    String isAutomatic = e.getElementsByTagName("isAutomatic").item(0).getTextContent();
                    //Activity Variables
                    innerList = e.getElementsByTagName("activitycolor");
                    for (int j = 0; j < innerList.getLength(); j++) {
                        Node innerNode = innerList.item(j);
                        if (innerNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) innerNode;
                            AttValueColor avc = new AttValueColor();
                            avc.name = eElement.getElementsByTagName("attribute").item(0).getTextContent();
                            avc.value = eElement.getElementsByTagName("value").item(0).getTextContent();
                            int r = Integer.parseInt(eElement.getElementsByTagName("r").item(0).getTextContent());
                            int g = Integer.parseInt(eElement.getElementsByTagName("g").item(0).getTextContent());
                            int b = Integer.parseInt(eElement.getElementsByTagName("b").item(0).getTextContent());
                            avc.color = new Color(r, g, b);
                            activityVC.add(avc);
                        }
                    }
                    //Entity Variables
                    innerList = e.getElementsByTagName("entitycolor");
                    for (int j = 0; j < innerList.getLength(); j++) {
                        Node innerNode = innerList.item(j);
                        if (innerNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) innerNode;
                            AttValueColor avc = new AttValueColor();
                            avc.name = eElement.getElementsByTagName("attribute").item(0).getTextContent();
                            if(eElement.getElementsByTagName("value").getLength() > 0 && eElement.getElementsByTagName("value").item(0).getTextContent() != "") {
                                avc.value = eElement.getElementsByTagName("value").item(0).getTextContent();
                                int r = Integer.parseInt(eElement.getElementsByTagName("r").item(0).getTextContent());
                                int g = Integer.parseInt(eElement.getElementsByTagName("g").item(0).getTextContent());
                                int b = Integer.parseInt(eElement.getElementsByTagName("b").item(0).getTextContent());
                                avc.color = new Color(r, g, b);
                            }
                            entityVC.add(avc);
                        }
                    }
                    //Agent Variables
                    innerList = e.getElementsByTagName("agentcolor");
                    for (int j = 0; j < innerList.getLength(); j++) {
                        Node innerNode = innerList.item(j);
                        if (innerNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) innerNode;
                            AttValueColor avc = new AttValueColor();
                            avc.name = eElement.getElementsByTagName("attribute").item(0).getTextContent();
                            avc.value = eElement.getElementsByTagName("value").item(0).getTextContent();
                            int r = Integer.parseInt(eElement.getElementsByTagName("r").item(0).getTextContent());
                            int g = Integer.parseInt(eElement.getElementsByTagName("g").item(0).getTextContent());
                            int b = Integer.parseInt(eElement.getElementsByTagName("b").item(0).getTextContent());
                            avc.color = new Color(r, g, b);
                            agentVC.add(avc);
                        }
                    }
                    vertexColorScheme = new VertexColorScheme(generalname,activityVC ,entityVC, agentVC, Boolean.parseBoolean(isAutomatic));
                    vertexModes.put(generalname, vertexColorScheme);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initialize Interface Filters
        InterfaceEdgeFilters();
        InterfaceVertexFilters();
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
        GraphFrame.edgeFilterList.setListData(types);
    }

    /**
     * Function to update the vertex filter list in the GraphFrame interface
     */
    private void InterfaceVertexFilters() {
        //Initialize Interface Filters
        String[] types = new String[vertexLabelFilter.size()];
        for (int x = 0; x < types.length; x++) {
            types[x] = vertexLabelFilter.get(x);
        }
        GraphFrame.vertexFilterList.setListData(types);
    }

    /**
     * Function to update the status filter list in the GraphFrame interface
     */
    private void InterfaceStatusFilters() {
        String[] items = new String[vertexModes.size()];
        int j = 0;
        for (ColorScheme mode : vertexModes.values()) {
            items[j] = mode.GetName();
            j++;
        }

        GraphFrame.StatusFilterBox.setModel(
                new DefaultComboBoxModel(items));
    }
}
