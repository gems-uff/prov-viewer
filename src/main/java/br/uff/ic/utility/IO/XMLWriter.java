/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.IO;

import br.uff.ic.utility.Attribute;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 *
 * @author Kohwalter
 */

//    @XmlArray("provenancedata")
public class XMLWriter {
//    @XmlArray("vertices")
//    @XmlArrayItem("vertex")

    public Collection<Object> vertexList;

//    @XmlArray("edges")
//    @XmlArrayItem("edge")   			
    public Collection<Edge> edgeList;

    //==========================================================================
    // Constructor
    //==========================================================================
    public XMLWriter(Collection<Object> vertices, Collection<Edge> edges) {
        vertexList = vertices;
        edgeList = edges;
    }

    public void saveToXML(String fileName) throws FileNotFoundException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.setXmlStandalone(true);
            Element rootElement = doc.createElement("provenancedata");
            doc.appendChild(rootElement);
            Element vertices = doc.createElement("vertices");
            rootElement.appendChild(vertices);

            for (int i = 0; i < vertexList.size(); i++) {
                Element vertex = doc.createElement("vertex");
                vertices.appendChild(vertex);

                Element id = doc.createElement("ID");
                id.setTextContent(((Vertex) vertexList.toArray()[i]).getID());
                vertex.appendChild(id);

                String vertexType;
                if (vertexList.toArray()[i] instanceof AgentVertex) {
                    vertexType = "Agent";
                } else if (vertexList.toArray()[i] instanceof ActivityVertex) {
                    vertexType = "Activity";
                } else {
                    vertexType = "Entity";
                }
                Element type = doc.createElement("type");
                type.setTextContent(vertexType);
                vertex.appendChild(type);

                Element label = doc.createElement("label");
                label.setTextContent(((Vertex) vertexList.toArray()[i]).getLabel());
                vertex.appendChild(label);

                Element data = doc.createElement("date");
                data.setTextContent(((Vertex) vertexList.toArray()[i]).getTimeString());
                vertex.appendChild(data);

                Element atts = doc.createElement("attributes");
                vertex.appendChild(atts);

                Object[] attributes = null;
                attributes = ((Vertex) vertexList.toArray()[i]).attributes.values().toArray();

                for (int j = 0; j < attributes.length; j++) {
                    Element att = doc.createElement("attribute");
                    atts.appendChild(att);
                    
                    Element name = doc.createElement("name");
                    name.setTextContent(((Attribute) attributes[j]).getName());
                    att.appendChild(name);
                    Element value = doc.createElement("value");
                    value.setTextContent(((Attribute) attributes[j]).getValue());
                    att.appendChild(value);
                }

            }

            Element edges = doc.createElement("edges");
            rootElement.appendChild(edges);
            for (int i = 0; i < edgeList.size(); i++) {
                Element edge = doc.createElement("edge");
                edges.appendChild(edge);

                Element id = doc.createElement("ID");
                id.setTextContent(((Edge) edgeList.toArray()[i]).getID());
                edge.appendChild(id);

                Element type = doc.createElement("type");
                type.setTextContent(((Edge) edgeList.toArray()[i]).getType());
                edge.appendChild(type);

                Element label = doc.createElement("label");
                label.setTextContent(((Edge) edgeList.toArray()[i]).getLabel());
                edge.appendChild(label);

                Element value = doc.createElement("value");
                value.setTextContent(Float.toString(((Edge) edgeList.toArray()[i]).getValue()));
                edge.appendChild(value);

                Element source = doc.createElement("sourceID");
                source.setTextContent(((Vertex) ((Edge) edgeList.toArray()[i]).getSource()).getID());
                edge.appendChild(source);

                Element target = doc.createElement("targetID");
                target.setTextContent(((Vertex) ((Edge) edgeList.toArray()[i]).getTarget()).getID());
                edge.appendChild(target);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource dom = new DOMSource(doc);
            String path = fileName + ".xml";//Output" + File.separator + fileName + ".xml";
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(dom, result);
            System.out.println("XML File was created: " + path);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
