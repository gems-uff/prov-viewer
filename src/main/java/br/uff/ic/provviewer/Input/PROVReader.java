/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kohwalter
 */
public class PROVReader extends XMLReader {

    public PROVReader(File fXmlFile) throws URISyntaxException, IOException {
        super(fXmlFile);
    }

    @Override
    public void ReadXML() {
        ReadEntity();
        ReadActivity();
        ReadAgent();
//        ReadGeneration();
//        ReadUsage();
//        ReadCommunication();
//        ReadStart();
//        ReadEnd();
//        ReadInvalidation();
//        ReadDerivation();
//        ReadRevision();
//        ReadQuotation();
//        PrimarySource();
//        ReadPerson();
//        ReadOrganization();
//        ReadSoftwareAgent();
//        ReadAttribution();
//        ReadAssociation();
//        ReadDelegation();
//        ReadInfluence();
//        ReadBundle();
//        ReadSpecialization();
//        ReadAlternate();
//        ReadCollection();
//        ReadEmptyCollection();
//        ReadMembership();
//        ReadPlan();
    }

    public void GetVertexValues(String elementType) {
        NodeList nList;

        nList = doc.getElementsByTagName(elementType);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Vertex vertex;
                // Primary Attributes
                String label = "";
                String location = "";
                String role = "";
                String type = "";
                String value = "";
                String id = "";
                // Secundary Attributes
                String time = "0";  // Time and startTime
                String endTime = "";

                // Initial Attributes
                GetProvVertexSecundaryAttributes(eElement, time, endTime);
                // Prov attributes
                GetProvAttributes(eElement, label, location, role, type, value, id);

                // Check vertex type
                if (type.equalsIgnoreCase("Activity")) {
                    vertex = new ActivityVertex(id, label, time, "");
                } else if (type.equalsIgnoreCase("Entity")) {
                    vertex = new EntityVertex(id, label, time, "");
                } else { //Agent
                    vertex = new AgentVertex(id, label, time, "");
                }

                // Add Prov Attributes
                AddProvAttributes(vertex, label, location, role, type, value, endTime);

                // Add ##other attributes
                HasOtherAttributes(nNode, vertex);

                AddNode(vertex);
            }
        }
    }

    public void ReadEntity() {
        GetVertexValues("Entity");
    }

    private void ReadActivity() {
        GetVertexValues("Activity");
    }

    private void ReadAgent() {
        GetVertexValues("Agent");
    }

    private void GetProvVertexSecundaryAttributes(Element eElement, String time, String endTime) {
        if (eElement.getElementsByTagName("startTime").item(0) != null) {
            time = eElement.getElementsByTagName("startTime").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("endTime").item(0) != null) {
            endTime = eElement.getElementsByTagName("endTime").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("time").item(0) != null) {
            time = eElement.getElementsByTagName("time").item(0).getTextContent();
        }
    }

    public void GetProvAttributes(Element eElement, String label, String location, String role, String type, String value, String id) {
        if (eElement.getElementsByTagName("label").item(0) != null) {
            label = eElement.getElementsByTagName("prov:label").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:location").item(0) != null) {
            location = eElement.getElementsByTagName("prov:location").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:role").item(0) != null) {
            role = eElement.getElementsByTagName("prov:role").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:type").item(0) != null) {
            type = eElement.getElementsByTagName("prov:type").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:value").item(0) != null) {
            value = eElement.getElementsByTagName("prov:value").item(0).getTextContent();
        }
        id = eElement.getAttribute("prov:id");
    }

    public void AddProvAttributes(Vertex vertex, String label, String location, String role, String type, String value, String endTime) {
        Attribute att;
        if (!"".equals(endTime)) {
            att = new Attribute("endTime", endTime);
            vertex.AddAttribute(att);
        }
        if (!"".equals(location)) {
            att = new Attribute("prov:location", location);
            vertex.AddAttribute(att);
        }
        if (!"".equals(role)) {
            att = new Attribute("prov:role", role);
            vertex.AddAttribute(att);
        }
        if (!"".equals(type)) {
            att = new Attribute("prov:type", type);
            vertex.AddAttribute(att);
        }
        if (!"".equals(value)) {
            att = new Attribute("prov:value", value);
            vertex.AddAttribute(att);
        }
    }

    public void HasOtherAttributes(Node nNode, Vertex vertex) {
        Attribute att;
        if (nNode.hasAttributes()) {
            NamedNodeMap nodeMap = nNode.getAttributes();

            for (int i = 0; i < nodeMap.getLength(); i++) {

                Node node = nodeMap.item(i);
                att = new Attribute(node.getNodeName(), node.getNodeValue());
                vertex.AddAttribute(att);
            }
        }
    }
}

//Generation
//entity        source
//activity      target
//time
//
//
//Usage
//activity      source
//entity        target
//time
//
//Communication
//informed      source
//informant     target
//
//Start
//activity      source
//trigger       target (entity) This
//starter       target (activity) Or this
//time
//
//End
//activity      source
//trigger       target (entity) This
//ender         target (activity) Or this
//time
//
//Invalidation
//entity        source
//activity      target
//time
//
//Derivation        Multiple edges...
//generatedEntity   source
//usedEntity        
//activity          
//generation
//usage
//
//Attribution
//entity        source
//agent         target
//
//Association
//activity      source
//agent         target
//plan          additional info
//
//Delegation
//delegate
//responsible
//activity
//
//Influence
//influencee    source
//influencer    target
//
//Specialization
//specificEntity    source
//generalEntity     target
//
//Alternate
//alternate1        source
//alternate2        target