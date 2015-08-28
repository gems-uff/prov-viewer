/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.utility.Attribute;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Kohwalter
 */
public class GraphMatching {
    
    private Collection<Object> vertexList;
    private Collection<Edge> edgeList;
    private Collection<Attribute> attributeList;    // Attribute.name = the atribute 
                                                    // Attribute.value = error margin
    
    /**
     * Constructor
     * @param restrictionList is the list of attributes, and their error margin, that is used to compare vertices
     */
    public void GraphMatching(Collection<Attribute> restrictionList) {
        vertexList = new ArrayList<Object>();
        edgeList = new ArrayList<Edge>();
        attributeList = restrictionList;
    }
    
    /**
     * Return the vertices from the combined graph
     * @return 
     */
    public Collection<Object> getVertexList(){
        return vertexList;
    }
    
    /**
     * Return the edges from the combined graph
     * @return 
     */
    public Collection<Edge> getEdgeList(){
        return edgeList;
    }
    
    /**
     * Function that determines if two vertices are equivalent
     * @param v1 is the first vertex
     * @param v2 is the second vertex
     * @return TRUE if equivalent and FALSE if not
     */
    public boolean isSimilar(Vertex v1, Vertex v2) {
        boolean isSimilar = false;
        // Code here
        // Use the attributeList to determine if vertices are similar
        
        throw new UnsupportedOperationException("Not supported yet.");
        
//        return isSimilar;
    }
    
    /**
     * Function to combine two vertices
     * @param v1 is the first vertex
     * @param v2 is the second vertex
     * @return the combined vertex
     */
    public Vertex combineVertices(Vertex v1, Vertex v2) {
        Vertex combinedVertex = null;
        
        // Code here
        throw new UnsupportedOperationException("Not supported yet.");
        
//        return combinedVertex;
    }
    
    /**
     * Method to add a vertex in the combined graph
     * @param vertex is the vertex to be added
     */
    public void addVertex(Vertex vertex) {
        vertexList.add(vertex);
    }
    
    /**
     * Method to add vertices from a graph to the combined graph
     * @param vertices is the graph that contains the vertices to be added
     */
    public void addVertices(Collection<Object> vertices) {
        // Code to add vertices in vertexList
        // Do not add vertices that were used to create a combined vertex
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Function to update edges that has a vertex as source or target which was combined
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
