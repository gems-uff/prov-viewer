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

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.GraphVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kohwalter
 */
public class UnityReader extends XMLReader {

    boolean isProvenanceGraph;
    boolean hackLabelPathAndFile = false;
    boolean hackSplitFilePath = false;

    public UnityReader(File fXmlFile) throws URISyntaxException, IOException {
        super(fXmlFile);
    }

    /**
     * Method to read the file
     */
    @Override
    public void readFile() {
        readHeader();
    }
    
    /**
     * Method to read the attributes from the graph object (can be either an
     * edge or a vertex)
     *
     * @param element
     * @param node
     */
    public void readAttribute(Element element, Map<String, GraphAttribute> attributes) {
        NodeList attributesList = element.getElementsByTagName("attributes");
        if (attributesList.getLength() > 0) {
            Node nNode = attributesList.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                NodeList aList = eElement.getElementsByTagName("attribute");
                boolean hasGraphFile = false;
                for (int i = 0; i < aList.getLength(); i++) {
                    GraphAttribute att;
                    if (eElement.getElementsByTagName("name").item(i).getTextContent().equalsIgnoreCase("GraphFile")) {
                        hasGraphFile = true;
                    }
                    if (hackSplitFilePath) {
                        if (eElement.getElementsByTagName("name").item(i).getTextContent().equalsIgnoreCase("Path")) {
                            Collection<GraphAttribute> mPath = new ArrayList<>();
                            String[] paths = eElement.getElementsByTagName("value").item(i).getTextContent().split("/");
                            int j = 0;
                            for (String s : paths) {
                                mPath.add(new GraphAttribute("Path_#" + j, s));
                                j++;
                            }
                            for (GraphAttribute ga : mPath) {
                                attributes.put(ga.getName(), ga);
                            }
                        }
                    }
                    if (eElement.getElementsByTagName("min").item(i) != null
                            && eElement.getElementsByTagName("max").item(i) != null
                            && eElement.getElementsByTagName("quantity").item(i) != null) {
                        if (eElement.getElementsByTagName("originalValues").item(i) != null) {
                            Node valuesList;
                            valuesList = eElement.getElementsByTagName("originalValues").item(i);
                            Element e = (Element) valuesList;
                            Collection<String> oValues = new ArrayList<String>();
                            for (int j = 0; j < Integer.valueOf(eElement.getElementsByTagName("quantity").item(i).getTextContent()); j++) {
                                oValues.add(e.getElementsByTagName("originalValue").item(j).getTextContent());
                            }
                            att = new GraphAttribute(eElement.getElementsByTagName("name").item(i).getTextContent(),
                                    eElement.getElementsByTagName("value").item(i).getTextContent(),
                                    eElement.getElementsByTagName("min").item(i).getTextContent(),
                                    eElement.getElementsByTagName("max").item(i).getTextContent(),
                                    eElement.getElementsByTagName("quantity").item(i).getTextContent(), oValues);
                        } else {
                            att = new GraphAttribute(eElement.getElementsByTagName("name").item(i).getTextContent(),
                                    eElement.getElementsByTagName("value").item(i).getTextContent(),
                                    eElement.getElementsByTagName("min").item(i).getTextContent(),
                                    eElement.getElementsByTagName("max").item(i).getTextContent(),
                                    eElement.getElementsByTagName("quantity").item(i).getTextContent());
                        }
                    } else {
                        att = new GraphAttribute(eElement.getElementsByTagName("name").item(i).getTextContent(),
                                eElement.getElementsByTagName("value").item(i).getTextContent());
                    }
//                    node.addAttribute(att);
                    attributes.put(att.getName(), att);
                }
                if (!hasGraphFile) {
                    GraphAttribute att = new GraphAttribute(VariableNames.GraphFile, file.getName());
//                    node.addAttribute(att);
                    attributes.put(att.getName(), att);
                }
            }
        } else {
            GraphAttribute att = new GraphAttribute(VariableNames.GraphFile, file.getName());
//            node.addAttribute(att);
            attributes.put(att.getName(), att);
        }
    }

    /**
     * Method that reads the file Header and then the vertex and edge lists (Graph)
     */
    public void readHeader() {
        isProvenanceGraph = true;
        NodeList nList;
        nList = doc.getElementsByTagName("edgesRightToLeft");
        if (nList != null && nList.getLength() > 0 && !nList.item(0).getTextContent().equalsIgnoreCase("")) {
            isProvenanceGraph = Boolean.parseBoolean(nList.item(0).getTextContent());
        }
        NodeList vList = doc.getElementsByTagName("vertex");
        NodeList eList = doc.getElementsByTagName("edge");
        readGraph(vList, eList, nodes, edges);
    }

    public void readGraph(NodeList vList, NodeList eList, Map<String, Vertex> vertices, Collection<Edge> edges) {
        readVertices(vList, vertices);
        readEdges(eList, edges, vertices);
    }

    /**
     * Method to read a vertex
     * @param nList
     * @param vertices
     */
    public void readVertices(NodeList nList, Map<String, Vertex> vertices) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String id = eElement.getElementsByTagName("ID").item(0).getTextContent();
                String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                String label = eElement.getElementsByTagName("label").item(0).getTextContent();
                String date = eElement.getElementsByTagName("date").item(0).getTextContent();

                Map<String, GraphAttribute> attributes = new HashMap<>();
                readAttribute(eElement, attributes);

                GraphAttribute path = null;
                GraphAttribute fileName = null;
                if (hackLabelPathAndFile) {
                    String[] folders = label.split("/");
                    String filePath = label.replace(folders[folders.length - 1], "");
                    String fn = folders[folders.length - 1];
                    path = new GraphAttribute("Path", filePath);
                    fileName = new GraphAttribute("FileName", fn);
                    if (type.equalsIgnoreCase("Entity")) {
                        label = fn;
                    }
                    if (type.equalsIgnoreCase("Activity")) {
                        label = label.split(":")[0];
                    }
                }
                Vertex node;
                if (type.equalsIgnoreCase("Activity")) {
                    node = new ActivityVertex(id, label, date);
                } else if (type.equalsIgnoreCase("Entity")) {
                    node = new EntityVertex(id, label, date);
                } else if (type.equalsIgnoreCase("Agent")) {
                    node = new AgentVertex(id, label, date);
                } else {
                    node = new GraphVertex(id, label, date);
                }
//                readAttribute(eElement, node);
                if (hackLabelPathAndFile) {
                    if (node instanceof EntityVertex) {
                        node.addAttribute(path);
                        node.addAttribute(fileName);
                    }
                }
                node.addAllAttributes(attributes);
//                System.out.println("Reading vertex: " + id);
                if (node instanceof GraphVertex) {
//                    System.out.println("Its a GraphVertex!");
                    NodeList graph = eElement.getElementsByTagName("Graph");
                    Node gNode = graph.item(0);
                    Element graphElement = (Element) gNode;
                    NodeList collapsedVertices = graphElement.getElementsByTagName("collapsedvertices");
                    NodeList collapsedEdged = graphElement.getElementsByTagName("collapsededges");
                    Node cVertexNode = collapsedVertices.item(0);
                    Node cEdgeNode = collapsedEdged.item(collapsedEdged.getLength() - 1);
                    if (cVertexNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element cVertexElement = (Element) cVertexNode;
                        Element cEdgeElement = (Element) cEdgeNode;
//                        NodeList vList = cVertexElement.getElementsByTagName("collapsedvertex");
//                        NodeList eList = cEdgeElement.getElementsByTagName("collapsededge");
                        NodeList vList = cVertexElement.getChildNodes();
                        NodeList eList = cEdgeElement.getChildNodes();
                        Map<String, Vertex> cv = new HashMap<>();
                        Collection<Edge> ce = new ArrayList<>();
                        readGraph(vList, eList, cv, ce);
                        ((GraphVertex) node).setClusterGraph(ce, cv);
                    }
//                    System.out.println("Finished Reading GraphVertex!");
                }
                vertices.put(node.getID(), node);
            }
        }
    }

    /**
     * Method to read an edge
     */
    public void readEdges(NodeList nList, Collection<Edge> edges, Map<String, Vertex> vertices) {
//        System.out.println("Edge list size: " + nList.getLength());
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
                Edge edge;
                Map<String, GraphAttribute> attributes = new HashMap<>();
                readAttribute(eElement, attributes);
                String newSource = "Null";
                String newTarget = "Null";
                if (attributes.containsKey(VariableNames.vertexNewSource)) {
                    newSource = attributes.get(VariableNames.vertexNewSource).getValue();
                }
                if (attributes.containsKey(VariableNames.vertexNewTarget)) {
                    newTarget = attributes.get(VariableNames.vertexNewTarget).getValue();
                }
                if ((vertices.containsKey(source) || vertices.containsKey(newSource)) && (vertices.containsKey(target) || vertices.containsKey(newTarget))) {
                    Vertex vs;
                    Vertex vt;
                    vs = findNode(source, vertices);
                    vt = findNode(target, vertices);

                    if (isProvenanceGraph) {
                        edge = new Edge(id, type, label, value, vt, vs);
                    }
                    else {
                        edge = new Edge(id, type, label, value, vs, vt);
                    }
                    edge.addAllAttributes(attributes);
                    edges.add(edge);
                } else {
                    System.out.println("Not possible!");
                    System.out.println("ID: " + id);
                    System.out.println("source: " + source);
                    System.out.println("target: " + target);
                    System.out.println("NewSource: " + newSource);
                    System.out.println("NewTarget: " + newTarget);
                }
            }
        }
    }

    /**
     * Method to find the original Vertex 
     * @param ID is the vertex ID we are looking for
     * @param vertices is the map that contains the vertices with the Key being the vertex's ID
     * @return the vertex
     */
    private Vertex findNode(String ID, Map<String, Vertex> vertices) {
        if (vertices.containsKey(ID)) {
            return vertices.get(ID);
        } else {
            for(String ids : vertices.keySet()) {
                if(ids.contains(ID)) { // It might have. Lets break it down to make sure it has the Source
                    String currentID = ids.replace("[", ""); // Remove the GraphVertex ID brankets
                    currentID = currentID.replace("]", "");
                    currentID = currentID.replace(" ", ""); // Remove spaces
                    String[] subIDs = currentID.split(","); // Split each ID that composes the GraphVertex
                    for(String i : subIDs) { // Lets check if each individual ID is the original Source
                        if(i.equalsIgnoreCase(ID)) { // If true then the original vertex is inside this GraphVertex
                            GraphVertex v = (GraphVertex) vertices.get(ids); // We got the GraphVertex, now need to explore it
                            return findNode(v.clusterGraph, ID);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Method to find the vertex inside a GraphVertex
     * @param clusterGraph is the GraphVertex's clustergraph
     * @param ID is the vertex's ID that we are looking for
     * @return the vertex
     */
    private Vertex findNode(Graph clusterGraph, String ID) {
        Vertex found = null;
        for(Object v : clusterGraph.getVertices()) {
            if(((Vertex)v).getID().equalsIgnoreCase(ID))
                return (Vertex) v;
            else if(v instanceof GraphVertex)
                found = findNode(((GraphVertex)v).clusterGraph, ID);
        }
        return found;
    }
    
    /**
     * Method that returns 
     * @param id
     * @return 
     */
    public Vertex getNewPointer(String id) {
        return nodes.get(id);
    }
}
