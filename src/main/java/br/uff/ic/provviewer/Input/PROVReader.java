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
import java.util.HashMap;
import java.util.Map;
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
        ReadGeneration();
        ReadUsage();
        ReadCommunication();
        ReadStart();
        ReadEnd();
        ReadInvalidation();
//        ReadDerivation();
//        ReadRevision();
//        ReadQuotation();
//        PrimarySource();
        ReadPerson();
        ReadOrganization();
        ReadSoftwareAgent();
        ReadAttribution();
        ReadAssociation();
//        ReadDelegation();
        ReadInfluence();
//        ReadBundle();
        ReadSpecialization();
        ReadAlternate();
//        ReadCollection();
//        ReadEmptyCollection();
//        ReadMembership();
//        ReadPlan();
    }

    public void GetXMLValues(String elementType, Boolean isVertex, Boolean activityAsTarget) {
        NodeList nList;

        nList = doc.getElementsByTagName(elementType);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                
                Map<String, Attribute> attributes  = new HashMap<String, Attribute>();

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
                String primarySource = "";
                String secondarySource = "";
                String primaryTarget = "";
                String secondaryTarget = "";
                String plan = "";
              
                // Initial Attributes
                GetProvSecondaryAttributes(eElement, time, endTime, primarySource, 
                        secondarySource, primaryTarget, secondaryTarget, plan, activityAsTarget);
                // Prov attributes
                GetProvPrimaryAttributes(eElement, label, location, role, type, value, id);
                
                // Add Prov Attributes
                AddProvAttributes(attributes, location, role, type, value,
                        endTime, plan);

                // Add ##other attributes
                HasOtherAttributes(nNode, attributes);
                
//                HasOtherElements(nNode);
                    
                if(isVertex)
                {
                    Vertex vertex;
                // Check vertex type
                    if (elementType.equalsIgnoreCase("Activity")) {
                        vertex = new ActivityVertex(id, label, time, "");
                    } else if (elementType.equalsIgnoreCase("Entity")) {
                        vertex = new EntityVertex(id, label, time, "");
                    } else { //Agent
                        vertex = new AgentVertex(id, label, time, "");
                    }
                    // Add attributes to vertex
                    vertex.AddAllAttributes(attributes);

                    AddNode(vertex);
                }
                else
                {   // is Edge
                    AddEdge(id, "", type, value, label, attributes, primaryTarget, primarySource);
                }
            }
        }
    }

    private void GetProvSecondaryAttributes(Element eElement, String time, String endTime,
            String primarySource, String secondarySource, String primaryTarget, String secondaryTarget,
            String plan, Boolean activityAsTarget) {
        if (eElement.getElementsByTagName("startTime").item(0) != null) {
            time = eElement.getElementsByTagName("startTime").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("endTime").item(0) != null) {
            endTime = eElement.getElementsByTagName("endTime").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("time").item(0) != null) {
            time = eElement.getElementsByTagName("time").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("plan").item(0) != null) {
            plan = eElement.getElementsByTagName("plan").item(0).getTextContent();
        }
        
        // Assign source and target
        if(activityAsTarget)
        {
            if (eElement.getElementsByTagName("entity").item(0) != null) {
                primarySource = eElement.getElementsByTagName("entity").item(0).getTextContent();
            }
            if (eElement.getElementsByTagName("activity").item(0) != null) {
                primaryTarget = eElement.getElementsByTagName("activity").item(0).getTextContent();
            }
        }
        else
        {
            if (eElement.getElementsByTagName("activity").item(0) != null) {
                primarySource = eElement.getElementsByTagName("activity").item(0).getTextContent();
            }
            if (eElement.getElementsByTagName("entity").item(0) != null) {
                primaryTarget = eElement.getElementsByTagName("entity").item(0).getTextContent();
            }
        }
        // Communication
        if (eElement.getElementsByTagName("informed").item(0) != null) {
            primarySource = eElement.getElementsByTagName("informed").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("informant").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("informant").item(0).getTextContent();
        }
        
        // Trigger
        if (eElement.getElementsByTagName("trigger").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("trigger").item(0).getTextContent();
        }
        
        // Starter
        if (eElement.getElementsByTagName("starter").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("starter").item(0).getTextContent();
        }
        
        // Ender
        if (eElement.getElementsByTagName("ender").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("ender").item(0).getTextContent();
        }
        
        // Agent
        if (eElement.getElementsByTagName("agent").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("agent").item(0).getTextContent();
        }
        
        // Influence
        if (eElement.getElementsByTagName("influencee").item(0) != null) {
            primarySource = eElement.getElementsByTagName("influencee").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("influencer").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("influencer").item(0).getTextContent();
        }
        
        // Specialization
        if (eElement.getElementsByTagName("specificEntity").item(0) != null) {
            primarySource = eElement.getElementsByTagName("specificEntity").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("generalEntity").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("generalEntity").item(0).getTextContent();
        }
        
        // Alternate
        if (eElement.getElementsByTagName("alternate1").item(0) != null) {
            primarySource = eElement.getElementsByTagName("alternate1").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("alternate2").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("alternate2").item(0).getTextContent();
        }

        // TODO: Derivation and Delegation assignments
        
    }

    public void GetProvPrimaryAttributes(Element eElement, String label, String location, 
            String role, String type, String value, String id) {
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
        if(eElement.getAttribute("prov:id") != null) {
            id = eElement.getAttribute("prov:id");
        }
    }

    public void AddProvAttributes(Map<String, Attribute> attributes, String location, 
            String role, String type, String value, String endTime, String plan) {
        Attribute att;
        if (!"".equals(endTime)) {
            att = new Attribute("endTime", endTime);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(location)) {
            att = new Attribute("prov:location", location);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(role)) {
            att = new Attribute("prov:role", role);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(type)) {
            att = new Attribute("prov:type", type);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(value)) {
            att = new Attribute("prov:value", value);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(plan)) {
            att = new Attribute("plan", plan);
            attributes.put(att.getName(), att);
        }
    }

    public void HasOtherAttributes(Node nNode, Map<String, Attribute> attributes) {
        Attribute att;
        
        if (nNode.hasAttributes()) {
            NamedNodeMap nodeMap = nNode.getAttributes();

            for (int i = 0; i < nodeMap.getLength(); i++) {

                Node node = nodeMap.item(i);
                if(!node.getNodeName().equalsIgnoreCase("prov:id"))
                {
                    att = new Attribute(node.getNodeName(), node.getNodeValue());
                    attributes.put(att.getName(), att);
                }
            }
        }
    }
    
    private void HasOtherElements(Node nNode) {
        throw new UnsupportedOperationException("Not yet implemented");
        //TODO: Ignore known prov attributes
    }
    
    public void ReadEntity() {
        GetXMLValues("Entity", true, false);
    }

    private void ReadActivity() {
        GetXMLValues("Activity", true, false);
    }

    private void ReadAgent() {
        GetXMLValues("Agent", true, false);
    }

    private void ReadGeneration() {
        GetXMLValues("Generation", false, true);
    }

    private void ReadUsage() {
        GetXMLValues("Usage", false, false);
    }

    private void ReadCommunication() {
        GetXMLValues("Communication", false, false);
    }

    private void ReadStart() {
        GetXMLValues("Start", false, false);
    }

    private void ReadEnd() {
        GetXMLValues("End", false, false);
    }

    private void ReadInvalidation() {
        GetXMLValues("Invalidation", false, true);
    }

    private void ReadPerson() {
        GetXMLValues("Person", true, false);
    }

    private void ReadOrganization() {
        GetXMLValues("Organization", true, false);
    }

    private void ReadSoftwareAgent() {
        GetXMLValues("SoftwareAgent", true, false);
    }
    
    private void ReadAttribution() {
        GetXMLValues("Attribution", false, false);
    }

    private void ReadAssociation() {
        GetXMLValues("Association", false, false);
    }

    private void ReadInfluence() {
        GetXMLValues("Influence", false, false);
    }

    private void ReadSpecialization() {
        GetXMLValues("Specialization", false, false);
    }

    private void ReadAlternate() {
        GetXMLValues("Alternate", false, false);
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