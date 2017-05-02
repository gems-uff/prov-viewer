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
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TODO: Not working
 * @author Kohwalter
 */
public class PROVReader extends XMLReader {

    public PROVReader(File fXmlFile) throws URISyntaxException, IOException {
        super(fXmlFile);
    }

    @Override
    public void readFile() {
        System.out.println("Reading XML");
        NodeList nList;

//        nList = doc.getElementsByTagNameNS("*", "*");
        nList = doc.getChildNodes();//.getElementsByTagNameNS("*", "*");
        nList = nList.item(0).getChildNodes();
        System.out.println("getLength: " + nList.getLength());
        
//        Node nNode = doc.getFirstChild();
//        while(nNode.getNextSibling() != null){
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Map<String, GraphAttribute> attributes = new HashMap<String, GraphAttribute>();
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
                Vertex vertex;

                if (eElement.getTagName().equalsIgnoreCase("prov:entity")) {
                    if (eElement.getAttribute("prov:id") != null) {
                        id = eElement.getAttribute("prov:id");
                        vertex = new EntityVertex(id, label, time);
                        addNode(vertex);
                        System.out.println(vertex.toString());
                    }
                }
                if (eElement.getTagName().equalsIgnoreCase("prov:activity")) {
                    if (eElement.getAttribute("prov:id") != null) {
                        id = eElement.getAttribute("prov:id");
                        vertex = new ActivityVertex(id, label, time);
                        addNode(vertex);
                        System.out.println(vertex.toString());
                    }
                }
                if (eElement.getTagName().equalsIgnoreCase("prov:agent")) {
                    if (eElement.getAttribute("prov:id") != null) {
                        id = eElement.getAttribute("prov:id");
                        vertex = new AgentVertex(id, label, time);
                        addNode(vertex);
                        System.out.println(vertex.toString());
                    }
                }
                if (eElement.getTagName().equalsIgnoreCase("prov:used")) {
                    primaryTarget = ((Element) eElement.getElementsByTagName("prov:activity").item(0)).getAttribute("prov:ref");
                    primarySource = ((Element) eElement.getElementsByTagName("prov:entity").item(0)).getAttribute("prov:ref");
                    System.out.println("prov:used> target: " + primaryTarget + " / source: " + primarySource);
                    addEdge("edge", "prov:used", "prov:used", value, primaryTarget, primarySource);   
                }
                if (eElement.getTagName().equalsIgnoreCase("prov:wasGeneratedBy")) {
                    primaryTarget = ((Element) eElement.getElementsByTagName("prov:entity").item(0)).getAttribute("prov:ref");
                    primarySource = ((Element) eElement.getElementsByTagName("prov:activity").item(0)).getAttribute("prov:ref");
                    System.out.println("prov:wasGeneratedBy> target: " + primaryTarget + " / source: " + primarySource);
                    addEdge("edge", "prov:wasGeneratedBy", "prov:wasGeneratedBy", value, primaryTarget, primarySource);   
                }
                if (eElement.getTagName().equalsIgnoreCase("prov:wasAssociatedWith")) {
                    primaryTarget = ((Element) eElement.getElementsByTagName("prov:activity").item(0)).getAttribute("prov:ref");
                    primarySource = ((Element) eElement.getElementsByTagName("prov:agent").item(0)).getAttribute("prov:ref");
                    System.out.println("prov:wasAssociatedWith> target: " + primaryTarget + " / source: " + primarySource);
                    addEdge("edge", "prov:wasAssociatedWith", "prov:wasAssociatedWith", value, primaryTarget, primarySource);   
                }
                if (eElement.getTagName().equalsIgnoreCase("prov:specializationOf")) {
                    primaryTarget = ((Element) eElement.getElementsByTagName("prov:specificEntity").item(0)).getAttribute("prov:ref");
                    primarySource = ((Element) eElement.getElementsByTagName("prov:generalEntity").item(0)).getAttribute("prov:ref");
                    System.out.println("prov:specializationOf> target: " + primaryTarget + " / source: " + primarySource);
                    addEdge("edge", "prov:specializationOf", "prov:specializationOf", value, primaryTarget, primarySource);   
                }
                if (eElement.getTagName().equalsIgnoreCase("prov:alternateOf")) {
                    primaryTarget = ((Element) eElement.getElementsByTagName("prov:alternate1").item(0)).getAttribute("prov:ref");
                    primarySource = ((Element) eElement.getElementsByTagName("prov:alternate2").item(0)).getAttribute("prov:ref");
                    System.out.println("prov:alternateOf> target: " + primaryTarget + " / source: " + primarySource);
                    addEdge("edge", "prov:alternateOf", "prov:alternateOf", value, primaryTarget, primarySource);   
                }
            }
            nNode = nNode.getNextSibling();
        }
                
//        ReadEntity();
//        ReadActivity();
//        ReadAgent();
//        ReadGeneration();
//        ReadUsage();
//        ReadCommunication();
//        ReadStart();
//        ReadEnd();
//        ReadInvalidation();
////        ReadDerivation();
////        ReadRevision();
////        ReadQuotation();
////        PrimarySource();
//        ReadPerson();
//        ReadOrganization();
//        ReadSoftwareAgent();
//        ReadAttribution();
//        ReadAssociation();
////        ReadDelegation();
//        ReadInfluence();
////        ReadBundle();
//        ReadSpecialization();
//        ReadAlternate();
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
                
                Map<String, GraphAttribute> attributes  = new HashMap<String, GraphAttribute>();
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
                Vertex vertex;
                
//                if(eElement.getTagName().equalsIgnoreCase("prov:entity"))
//                {
//                    if(eElement.getAttribute("prov:id") != null) {
//                        id = eElement.getAttribute("prov:id");
//                        vertex = new EntityVertex(id, label, time);
//                        addNode(vertex);
//                        System.out.println(vertex.toString());
//                }
//                if(eElement.getTagName().equalsIgnoreCase("prov:activity"))
//                {
//                    if(eElement.getAttribute("prov:id") != null) {
//                        id = eElement.getAttribute("prov:id");
//                        vertex = new ActivityVertex(id, label, time);
//                        addNode(vertex);
//                        System.out.println(vertex.toString());
//                }
//                if(eElement.getTagName().equalsIgnoreCase("prov:agent"))
//                {
//                    if(eElement.getAttribute("prov:id") != null) {
//                        id = eElement.getAttribute("prov:id");
//                        vertex = new AgentVertex(id, label, time);
//                        addNode(vertex);
//                        System.out.println(vertex.toString());
//                }

//                
//              
//                // Initial Attributes
//                GetProvSecondaryAttributes(eElement, time, endTime, primarySource, 
//                        secondarySource, primaryTarget, secondaryTarget, plan, activityAsTarget);
//                // Prov attributes
////                GetProvPrimaryAttributes(eElement, label, location, role, type, value, id);
//                
//                // Add Prov Attributes
//                AddProvAttributes(attributes, location, role, type, value,
//                        endTime, plan);
//
//                // Add ##other attributes
//                HasOtherAttributes(nNode, attributes);
//                
////                HasOtherElements(nNode);
//                    
//                if(isVertex)
//                {
//                    Vertex vertex;
//                // Check vertex type
//                    if (elementType.equalsIgnoreCase("prov:activity")) {
//                        vertex = new ActivityVertex(id, label, time);
//                    } else if (elementType.equalsIgnoreCase("prov:entity")) {
//                        vertex = new EntityVertex(id, label, time);
//                    } else { //Agent
//                        vertex = new AgentVertex(id, label, time);
//                    }
//                    // Add attributes to vertex
//                    vertex.AddAllAttributes(attributes);
//                    System.out.println(vertex.toString());
//                    addNode(vertex);
//                }
//                else
//                {   // is Edge
//                    System.out.println(elementType);
//                    System.out.println("Edge: " + id + " " + primaryTarget + " " + primarySource);
//                    addEdge(id, "", type, value, label, attributes, primaryTarget, primarySource);
//                }
            }
        }
    }

    private void GetProvSecondaryAttributes(Element eElement, String time, String endTime,
            String primarySource, String secondarySource, String primaryTarget, String secondaryTarget,
            String plan, Boolean activityAsTarget) {
        if (eElement.getElementsByTagName("prov:startTime").item(0) != null) {
            time = eElement.getElementsByTagName("prov:startTime").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:endTime").item(0) != null) {
            endTime = eElement.getElementsByTagName("prov:endTime").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:time").item(0) != null) {
            time = eElement.getElementsByTagName("prov:time").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:plan").item(0) != null) {
            plan = eElement.getElementsByTagName("prov:plan").item(0).getTextContent();
        }
        
        // Assign source and target
        if(activityAsTarget)
        {
            if (eElement.getElementsByTagName("prov:entity").item(0) != null) {
                primarySource = eElement.getElementsByTagName("prov:entity").item(0).getTextContent();
            }
            if (eElement.getElementsByTagName("prov:activity").item(0) != null) {
                primaryTarget = eElement.getElementsByTagName("prov:activity").item(0).getTextContent();
            }
        }
        else
        {
            if (eElement.getElementsByTagName("prov:activity").item(0) != null) {
                primarySource = eElement.getElementsByTagName("prov:activity").item(0).getTextContent();
            }
            if (eElement.getElementsByTagName("prov:entity").item(0) != null) {
                primaryTarget = eElement.getElementsByTagName("prov:entity").item(0).getTextContent();
            }
        }
        // Communication
        if (eElement.getElementsByTagName("prov:informed").item(0) != null) {
            primarySource = eElement.getElementsByTagName("prov:informed").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:informant").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:informant").item(0).getTextContent();
        }
        
        // Trigger
        if (eElement.getElementsByTagName("prov:trigger").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:trigger").item(0).getTextContent();
        }
        
        // Starter
        if (eElement.getElementsByTagName("prov:starter").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:starter").item(0).getTextContent();
        }
        
        // Ender
        if (eElement.getElementsByTagName("prov:ender").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:ender").item(0).getTextContent();
        }
        
        // Agent
        if (eElement.getElementsByTagName("prov:agent").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:agent").item(0).getTextContent();
        }
        
        // Influence
        if (eElement.getElementsByTagName("prov:influencee").item(0) != null) {
            primarySource = eElement.getElementsByTagName("prov:influencee").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:influencer").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:influencer").item(0).getTextContent();
        }
        
        // Specialization
        if (eElement.getElementsByTagName("prov:specificEntity").item(0) != null) {
            primarySource = eElement.getElementsByTagName("prov:specificEntity").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:generalEntity").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:generalEntity").item(0).getTextContent();
        }
        
        // Alternate
        if (eElement.getElementsByTagName("prov:alternate1").item(0) != null) {
            primarySource = eElement.getElementsByTagName("prov:alternate1").item(0).getTextContent();
        }
        if (eElement.getElementsByTagName("prov:alternate2").item(0) != null) {
            primaryTarget = eElement.getElementsByTagName("prov:alternate2").item(0).getTextContent();
        }

        // TODO: Derivation and Delegation assignments
        
    }

    public void GetProvPrimaryAttributes(Element eElement, String label, String location, 
            String role, String type, String value, String id) {
        if (eElement.getElementsByTagName("prov:label").item(0) != null) {
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
            System.out.println("ID: " + id);
        }
    }

    public void AddProvAttributes(Map<String, GraphAttribute> attributes, String location, 
            String role, String type, String value, String endTime, String plan) {
        GraphAttribute att;
        if (!"".equals(endTime)) {
            att = new GraphAttribute("prov:endTime", endTime);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(location)) {
            att = new GraphAttribute("prov:location", location);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(role)) {
            att = new GraphAttribute("prov:role", role);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(type)) {
            att = new GraphAttribute("prov:type", type);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(value)) {
            att = new GraphAttribute("prov:value", value);
            attributes.put(att.getName(), att);
        }
        if (!"".equals(plan)) {
            att = new GraphAttribute("prov:plan", plan);
            attributes.put(att.getName(), att);
        }
    }

    public void HasOtherAttributes(Node nNode, Map<String, GraphAttribute> attributes) {
        GraphAttribute att;
        
        if (nNode.hasAttributes()) {
            NamedNodeMap nodeMap = nNode.getAttributes();

            for (int i = 0; i < nodeMap.getLength(); i++) {

                Node node = nodeMap.item(i);
                if(!node.getNodeName().equalsIgnoreCase("prov:id"))
                {
                    att = new GraphAttribute(node.getNodeName(), node.getNodeValue());
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
        System.out.println("Reading Entity");
        GetXMLValues("prov:entity", true, false);
    }

    private void ReadActivity() {
        GetXMLValues("prov:activity", true, false);
    }

    private void ReadAgent() {
        GetXMLValues("prov:agent", true, false);
    }

    private void ReadGeneration() {
        GetXMLValues("prov:wasGeneratedBy", false, true);
    }

    private void ReadUsage() {
        GetXMLValues("prov:used", false, false);
    }

    private void ReadCommunication() {
        GetXMLValues("prov:wasInformedBy", false, false);
    }

    private void ReadStart() {
        GetXMLValues("prov:wasStartedBy", false, false);
    }

    private void ReadEnd() {
        GetXMLValues("prov:wasEndedBy", false, false);
    }

    private void ReadInvalidation() {
        GetXMLValues("prov:wasInvalidatedBy", false, true);
    }

    private void ReadDerivation() {
        GetXMLValues("prov:wasDerivedFrom", false, false);
    }
    
    private void ReadPerson() {
        GetXMLValues("prov:person", true, false);
    }

    private void ReadOrganization() {
        GetXMLValues("prov:organization", true, false);
    }

    private void ReadSoftwareAgent() {
        GetXMLValues("prov:softwareAgent", true, false);
    }
    
    private void ReadAttribution() {
        GetXMLValues("prov:wasAttributedTo", false, false);
    }

    private void ReadAssociation() {
        GetXMLValues("prov:wasAssociatedWith", false, false);
    }

    private void ReadInfluence() {
        GetXMLValues("prov:wasInfluencedBy", false, false);
    }

    private void ReadSpecialization() {
        GetXMLValues("prov:specializationOf", false, false);
    }

    private void ReadAlternate() {
        GetXMLValues("prov:alternateOf", false, false);
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