/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author Kohwalter
 */
public abstract class XMLReader {

    Map<String, Vertex> nodes;
    Collection<Edge> edges;
    Document doc;

    /**
     * Return edges
     *
     * @return
     */
    public Collection<Edge> getEdges() {
        return edges;
    }

    /**
     * Return vertices
     *
     * @return
     */
    public Collection<Vertex> getNodes() {
        return nodes.values();
    }

    public XMLReader(File fXmlFile) throws URISyntaxException, IOException {
        try {
//            URL location = XMLReader.class.getResource("/input.xml");
//            File fXmlFile = new File(location.getFile());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            
            nodes = new HashMap<String, Vertex>();
            edges = new ArrayList<Edge>();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void AddEdge(String id, String influence, String type, String value, String label, Map<String, Attribute> attributes, String target, String source)
    {
        Edge edge = new Edge(id, influence, type, value, label, attributes, nodes.get(target), nodes.get(source));
        edges.add(edge);
    }

    /**
     * Create the edge and add to the Map
     * @param id
     * @param influence
     * @param type
     * @param value
     * @param label
     * @param target
     * @param source 
     */
    public void AddEdge(String id, String influence, String type, String value, String label, String target, String source)
    {
        Edge edge = new Edge(id, influence, type, value, label, nodes.get(target), nodes.get(source));
        edges.add(edge);
    }
    
    /**
     *  Overload method from AddEdge without Label
     * @param id
     * @param influence
     * @param type
     * @param value
     * @param target
     * @param source 
     */
    public void AddEdge(String id, String influence, String type, String value, String target, String source)
    {
        AddEdge(id, influence, type, value, "", target, source);
    }
    
    /**
     * Method to add Vertex to the Map
     * @param node 
     */
    public void AddNode(Vertex node)
    {
        nodes.put(node.getID(), node);
    }
    
    /**
     * Abstract method to read a XML file depending on the XML-Schema
     */
    public abstract void ReadXML();

}
