/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.utility.Attribute;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphMatching {

    private double threshold;
    private Collection<Object> vertexList;
    private Collection<Edge> edgeList;
    private Map<String, Vertex> combinedVertexList;
    private Map<String, Attribute> attributeList;    // Attribute.name = the atribute 
    // Attribute.value = error margin

    /**
     * Constructor
     *
     * @param restrictionList is the list of attributes, and their error margin,
 that is used to compareAttributes vertices
     * @param similarityThreshold is the percentage used to define when two vertices are considered similar. Varies from 0 to 1.0
     */
    public GraphMatching(Map<String, Attribute> restrictionList, double similarityThreshold) {
        vertexList = new ArrayList<Object>();
        edgeList = new ArrayList<Edge>();
        attributeList = restrictionList;
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0, 1.0, similarityThreshold);
        combinedVertexList = new HashMap<String, Vertex>();
    }
    
    /**
     * Constructor without a list of attributes with their error margins
     * @param similarityThreshold is the percentage used to define when two vertices are considered similar. Varies from 0 to 1.0
     */
    public GraphMatching(double similarityThreshold) {
        vertexList = new ArrayList<Object>();
        edgeList = new ArrayList<Edge>();
        attributeList = new HashMap<String, Attribute>();
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0, 1.0, similarityThreshold);
        combinedVertexList = new HashMap<String, Vertex>();
    }

    /**
     * Return the vertices from the combined graph
     *
     * @return
     */
    public Collection<Object> getVertexList() {
        return vertexList;
    }

    /**
     * Return the edges from the combined graph
     *
     * @return
     */
    public Collection<Edge> getEdgeList() {
        return edgeList;
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
        
        Map<String, Attribute> attributes = new HashMap<String, Attribute>();
        
        // Check all v1 attributes
        for (Attribute attribute : v1.getAttributes()) {
            similarity = compareAttributes(attributes, attribute, v2, similarity);
        }
        
        // Now check all v2 attributes
        for (Attribute attribute : v2.getAttributes()) {
            // Do not check the same attributes already verified when checking v1
            if(!attributes.containsKey(attribute.getName())) {
                similarity = compareAttributes(attributes, attribute,v1, similarity);
            }
        }
        System.out.println("Similarity " + similarity);
        System.out.println("Size " + attributes.size());
        similarity = similarity / attributes.size();
        
        System.out.println("Match Similarity between " + v1.getID() + " and " + v2.getID() + ": " + similarity);
        if (similarity >= threshold) {
            isSimilar = true;
        }

        return isSimilar;
    }

    /**
     * Function to compare all attributes from 2 verties
     * @param attributes is the list of processed attributes
     * @param attribute is the current attribute from v1
     * @param v2 is the second vertex
     * @param similarity is the similarity variable used to discern if vertices are similar
     * @return 
     */
    public double compareAttributes(Map<String, Attribute> attributes, Attribute attribute, Vertex v2, double similarity) {
        attributes.put(attribute.getName(), attribute);
        if(v2.getAttribute(attribute.getName()) != null)
        {
            String av1 = attribute.getValue();
            String av2 = v2.getAttribute(attribute.getName()).getValue();
            String errorMargin = "0";
            if(attributeList.get(attribute.getName()) != null)
                errorMargin = attributeList.get(attribute.getName()).getValue();

            if (Utils.tryParseFloat(av1) && Utils.tryParseFloat(av2) && Utils.tryParseFloat(errorMargin)) {
                if (Utils.FloatEqualTo(Utils.convertFloat(av1), Utils.convertFloat(av2), Utils.convertFloat(errorMargin))) {
                    similarity ++;
                }
            } // Dealing with string values: Only accepting complete string match
            else if (av1.equalsIgnoreCase(av2)) {
                similarity ++;
            }

            // TODO: Deal with time/date
            // Need to read from vertex.getTimeString()
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
        combinedVertexList.put(v1.getID(), v1);
        combinedVertexList.put(v2.getID(), v2);
        
        // Code here
        
        // Create new ID
        
        // Create Label
        
        // Set Vertex Time
        
        // Generate the attributes for the combined vertex from both vertices
        
        throw new UnsupportedOperationException("Not supported yet.");

//        return combinedVertex;
    }

    /**
     * Method to add a vertex in the combined graph
     *
     * @param vertex is the vertex to be added
     */
    public void addVertex(Vertex vertex) {
        vertexList.add(vertex);
    }

    /**
     * Method to add vertices from a graph to the combined graph
     *
     * @param vertices is the graph that contains the vertices to be added
     */
    public void addVertices(Collection<Object> vertices) {
        for(Object vertex : vertices) {
            // Add only the vertices that were not used to generate a combined vertex
            if(combinedVertexList.get(((Vertex)vertex).getID()) == null) {
                addVertex((Vertex)vertex);
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
        Collection<Edge> updatedEdges = new ArrayList<Edge>();

        // Code to insert edges in updatedEdges and update those that has a combined vertex as source or target 
        throw new UnsupportedOperationException("Not supported yet.");

//        return updatedEdges;
    }

    /**
     * Method to add edges from a Graph in the combined graph
     *
     * @param edges
     */
    public void addEdges(Collection<Edge> edges) {
        edgeList.addAll(edges);
    }

    /**
     * Method to combine duplicate or similar edges from the combined graph
     */
    public void combineEdges() {
        // Code to combine similar edges from edgeList
        throw new UnsupportedOperationException("Not supported yet.");

    }
}
