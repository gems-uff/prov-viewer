/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphMatching {

    private int edgeID = 0;
    private int vertexID = 0;
    private double threshold;
    Map<String, Edge> duplicateEdges;
    private final Map<String, Vertex> vertexList;
    private final Map<String, Edge> edgeList;
    private final Map<String, Vertex> combinedVertexList;
    private final Map<String, AttributeErrorMargin> attributeList;  
    private final Map<String, String> vocabulary; 
    // AttributeErrorMargin.name = the atribute 
    // AttributeErrorMargin.value = error margin
    // AttributeErrorMargin.value for timeDate in milliseconds for the error margin
    // AttributeErrorMargin.value for Strings is a Vocabularity (accepted strings)

    /**
     * Constructor
     *
     * @param restrictionList is the list of attributes, and their error margin,
     * that is used to compareAttributes vertices
     * @param vocabulary is the vocabulary of synonymous words
     * @param similarityThreshold is the percentage used to define when two
     * vertices are considered similar. Varies from 0 to 1.0
     */
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, Map<String, String> vocabulary, double similarityThreshold) {
        vertexList = new HashMap<String, Vertex>();
        edgeList = new HashMap<String, Edge>();
        attributeList = restrictionList;
        this.vocabulary = vocabulary;
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0, 1.0, similarityThreshold);
        combinedVertexList = new HashMap<String, Vertex>();
        duplicateEdges = new HashMap<String, Edge>();
    }
    
    /**
     * Constructor
     *
     * @param restrictionList is the list of attributes, and their error margin,
     * that is used to compareAttributes vertices
     * @param similarityThreshold is the percentage used to define when two
     * vertices are considered similar. Varies from 0 to 1.0
     */
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, double similarityThreshold) {
        vertexList = new HashMap<String, Vertex>();
        edgeList = new HashMap<String, Edge>();
        attributeList = restrictionList;
        this.vocabulary = new HashMap<String, String>();
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0, 1.0, similarityThreshold);
        combinedVertexList = new HashMap<String, Vertex>();
        duplicateEdges = new HashMap<String, Edge>();
    }

    /**
     * Constructor without a list of attributes with their error margins
     *
     * @param similarityThreshold is the percentage used to define when two
     * vertices are considered similar. Varies from 0 to 1.0
     */
    public GraphMatching(double similarityThreshold) {
        vertexList = new HashMap<String, Vertex>();
        edgeList = new HashMap<String, Edge>();
        attributeList = new HashMap<String, AttributeErrorMargin>();
        vocabulary = new HashMap<String, String>(); 
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0, 1.0, similarityThreshold);
        combinedVertexList = new HashMap<String, Vertex>();
        duplicateEdges = new HashMap<String, Edge>();
    }

    /**
     * Return the vertices from the combined graph
     *
     * @return
     */
    public Collection<Vertex> getVertexList() {
        return vertexList.values();
    }

    /**
     * Return the edges from the combined graph
     *
     * @return
     */
    public Collection<Edge> getEdgeList() {
        return edgeList.values();
    }

    /**
     * Function that determines if two vertices are equivalent
     *
     * @param v1 is the first vertex
     * @param v2 is the second vertex
     * @return TRUE if equivalent and FALSE if not
     */
    public boolean isSimilar(Vertex v1, Vertex v2) {
        boolean isSimilar = false;
        // Code here
        // Use the attributeList to determine if vertices are similar
        double similarity = 0.0F;

        // Compare vertex types: If different types than it is not similar
        if (!v1.getNodeType().equalsIgnoreCase(v2.getNodeType())) {
            return false;
        }

        Map<String, GraphAttribute> attributes = new HashMap<String, GraphAttribute>();

        // Check all v1 attributes
        for (GraphAttribute attribute : v1.getAttributes()) {
            similarity = compareAttributes(attributes, attribute, v2, similarity);
        }

        // Now check all v2 attributes
        for (GraphAttribute attribute : v2.getAttributes()) {
            // Do not check the same attributes already verified when checking v1
            if (!attributes.containsKey(attribute.getName())) {
                similarity = compareAttributes(attributes, attribute, v1, similarity);
            }
        }

        // Compute the number of attributes, considering their weights, for the similarity check
        float size = 0;
        for (GraphAttribute att : attributes.values()) {
            float weight = 1;
            if (attributeList.get(att.getName()) != null) {
                weight = attributeList.get(att.getName()).getWeight();
            }
            size = size + (1 * weight);
        }
        
        
        similarity = similarity / size;

//        System.out.println("Match Similarity between " + v1.getID() + " and " + v2.getID() + ": " + similarity);
        if (similarity >= threshold) {
            isSimilar = true;
        }

        return isSimilar;
    }

    /**
     * Function to compare all attributes from 2 verties
     *
     * @param attributes is the list of processed attributes
     * @param attribute is the current attribute from v1
     * @param v2 is the second vertex
     * @param similarity is the similarity variable used to discern if vertices
     * are similar
     * @return
     */
    public double compareAttributes(Map<String, GraphAttribute> attributes, GraphAttribute attribute, Vertex v2, double similarity) {
        attributes.put(attribute.getName(), attribute);
        if (v2.getAttribute(attribute.getName()) != null) {
            String av1 = attribute.getAverageValue();
            String av2 = v2.getAttribute(attribute.getName()).getAverageValue();
            String errorMargin = "0";
            float weight = 1;
            if (attributeList.get(attribute.getName()) != null) {
                errorMargin = attributeList.get(attribute.getName()).getValue();
                weight = attributeList.get(attribute.getName()).getWeight();
            }
            
            // Dealing with numeric values
            if (Utils.tryParseFloat(av1) && Utils.tryParseFloat(av2) && Utils.tryParseFloat(errorMargin)) {
                if (Utils.FloatEqualTo(Utils.convertFloat(av1), Utils.convertFloat(av2), Utils.convertFloat(errorMargin))) {
                    similarity = similarity + (1 * weight);
                }
            } // Dealing with a timeDate values
            else if(Utils.tryParseDate(av1) && Utils.tryParseDate(av2)) {
                if(Utils.FloatEqualTo(Utils.convertStringDateToDouble(av1),Utils.convertStringDateToDouble(av2),Utils.convertDouble(errorMargin))) {
                    similarity = similarity + (1 * weight);
                }
            }
            // Dealing with string values: Checking if they are equals
            else if (av1.equalsIgnoreCase(av2)) {
                similarity = similarity + (1 * weight);
            } // Dealing with String values: Checking if they are in the Vocabulary and thus synonymous
            else if(vocabulary.containsKey(av1.toLowerCase()))
                if(vocabulary.get(av1.toLowerCase()).contains(av2.toLowerCase() + " ")){
                    similarity = similarity + (1 * weight);
            } 
        }

        return similarity;
    }

    /**
     * Function to combine two vertices
     *
     * @param v1 is the first vertex
     * @param v2 is the second vertex
     * @return the combined vertex
     */
    public Vertex combineVertices(Vertex v1, Vertex v2) {
        Vertex combinedVertex = null;

        if (v1 instanceof ActivityVertex) {
            combinedVertex = new ActivityVertex(v1.getID(), v1.getLabel(), v1.getTimeString());
        } else if (v1 instanceof EntityVertex) {
            combinedVertex = new EntityVertex(v1.getID(), v1.getLabel(), v1.getTimeString());
        } else {
            combinedVertex = new AgentVertex(v1.getID(), v1.getLabel(), v1.getTimeString());
        }

        // Add all attributes from v1
        combinedVertex.addAllAttributes(v1.attributes);

        // Now add/update all attributes from v1 to combinedVertex
        for (GraphAttribute att : v2.getAttributes()) {
            if (combinedVertex.attributes.containsKey(att.getName())) {
                GraphAttribute temporary = combinedVertex.attributes.get(att.getName());
                temporary.updateAttribute(att.getAverageValue());
                combinedVertex.attributes.put(att.getName(), temporary);
            } else {
                combinedVertex.attributes.put(att.getName(), new GraphAttribute(att.getName(), att.getAverageValue()));
            }
        }

        // Update ID and Label
        combinedVertex.setID(combinedVertex.getID() + "_" + v2.getID());
        if(!combinedVertex.getLabel().equalsIgnoreCase(v2.getLabel()))
            combinedVertex.setLabel(combinedVertex.getLabel() + "_" + v2.getLabel());        

        // TODO: Update time
//        combinedVertex.setTime(null);
        combinedVertexList.put(v1.getID(), combinedVertex);
        combinedVertexList.put(v2.getID(), combinedVertex);

        return combinedVertex;
    }

    /**
     * Method to add a vertex in the combined graph
     *
     * @param vertex is the vertex to be added
     */
    public void addVertex(Vertex vertex) {
        if (!vertexList.containsKey(vertex.getID())) {
            vertexList.put(vertex.getID(), vertex);
        } else {
            vertex.setID(vertex.getID() + "_" + vertexID++);
            vertexList.put(vertex.getID(), vertex);
        }
    }

    /**
     * Method to add vertices from a graph to the combined graph
     *
     * @param vertices is the graph that contains the vertices to be added
     */
    public void addVertices(Collection<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            // Add only the vertices that were not used to generate a combined vertex
            if (!combinedVertexList.containsKey(((Vertex) vertex).getID())) {
                addVertex((Vertex) vertex);
            }
        }
    }

    /**
     * Function to update edges that has a vertex as source or target which was
     * combined
     *
     * @param edges is the list of edges to be updated
     * @return a new list of updated edges
     */
    public Collection<Edge> updateEdges(Collection<Edge> edges) {
        Collection<Edge> newEdges = new ArrayList<Edge>();

        for (Edge edge : edges) {
            Edge updatedEdge = edge;
            boolean source = false;
            boolean target = false;
            if (combinedVertexList.containsKey(((Vertex)edge.getSource()).getID())) {
                updatedEdge.setSource(combinedVertexList.get(((Vertex)edge.getSource()).getID()));
                source = true;
            }
            if (combinedVertexList.containsKey(((Vertex)edge.getTarget()).getID())) {
                updatedEdge.setTarget(combinedVertexList.get(((Vertex)edge.getTarget()).getID()));
                target = true;
            }
            // Add the edge
            newEdges.add(updatedEdge);

            // If both the source and target of an edge were modified, than it will generate duplicates when updating both graphs
            // because both extremes were updated
//            if (source && target) {
//                duplicateEdges.put(updatedEdge.getType() + updatedEdge.getSource().getID() + updatedEdge.getTarget().getID(), updatedEdge);
//            }
        }

        return newEdges;
    }

    /**
     * Method to add an edge in the combined graph
     *
     * @param edge is the edge to be added
     */
    public void addEdge(Edge edge) {
        if (!edgeList.containsKey(edge.getID())) {
            edgeList.put(edge.getID(), edge);
        } else { // Conflict ID, need to change the edge ID
            edge.setID(edge.getID() + "_" + edgeID++);
            edgeList.put(edge.getID(), edge);
        }
    }

    /**
     * Method to add edges from a Graph in the combined graph
     *
     * @param edges
     */
    public void addEdges(Collection<Edge> edges) {
        for (Edge edge : edges) {
            addEdge(edge);
        }
    }

    /**
     * Method to remove duplicate or similar edges from the combined graph
     */
    public void removeDuplicateEdges() {
        // Code to combine similar edges from edgeList
        Collection<Edge> values = new ArrayList<Edge>();
        values.addAll(edgeList.values());
        duplicateEdges = new HashMap<String, Edge>();
        
        for (Edge e1 : values) {
            for (Edge e2 : values) {
                if (!(duplicateEdges.containsKey(e1.getID()) && duplicateEdges.containsKey(e2.getID()))) {
                    if(!e1.getID().equalsIgnoreCase(e2.getID())) {
                        if(e1.getType().equalsIgnoreCase(e2.getType())) {
                            if(((Vertex)e1.getSource()).getID().equalsIgnoreCase(((Vertex)e2.getSource()).getID())) {
                                if(((Vertex)e1.getTarget()).getID().equalsIgnoreCase(((Vertex)e2.getTarget()).getID())) {
                                    edgeList.remove(e2.getID());
                                    // TO DO: Merge E1 with E2
                                    
                                    duplicateEdges.put(e1.getID(), e1);
                                    duplicateEdges.put(e2.getID(), e2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Method to return the combined graph's edge collection
     *
     * @return edges from the combined graph
     */
    public Collection<Edge> getEdges() {
        return edgeList.values();
    }

    /**
     * Method to return the combined graph
     *
     * @return the combined graph
     */
    public DirectedGraph<Vertex, Edge> getCombinedGraph() {
        DirectedGraph<Vertex, Edge> combinedGraph = new DirectedSparseMultigraph<Vertex, Edge>();

        removeDuplicateEdges();

        for (Edge edge : this.getEdges()) {
            combinedGraph.addEdge(edge, (Vertex) edge.getSource(), (Vertex) edge.getTarget());
        }

        return combinedGraph;
    }
}
