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

import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kohwalter
 */
public class UnityReader extends XMLReader{

    boolean isProvenanceGraph;
    public UnityReader(File fXmlFile) throws URISyntaxException, IOException {
        super(fXmlFile);
    }


    @Override
    public void readFile() {
        //Read all vertices
        readVertex();

        //Read all edges
        readEdge();
    }
    
    public void readVertex()
    {
        isProvenanceGraph = true;
        NodeList nList;
        nList = doc.getElementsByTagName("edgesRightToLeft");
        if (nList != null && nList.getLength() > 0 && !nList.item(0).getTextContent().equalsIgnoreCase("")) {
            isProvenanceGraph = Boolean.parseBoolean(nList.item(0).getTextContent());
        }

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
                
                Vertex node;
                if(type.equalsIgnoreCase("Activity"))
                {
                    node = new ActivityVertex(id, label, date);
                }
                else if(type.equalsIgnoreCase("Entity"))
                {
                    node = new EntityVertex(id, label, date);
                }
                else //Agent
                {
                    node = new AgentVertex(id, label, date);
                }
                
                NodeList aList = eElement.getElementsByTagName("attribute");
                boolean hasGraphFile = false;
                for(int i = 0; i < aList.getLength(); i++){
                    GraphAttribute att;
                    if(eElement.getElementsByTagName("name").item(i).getTextContent().equalsIgnoreCase("GraphFile")) {
                        hasGraphFile = true;
                    }
                    if(eElement.getElementsByTagName("min").item(i) != null && eElement.getElementsByTagName("max").item(i) != null && eElement.getElementsByTagName("quantity").item(i) != null) {
                        if(eElement.getElementsByTagName("originalValues").item(i) !=null) {
                            Node valuesList;
                            valuesList = eElement.getElementsByTagName("originalValues").item(i);
                            Element e = (Element) valuesList;
                            Collection<String> oValues = new ArrayList<String>(); 
                            for(int j = 0; j < Integer.valueOf(eElement.getElementsByTagName("quantity").item(i).getTextContent()); j++) {
                                oValues.add(e.getElementsByTagName("originalValue").item(j).getTextContent());
                            }
                            att = new GraphAttribute(eElement.getElementsByTagName("name").item(i).getTextContent(),
                            eElement.getElementsByTagName("value").item(i).getTextContent(),
                            eElement.getElementsByTagName("min").item(i).getTextContent(),
                            eElement.getElementsByTagName("max").item(i).getTextContent(),
                            eElement.getElementsByTagName("quantity").item(i).getTextContent(), oValues);
                        } else
                            att = new GraphAttribute(eElement.getElementsByTagName("name").item(i).getTextContent(),
                            eElement.getElementsByTagName("value").item(i).getTextContent(),
                            eElement.getElementsByTagName("min").item(i).getTextContent(),
                            eElement.getElementsByTagName("max").item(i).getTextContent(),
                            eElement.getElementsByTagName("quantity").item(i).getTextContent());
                    } else
                        att = new GraphAttribute(eElement.getElementsByTagName("name").item(i).getTextContent(),
                        eElement.getElementsByTagName("value").item(i).getTextContent());
                    node.addAttribute(att);
                }
                if(!hasGraphFile) {
                    GraphAttribute att = new GraphAttribute("GraphFile", file.getName());
                    node.addAttribute(att);
                }
                //String details = eElement.getElementsByTagName("details").item(0).getTextContent();
                //node.SetDetail(details);
                
                addNode(node);
            }
        }
    }
    
    public void readEdge()
    {
        NodeList nList;

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
                if(nodes.containsKey(source) && nodes.containsKey(target)) {
                    if(isProvenanceGraph)
                        addEdge(id, type, label, value, target, source);
                    else
                        addEdge(id, type, label, value, source, target);
                }
                    
            }
        }
    }
}
