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
    boolean hackSplitFilePath = true;

    public UnityReader(File fXmlFile) throws URISyntaxException, IOException {
        super(fXmlFile);
    }

    @Override
    public void readFile() {
        //Read all vertices
        readHeader();

        //Read all edges
//        readEdge();
    }

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
        readEdges(eList, edges);
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
                if (node instanceof GraphVertex) {
                    NodeList collapsedVertices = eElement.getElementsByTagName("collapsedvertices");
                    Node cNode = collapsedVertices.item(0);
                    if (cNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element cElement = (Element) cNode;
                        NodeList vList = cElement.getElementsByTagName("collapsedvertex");
                        NodeList eList = cElement.getElementsByTagName("collapsededge");
                        Map<String, Vertex> cv = new HashMap<>();
                        Collection<Edge> ce = new ArrayList<>();
                        readGraph(vList, eList, cv, ce);
                        ((GraphVertex) node).setClusterGraph(ce, cv.values());
                    }
                }
                vertices.put(node.getID(), node);
            }
        }
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
     * Method to read an edge
     */
    public void readEdges(NodeList nList, Collection<Edge> edges) {
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
                if (attributes.containsKey("NewSource")) {
                    newSource = attributes.get("NewSource").getValue();
                }
                if (attributes.containsKey("NewTarget")) {
                    newTarget = attributes.get("NewTarget").getValue();
                }
                if ((nodes.containsKey(source) || nodes.containsKey(newSource)) && (nodes.containsKey(target) || nodes.containsKey(newTarget))) {
                    Vertex vs;
                    Vertex vt;
                    if (nodes.containsKey(source)) {
                        vs = nodes.get(source);
                    } else {
                        vs = nodes.get(attributes.get("NewSource").getValue());
//                        vs = getNode(nodes, source, attributes, "NewSource");
                    }
                    if (nodes.containsKey(target)) {
                        vt = nodes.get(target);
                    } else {
                        vt = nodes.get(attributes.get("NewTarget").getValue());
//                        vt = getNode(nodes, target, attributes, "NewTarget");
                    }

                    if (isProvenanceGraph) {
                        edge = new Edge(id, type, label, value, vt, vs);
                    } //                        addEdge(id, type, label, value, target, source);
                    else {
                        edge = new Edge(id, type, label, value, vs, vt);
                    }
//                        addEdge(id, type, label, value, source, target);
//                    readAttribute(eElement, edge);
                    edge.addAllAttributes(attributes);
//                    addEdge(edge);
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
    
    private Vertex getNode(Map<String, Vertex> n, String source, Map<String, GraphAttribute> attributes, String tag) {
        if (n.containsKey(source)) {
            return n.get(source);
        } else {
            GraphVertex gv = (GraphVertex) n.get(attributes.get(tag).getValue());
            return findSource(gv, source);
        }
    }
    
    private Vertex findSource(GraphVertex gv, String source) {
        for(Object v : gv.clusterGraph.getVertices()) {
            if(v instanceof GraphVertex)
                findSource((GraphVertex) v, source);
            else if(((Vertex)v).getID().equalsIgnoreCase(source))
                return (Vertex) v;
        }
        return null;
    }
}
