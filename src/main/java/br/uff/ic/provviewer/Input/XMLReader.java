/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.EntityVertex;
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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kohwalter
 */
public class XMLReader {

    private Map<String, Vertex> nodes = new HashMap<String, Vertex>();
    private Collection<Edge> edges = new ArrayList<Edge>();

    public XMLReader(File fXmlFile) throws URISyntaxException, IOException {
        try {
            //URL location = XMLReader.class.getResource("/input.xml");

            //File fXmlFile = new File(location.getFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            
            NodeList nList;

            //Read all vertices
            nList = doc.getElementsByTagName("vertex");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String id = eElement.getElementsByTagName("ID").item(0).getTextContent();
                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                    String label = eElement.getElementsByTagName("label").item(0).getTextContent();
                    String date = eElement.getElementsByTagName("date").item(0).getTextContent();
                    String attribute = "";
                    NodeList aList = eElement.getElementsByTagName("attribute");
                    for(int i = 0; i < aList.getLength(); i++){
                        attribute += eElement.getElementsByTagName("name").item(i).getTextContent() + ": ";
                        attribute += eElement.getElementsByTagName("value").item(i).getTextContent() + " <br>";
                    }
                    String details = eElement.getElementsByTagName("details").item(0).getTextContent();
                    Vertex node;
                    if(type.equalsIgnoreCase("Activity"))
                    {
                        node = new ActivityVertex(id, label, date, attribute + " <br>" + details);
                    }
                    else if(type.equalsIgnoreCase("Entity"))
                    {
                        node = new EntityVertex(id, label, date, attribute + " <br>" + details);
                    }
                    else //Agent
                    {
                        node = new AgentVertex(id, label, date, attribute + " <br>" + details);
                    }
                    nodes.put(node.getID(), node);
                }
            }
            
            //Read all edges
            nList = doc.getElementsByTagName("edge");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String id = eElement.getElementsByTagName("ID").item(0).getTextContent();
                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                    String label = eElement.getElementsByTagName("label").item(0).getTextContent();
                    String value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    String source = eElement.getElementsByTagName("sourceID").item(0).getTextContent();
                    String target = eElement.getElementsByTagName("targetID").item(0).getTextContent();
                    Edge edge = new Edge(id, type, label, value, nodes.get(target), nodes.get(source));
                    edges.add(edge);
                    System.out.println(edge.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}
