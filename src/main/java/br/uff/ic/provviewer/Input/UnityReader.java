/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kohwalter
 */
public class UnityReader extends XMLReader{

    public UnityReader(File fXmlFile) throws URISyntaxException, IOException {
        super(fXmlFile);
    }


    @Override
    public void ReadXML() {
        //Read all vertices
        ReadVertex();

        //Read all edges
        ReadEdge();
    }
    
    public void ReadVertex()
    {
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
                
                Vertex node;
                if(type.equalsIgnoreCase("Activity"))
                {
                    node = new ActivityVertex(id, label, date, "");
                }
                else if(type.equalsIgnoreCase("Entity"))
                {
                    node = new EntityVertex(id, label, date, "");
                }
                else //Agent
                {
                    node = new AgentVertex(id, label, date, "");
                }
                
                NodeList aList = eElement.getElementsByTagName("attribute");
                for(int i = 0; i < aList.getLength(); i++){
                    Attribute att = new Attribute(eElement.getElementsByTagName("name").item(i).getTextContent(),
                    eElement.getElementsByTagName("value").item(i).getTextContent());
                    node.AddAttribute(att);
                }
                
                String details = eElement.getElementsByTagName("details").item(0).getTextContent();
                node.SetDetail(details);
                
                AddNode(node);
            }
        }
    }
    
    public void ReadEdge()
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
                AddEdge(id, type, label, value, target, source);
            }
        }
    }
}
