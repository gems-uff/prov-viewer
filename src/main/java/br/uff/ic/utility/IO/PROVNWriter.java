/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.IO;

import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public final class PROVNWriter {

    public Collection<Object> vertexList;
    public Collection<Edge> edgeList;

    private BufferedWriter buffer;
    public static final String MARKER = "-";
    public boolean standaloneExpression = false;

    public PROVNWriter(Collection<Object> vertices, Collection<Edge> edges) {
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

    public void saveToProvn(String fileName) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter(fileName + ".provn");
        buffer = new BufferedWriter(fw);

        startDocument("");
        writeVertices();
        writeEdges();
        close();
    }

    public void writeVertices() throws IOException {
        for (Object vertex : vertexList) {
            if (vertex instanceof AgentVertex) {
                newAgent((Vertex) vertex);
            } else if (vertex instanceof ActivityVertex) {
                newActivity((Vertex) vertex);
            } else {
                newEntity((Vertex) vertex);
            }

        }
    }

    public void writeEdges() throws IOException {
        for (Edge edge : edgeList) {
            if(edge.getType().equalsIgnoreCase("wasGeneratedBy")){
                writeGeneration(edge);
            } 
            if(edge.getType().equalsIgnoreCase("used")){
                writeUsage(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasInformedBy")){
                writeCommunication(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasStartedBy")){
                writeStart(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasEndedBy")){
                writeEnd(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasInvalidatedBy")){
                writeInvalidation(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasDerivedFrom")){ //Revision, Quotation, Primary Source from PROV-N
                writeDerivation(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasAttributedTo")){
                writeAttribution(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasAssociatedWith")){
                writeAssociation(edge);
            }
            if(edge.getType().equalsIgnoreCase("actedOnBehalfOf")){
                writeDelegation(edge);
            }
            if(edge.getType().equalsIgnoreCase("wasInfluencedBy")){
                writeInfluence(edge);
            }

        }
    }

    public void newActivity(Vertex vertex) throws IOException {
        String s = keyword("activity") + "(" + vertex.getID() + ","
                + attOrMarker(vertex.getAttribute("startTime")) + "," + attOrMarker(vertex.getAttribute("endTime"))
                + optionalAttributes(vertex.getAttributes()) + ")";
        writeln(s);
    }

    public void newAgent(Vertex vertex) throws IOException {
        String s = keyword("agent") + "(" + vertex.getID()
                + optionalAttributes(vertex.getAttributes()) + ")";
        writeln(s);
    }

    public void newEntity(Vertex vertex) throws IOException {
        String s = keyword("entity") + "(" + vertex.getID()
                + optionalAttributes(vertex.getAttributes()) + ")";
        writeln(s);

    }

    public void writeDelegation(Edge edge) throws IOException {
        String s = keyword("actedOnBehalfOf") + "(" + optionalId(edge.getID())
                + getID(edge.getSource()) + "," + getID(edge.getTarget()) + "," + attOrMarker(edge.getAttribute("activity"))
                + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void newAlternateOf(Edge edge) throws IOException {
        writeln("alternateOf(" + getID(edge.getSource()) + "," + getID(edge.getTarget()) + ")");
    }

    public void newHadMember(Edge edge) throws IOException {

        String s = keyword("hadMember") + "(" + getID(edge.getSource()) + ","
                + getID(edge.getTarget()) + ")";
        writeln(s);
    }

    public void newSpecializationOf(Edge edge) throws IOException {
        writeln("specializationOf(" + getID(edge.getSource()) + "," + getID(edge.getTarget()) + ")");
    }

    public void writeUsage(Edge edge) throws IOException {
        String s = keyword("used") + "(" + optionalId(edge.getID())
                + getID(edge.getSource()) + "," + getID(edge.getTarget()) + ","
                + attOrMarker(edge.getAttribute("time")) + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeAssociation(Edge edge) throws IOException {
        String s = keyword("wasAssociatedWith") + "(" + optionalId(edge.getID())
                + getID(edge.getSource()) + "," + getID(edge.getTarget()) + "," + attOrMarker(edge.getAttribute("plan"))
                + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeAttribution(Edge edge) throws IOException {
        String s = keyword("wasAttributedTo") + "(" + optionalId(edge.getID())
                + getID(edge.getSource()) + "," + getID(edge.getTarget())
                + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeDerivation(Edge edge) throws IOException {
        String s = keyword("wasDerivedFrom")
                + "(" + optionalId(edge.getID()) + getID(edge.getSource()) + "," + getID(edge.getTarget())
                + ((edge.getAttribute("activity") == null && edge.getAttribute("generation") == null && edge.getAttribute("usage") == null) ? ""
                        : ", " + attOrMarker(edge.getAttribute("activity")) + ", "
                        + attOrMarker(edge.getAttribute("generation")) + ", "
                        + attOrMarker(edge.getAttribute("usage")))
                + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeEnd(Edge edge) throws IOException {
        String s = "wasEndedBy(" + optionalId(edge.getID()) + getID(edge.getSource()) + ","
                + attOrMarker(edge.getAttribute("trigger")) + "," + getID(edge.getTarget()) + ","
                + attOrMarker(edge.getAttribute("time")) + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeStart(Edge edge) throws IOException {
        String s = "wasStartedBy(" + optionalId(edge.getID()) + getID(edge.getSource())
                + "," + attOrMarker(edge.getAttribute("trigger")) + "," + getID(edge.getTarget()) + ","
                + attOrMarker(edge.getAttribute("time")) + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeGeneration(Edge edge) throws IOException {
        String s = keyword("wasGeneratedBy") + "(" + optionalId(edge.getID())
                + getID(edge.getSource()) + "," + getID(edge.getTarget()) + ","
                + attOrMarker(edge.getAttribute("time")) + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeInfluence(Edge edge) throws IOException {
        String s = "wasInfluencedBy(" + optionalId(edge.getID()) + getID(edge.getSource()) + ","
                + getID(edge.getTarget()) + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeCommunication(Edge edge) throws IOException {
        String s = "wasInformedBy(" + optionalId(edge.getID()) + getID(edge.getSource()) + ","
                + getID(edge.getTarget()) + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public void writeInvalidation(Edge edge) throws IOException {
        String s = keyword("wasInvalidatedBy") + "(" + optionalId(edge.getID())
                + getID(edge.getSource()) + "," + getID(edge.getTarget()) + ","
                + attOrMarker(edge.getAttribute("time")) + optionalAttributes(edge.getAttributes()) + ")";
        writeln(s);
    }

    public String attOrMarker(GraphAttribute att) {
        return ((att == null) ? MARKER : att.getAverageValue());
    }

    public String keyword(String s) {
        return s;
    }

    public String breakline() {
        return "\n";
    }

    public void startDocument(String namespaces) throws IOException {
        String s = keyword("document") + breakline();
//        s = s + processNamespaces(namespaces);
        write(s);
    }

    public void close() throws IOException {
        String s = keyword("endDocument") + breakline();
        write(s);
        buffer.close();
    }

    public String symbol(String s) {
        return s;
    }

    public String optionalAttributes(Collection<GraphAttribute> attrs) {
        if ((attrs == null) || (attrs.isEmpty())) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (GraphAttribute attr : attrs) {
            if (attr.getName().equalsIgnoreCase("activity"))
                ; else if (attr.getName().equalsIgnoreCase("time"))
                ; else if (attr.getName().equalsIgnoreCase("plan"))
                ; else if (attr.getName().equalsIgnoreCase("generation"))
                ; else if (attr.getName().equalsIgnoreCase("usage"))
                ; else if (attr.getName().equalsIgnoreCase("trigger"))
                ; else if (first) {
                sb.append(symbol(",[") + attr.toNotationString());
                first = false;
            } else {
                sb.append(symbol(",") + " " + attr.toNotationString());
            }
        }
        if (!first) {
            sb.append(symbol("]"));
        }
        return sb.toString();
    }

    public void write(String s) throws IOException {
        buffer.write(s);
    }

    public void writeln(String s) throws IOException {
        buffer.write(s);
        if (!standaloneExpression) {
            buffer.newLine();
        }
    }

    public String optionalId(String id) {
        return id + "; ";
    }

    public String getID(Object id) {
        return ((Vertex) id).getID();
    }
}
