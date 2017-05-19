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

package br.uff.ic.utility.IO;

import br.uff.ic.utility.GraphAttribute;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Kohwalter
 */

//    @XmlArray("provenancedata")
public final class XMLWriter {
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
        
        sortLists();
    }
       
    public void sortLists() {
        //Sort vertexList by name
        List<Object> vList = new ArrayList<Object>( vertexList );
        Comparator<Object> comparator = new Comparator<Object>() {
            public int compare(Object c1, Object c2) {
                return ((Vertex)c1).getID().compareTo(((Vertex)c2).getID());
            }
        };

        Collections.sort(vList, comparator);
        vertexList = vList;
        
        //Sort edgeList by name
        List<Edge> eList = new ArrayList<Edge>( edgeList );
        Comparator<Edge> comparator2 = new Comparator<Edge>() {
            public int compare(Edge c1, Edge c2) {
                return ((Edge)c1).getType().compareTo(((Edge)c2).getType());
            }
        };

        Collections.sort(eList, comparator2);
        edgeList = eList;
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
                
                Object[] attributes = null;
                attributes = ((Vertex) vertexList.toArray()[i]).attributes.values().toArray();
                addAttributes(doc, vertex, attributes);
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
                
                Object[] attributes = null;
                attributes = ((Edge) edgeList.toArray()[i]).attributes.values().toArray();
                addAttributes(doc, edge, attributes);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource dom = new DOMSource(doc);
            String path = fileName + ".xml";//Output" + File.separator + fileName + ".xml";
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(dom, result);
//            System.out.println("XML File was created: " + path);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void addAttributes(Document doc, Element graphObject, Object[] attributes) {
        Element atts = doc.createElement("attributes");
        graphObject.appendChild(atts);

        

        for (int j = 0; j < attributes.length; j++) {
            Element att = doc.createElement("attribute");
            atts.appendChild(att);

            Element name = doc.createElement("name");
            name.setTextContent(((GraphAttribute) attributes[j]).getName());
            att.appendChild(name);

            Element value = doc.createElement("value");
            value.setTextContent(((GraphAttribute) attributes[j]).getValue());
            att.appendChild(value);
            if(Integer.parseInt(((GraphAttribute) attributes[j]).getQuantity()) > 1) {
                Element min = doc.createElement("min");
                min.setTextContent(((GraphAttribute) attributes[j]).getMin());
                att.appendChild(min);

                Element max = doc.createElement("max");
                max.setTextContent(((GraphAttribute) attributes[j]).getMax());
                att.appendChild(max);

                Element quantity = doc.createElement("quantity");
                quantity.setTextContent(((GraphAttribute) attributes[j]).getQuantity());
                att.appendChild(quantity);

                Element values = doc.createElement("originalValues");
                att.appendChild(values);
                Object[] originalValues = null;
                originalValues = ((GraphAttribute) attributes[j]).getValues().toArray();
                for(Object s : originalValues) {
                    Element originalValue = doc.createElement("originalValue");
                    originalValue.setTextContent((String)s);
                    values.appendChild(originalValue);
                }
            }
        }
    }
}
