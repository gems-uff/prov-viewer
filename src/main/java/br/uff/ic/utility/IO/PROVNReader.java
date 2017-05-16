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
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.GraphObject;
import br.uff.ic.utility.graph.Vertex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Kohwalter
 */
public class PROVNReader extends InputReader {

    int edgeOptionalID = 0;

    public PROVNReader(File f) throws URISyntaxException, IOException {
        super(f);
    }

    public void readFile() throws URISyntaxException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            String line = br.readLine();

            Vertex node = new AgentVertex("Unknown Agent", "Unknown Agent", "");
            addNode(node);
            node = new ActivityVertex("Unknown Activity", "Unknown Activity", "");
            addNode(node);
            node = new EntityVertex("Unknown Entity", "Unknown Entity", "");
            addNode(node);

            while (line != null) {
                Read(line);
                line = br.readLine();
            }
        } finally {
            br.close();
        }

    }

    public void Read(String line) {
        String[] elements;
        String[] statement;
        String[] attributes;
        String[] optionalAttributes = null;

        line = line.replace(")", "");
        line = line.replace(" ", "");
        line = line.replace("\t", "");
        line = line.replace("]", "");
//        line = line.replace("'", "");
        elements = line.split("\\(");
        if (elements.length > 1) {
            statement = elements[1].split("\\[");
            attributes = statement[0].split(",");

            if (statement.length > 1) {
                optionalAttributes = statement[1].split(",");
            }

            if (elements[0].contains("entity")) {
                readEntity(attributes, optionalAttributes);
            }
            if (elements[0].contains("activity")) {
                readActivity(attributes, optionalAttributes);
            }
            if (elements[0].contains("agent")) {
                readAgent(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasGeneratedBy")) {
                readGeneration(attributes, optionalAttributes);
            }
            if (elements[0].contains("used")) {
                readUsage(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasInformedBy")) {
                readCommunication(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasStartedBy")) {
                readStart(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasEndedBy")) {
                readEnd(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasInvalidatedBy")) {
                readInvalidation(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasDerivedFrom")) { //Revision, Quotation, Primary Source from PROV-N
                readDerivation(attributes, optionalAttributes);
            }
//            if(elements[0].contains("wasDerivedFrom")){ //Revision from PROV-N
//                readRevision(attributes, optionalAttributes);
//            }
//            if(elements[0].contains("wasDerivedFrom")){ //Quotation from PROV-N
//                readQuotation(attributes, optionalAttributes);
//            }
//            if(elements[0].contains("wasDerivedFrom")){ //Primary Source from PROV-N
//                readPrimarySource(attributes, optionalAttributes);
//            }
            if (elements[0].contains("wasAttributedTo")) {
                readAttribution(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasAssociatedWith")) {
                readAssociation(attributes, optionalAttributes);
            }
            if (elements[0].contains("actedOnBehalfOf")) {
                readDelegation(attributes, optionalAttributes);
            }
            if (elements[0].contains("wasInfluencedBy")) {
                readInfluence(attributes, optionalAttributes);
            }
        }
    }

    public void readEntity(String[] attributes, String[] optionalAttributes) {
        Vertex node;
        String id = attributes[0];
        node = new EntityVertex(id, id, "");
        readAttributes(node, optionalAttributes);
        addNode(node);
    }

    public void readActivity(String[] attributes, String[] optionalAttributes) {
        Vertex node;
        String id = attributes[0];
        node = new ActivityVertex(id, id, "");
        if (attributes.length > 1) {
            String startTime = attributes[1];

            GraphAttribute optAtt = new GraphAttribute("startTime", startTime);
            node.addAttribute(optAtt);
            GraphAttribute time = new GraphAttribute("Timestamp", startTime);
            node.addAttribute(time);
//            node.setTime(startTime);
        }
        if (attributes.length > 2) {
            String endTime = attributes[2];
            GraphAttribute optAtt = new GraphAttribute("endTime", endTime);
            node.addAttribute(optAtt);
        }
        readAttributes(node, optionalAttributes);
        addNode(node);
    }

    public void readAgent(String[] attributes, String[] optionalAttributes) {
        Vertex node;
        String id = attributes[0];
        node = new AgentVertex(id, id, "");
        readAttributes(node, optionalAttributes);
        addNode(node);
    }

    public void readGeneration(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String entity = "-";
        String activity = "-";
        String time;

        if (attributes.length == 3) {
            id = getEdgeID(attributes[0], id);
            entity = getEdge1stAttribute(attributes[0], entity);
            activity = attributes[1];
            time = attributes[2];
        } else {
            id = getEdgeID(attributes[0], id);
            entity = getEdge1stAttribute(attributes[0], entity);
            time = "";
        }
        activity = testPointer(activity, "Activity");
        entity = testPointer(entity, "Entity");

        edge = new Edge(id, "wasGeneratedBy", "-", "-", nodes.get(activity), nodes.get(entity));
        GraphAttribute optAtt = new GraphAttribute("time", time);
        edge.addAttribute(optAtt);

        readAttributes(edge, optionalAttributes);
        addEdge(edge);

    }

    public void readUsage(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String activity = "-";
        String entity = "-";
        String time;

        if (attributes.length == 3) {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            entity = attributes[1];
            time = attributes[2];
        } else {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            time = "-";
        }

        activity = testPointer(activity, "Activity");
        entity = testPointer(entity, "Entity");

        edge = new Edge(id, "used", "-", "-", nodes.get(entity), nodes.get(activity));
        GraphAttribute optAtt = new GraphAttribute("time", time);
        edge.addAttribute(optAtt);

        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }

    public void readCommunication(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String informed = "-";
        String informant = "-";

        id = getEdgeID(attributes[0], id);
        informed = getEdge1stAttribute(attributes[0], informed);
        informant = attributes[1];

        informed = testPointer(informed, "Activity");
        informant = testPointer(informant, "Activity");

        edge = new Edge(id, "wasInformedBy", "-", "-", nodes.get(informant), nodes.get(informed));

        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }

    public void readStart(String[] attributes, String[] optionalAttributes) {
        starterOrEnder(attributes, optionalAttributes, "wasStartedBy");
    }

    public void readEnd(String[] attributes, String[] optionalAttributes) {
        starterOrEnder(attributes, optionalAttributes, "wasEndedBy");
    }

    public void starterOrEnder(String[] attributes, String[] optionalAttributes, String type) {
        Edge edge;
        String id = null;
        String activity = "-";
        String trigger = "-";
        String starterOrEnder = "-";
        String time;

        if (attributes.length == 4) {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            trigger = attributes[1];
            starterOrEnder = attributes[2];
            time = attributes[3];
        } else {
            id = getEdgeID(attributes[0], id);
            activity = getEdge1stAttribute(attributes[0], activity);
            time = "";
        }

        starterOrEnder = testPointer(starterOrEnder, "Activity");
        activity = testPointer(activity, "Activity");

        edge = new Edge(id, type, "-", "-", nodes.get(starterOrEnder), nodes.get(activity));

        GraphAttribute optAtt = new GraphAttribute("trigger", trigger);
        edge.addAttribute(optAtt);

        optAtt = new GraphAttribute(type, starterOrEnder);
        edge.addAttribute(optAtt);

        optAtt = new GraphAttribute("time", time);
        edge.addAttribute(optAtt);

        readAttributes(edge, optionalAttributes);
        addEdge(edge);
//        }
//        else {
//            edge = new Edge(id + "_trigger", type, "-", "-", nodes.get(trigger), nodes.get(activity));
//            GraphAttribute optAtt = new GraphAttribute("time", time);
//            edge.addAttribute(optAtt);
//
//            readAttributes(edge, optionalAttributes);
//            addEdge(edge);
//            
//            edge = new Edge(id + "_generated", type, "-", "-", nodes.get(starterOrEnder), nodes.get(trigger));
//            optAtt = new GraphAttribute("time", time);
//            edge.addAttribute(optAtt);
//
//            readAttributes(edge, optionalAttributes);
//            addEdge(edge);
//        }
    }

    public void readInvalidation(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String entity = "-";
        String activity = "-";
        String time;

        id = getEdgeID(attributes[0], id);
        entity = getEdge1stAttribute(attributes[0], entity);
        activity = attributes[1];
        time = attributes[2];

        entity = testPointer(entity, "Entity");
        activity = testPointer(activity, "Activity");

        edge = new Edge(id, "wasInvalidatedBy", "-", "-", nodes.get(activity), nodes.get(entity));
        GraphAttribute optAtt = new GraphAttribute("time", time);
        edge.addAttribute(optAtt);

        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }

    public void readDerivation(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String generatedEntity = null;
        String usedEntity;
        String activity = "-";
        String generation = "-";
        String usage = "-";
        if (attributes.length == 5) {
            id = getEdgeID(attributes[0], id);
            generatedEntity = getEdge1stAttribute(attributes[0], generatedEntity);
            usedEntity = attributes[1];
            activity = attributes[2];
            generation = attributes[3];
            usage = attributes[4];
        } else {
            id = getEdgeID(attributes[0], id);
            generatedEntity = getEdge1stAttribute(attributes[0], generatedEntity);
            usedEntity = attributes[1];
        }

        usedEntity = testPointer(usedEntity, "Entity");
        generatedEntity = testPointer(generatedEntity, "Entity");

        edge = new Edge(id, "wasDerivedFrom", "-", "-", nodes.get(usedEntity), nodes.get(generatedEntity));

        GraphAttribute optAtt = new GraphAttribute("activity", activity);
        edge.addAttribute(optAtt);

        optAtt = new GraphAttribute("generation", generation);
        edge.addAttribute(optAtt);

        optAtt = new GraphAttribute("usage", usage);
        edge.addAttribute(optAtt);

        readAttributes(edge, optionalAttributes);
        addEdge(edge);

//        if (generation != null && !generation.matches("-") && activity != null && !activity.matches("-")) {
//            edge = new Edge(generation, "wasGeneratedBy", "-", "-", nodes.get(activity), nodes.get(generatedEntity));
//            readAttributes(edge, optionalAttributes);
//            addEdge(edge);
//
//            edge = new Edge(usage, "used", "-", "-", nodes.get(usedEntity), nodes.get(activity));
//            readAttributes(edge, optionalAttributes);
//            addEdge(edge);
//        }
    }

    public void readRevision(String[] attributes, String[] optionalAttributes) {
        readGeneration(attributes, optionalAttributes);
    }

    public void readQuotation(String[] attributes, String[] optionalAttributes) {
        readGeneration(attributes, optionalAttributes);
    }

    public void readPrimarySource(String[] attributes, String[] optionalAttributes) {
        readGeneration(attributes, optionalAttributes);
    }

    public void readAttribution(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String entity = "-";
        String agent;

        id = getEdgeID(attributes[0], id);
        entity = getEdge1stAttribute(attributes[0], entity);
        agent = attributes[1];

        entity = testPointer(entity, "Entity");
        agent = testPointer(agent, "Agent");

        edge = new Edge(id, "wasAttributedTo", "-", "-", nodes.get(agent), nodes.get(entity));

        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }

    public void readAssociation(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String activity = "-";
        String agent;
        String plan = "-";
        boolean hasPlan = false;

        id = getEdgeID(attributes[0], id);
        activity = getEdge1stAttribute(attributes[0], activity);
        agent = attributes[1];

        if (attributes.length == 3) {
            plan = attributes[2];
            if (!plan.contentEquals("-")) {
                hasPlan = true;
            }
        }

        activity = testPointer(activity, "Activity");
        agent = testPointer(agent, "Agent");
        plan = testPointer(plan, "Entity");

        edge = new Edge(id, "wasAssociatedWith", "-", "-", nodes.get(agent), nodes.get(activity));

        GraphAttribute optAtt = new GraphAttribute("plan", plan);
        edge.addAttribute(optAtt);

        readAttributes(edge, optionalAttributes);
        addEdge(edge);

        if (hasPlan) {
//            Vertex node = new EntityVertex(plan, plan, "");
//            GraphAttribute optAtt = new GraphAttribute("prov:type", "prov:Plan");
//            addNode(node);
            edge = new Edge(id, "wasAssociatedWith(Plan)", "-", "-", nodes.get(plan), nodes.get(agent));
            readAttributes(edge, optionalAttributes);
            addEdge(edge);
        }
    }

    public void readDelegation(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String delegate = "-";
        String responsible;
        String activity = "-";

        id = getEdgeID(attributes[0], id);
        delegate = getEdge1stAttribute(attributes[0], delegate);
        responsible = attributes[1];

        if (attributes.length == 3) {
            activity = attributes[2];
        }

        delegate = testPointer(delegate, "Agent");
        responsible = testPointer(responsible, "Agent");

        edge = new Edge(id, "actedOnBehalfOf", "-", "-", nodes.get(delegate), nodes.get(responsible));

        GraphAttribute optAtt = new GraphAttribute("activity", activity);
        edge.addAttribute(optAtt);

        readAttributes(edge, optionalAttributes);
        addEdge(edge);

        if (activity != null && !activity.matches("-")) {
            edge = new Edge(id, "actedOnBehalfOf(Activity)", "-", "-", nodes.get(activity), nodes.get(responsible));
            readAttributes(edge, optionalAttributes);
            addEdge(edge);
        }
    }

    public void readInfluence(String[] attributes, String[] optionalAttributes) {
        Edge edge;
        String id = null;
        String influencee = "-";
        String influencer;

        id = getEdgeID(attributes[0], id);
        influencee = getEdge1stAttribute(attributes[0], influencee);
        influencer = attributes[1];

        influencee = testPointer(influencee, "Entity");
        influencer = testPointer(influencer, "Entity");

        edge = new Edge(id, "wasInfluencedBy", "-", "-", nodes.get(influencer), nodes.get(influencee));

        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }

    public void readAlternate(String[] attributes, String[] optionalAttributes) {
        alternateOrSpecializationOrMembership(attributes, optionalAttributes, "alternateOf");
    }

    public void readSpecialization(String[] attributes, String[] optionalAttributes) {
        alternateOrSpecializationOrMembership(attributes, optionalAttributes, "specializationOf");
    }

    public void readMembership(String[] attributes, String[] optionalAttributes) {
        alternateOrSpecializationOrMembership(attributes, optionalAttributes, "hadMember");
    }

    public void alternateOrSpecializationOrMembership(String[] attributes, String[] optionalAttributes, String type) {
        Edge edge;
        String id = null;
        String alternate1 = "-";
        String alternate2;

        id = "Edge_" + edgeOptionalID;
        edgeOptionalID++;
        alternate2 = attributes[1];

        alternate1 = testPointer(alternate1, "Entity");
        alternate2 = testPointer(alternate2, "Entity");

        edge = new Edge(id, type, "-", "-", nodes.get(alternate2), nodes.get(alternate1));

        readAttributes(edge, optionalAttributes);
        addEdge(edge);
    }

    public void readAttributes(GraphObject obj, String[] attributes) {
        if (attributes != null) {
            for (String attList1 : attributes) {
                String[] att = attList1.split("=");
                if (att.length > 1) {
                    if (att[0].equalsIgnoreCase("prov:label")) {
                        obj.setLabel(att[1]);
                    } else if ((obj instanceof Edge) && (att[0].contains("value"))) {
                        ((Edge) obj).setValue(att[1]);
                    }
                    GraphAttribute optAtt = new GraphAttribute(att[0], att[1]);
                    obj.addAttribute(optAtt);
                }
            }
        }
    }

    public String getEdgeID(String attribute, String id) {
        String[] att = attribute.split(";");
        if (att.length == 2) {
            if (att[0].equalsIgnoreCase("-")) {
                id = "Edge_" + edgeOptionalID;
                edgeOptionalID++;
            } else {
                id = att[0];
            }
        } else {
            id = "Edge_" + edgeOptionalID;
            edgeOptionalID++;
        }
        return id;
    }

    public String getEdge1stAttribute(String attribute, String attr) {
        String[] att = attribute.split(";");
        if (att.length == 2) {
            attr = att[1];
        } else {
            attr = att[0];
        }
        return attr;
    }

    /**
     * Function to verify if the pointers were initialized or informed
     *
     * @param pointer: Is the edge's source or target
     * @param type: Is the type of the vertex we are trying to verify. This is
     * used to create the correct vertex type when the pointer was not
     * initialized
     * @return the pointer
     */
    public String testPointer(String pointer, String type) {
        if (nodes.get(pointer) == null) {
            if (pointer.contentEquals("-")) {
                if(type.contentEquals("Entity"))
                    pointer = "Unknown Entity";
                else if(type.contentEquals("Agent"))
                    pointer = "Unknown Agent";
                else
                    pointer = "Unknown Activity";
            } else if (pointer == null) {
                pointer = "Unknown";
            } else if (pointer.isEmpty()) {
                pointer = "Unknown";
            } else if (!pointer.isEmpty()) {
                if (type.equals("Agent")) {
                    Vertex node = new AgentVertex(pointer, "Not Initialized", "");
                    addNode(node);
                } else if (type.equals("Activity")) {
                    Vertex node = new ActivityVertex(pointer, "Not Initialized", "");
                    addNode(node);
                } else {
                    Vertex node = new EntityVertex(pointer, "Not Initialized", "");
                    addNode(node);
                }
            } else {
                pointer = "Unknown";
            }
        }
        return pointer;
    }
}
