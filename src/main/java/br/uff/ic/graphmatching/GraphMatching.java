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
package br.uff.ic.graphmatching;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.GraphCollapser;
import br.uff.ic.utility.GraphUtils;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.GraphVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
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
    private String defaultError;
    private float defaultWeight;
    Map<String, Edge> duplicateEdges;
    private final Map<String, Object> vertexList;
    private final Map<String, Edge> edgeList;
    private final Map<String, Object> combinedVertexList;
    private Map<String, AttributeErrorMargin> attributeList;  
    private final Map<String, String> vocabulary; 
    DirectedGraph<Object, Edge> cg = new DirectedSparseMultigraph<>();
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
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, Map<String, String> vocabulary, float similarityThreshold) {
        vertexList = new HashMap<>();
        edgeList = new HashMap<>();
        attributeList = restrictionList;
        this.vocabulary = vocabulary;
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0f, 1.0f, similarityThreshold);
        combinedVertexList = new HashMap<>();
        duplicateEdges = new HashMap<>();
        defaultError = "0";
        defaultWeight = 1;
    }
    
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, Map<String, String> vocabulary, float similarityThreshold, String errorMargin) {
        vertexList = new HashMap<>();
        edgeList = new HashMap<>();
        attributeList = restrictionList;
        this.vocabulary = vocabulary;
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0f, 1.0f, similarityThreshold);
        combinedVertexList = new HashMap<>();
        duplicateEdges = new HashMap<>();
        defaultError = errorMargin;
        defaultWeight = 1;
    }
    
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, Map<String, String> vocabulary, float similarityThreshold, String errorMargin, float weight) {
        vertexList = new HashMap<>();
        edgeList = new HashMap<>();
        attributeList = restrictionList;
        this.vocabulary = vocabulary;
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0f, 1.0f, similarityThreshold);
        combinedVertexList = new HashMap<>();
        duplicateEdges = new HashMap<>();
        defaultError = errorMargin;
        defaultWeight = weight;
    }
    
    /**
     * Constructor
     *
     * @param restrictionList is the list of attributes, and their error margin,
     * that is used to compareAttributes vertices
     * @param similarityThreshold is the percentage used to define when two
     * vertices are considered similar. Varies from 0 to 1.0
     */
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, float similarityThreshold) {
        vertexList = new HashMap<>();
        edgeList = new HashMap<>();
        attributeList = restrictionList;
        this.vocabulary = new HashMap<>();
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0f, 1.0f, similarityThreshold);
        combinedVertexList = new HashMap<>();
        duplicateEdges = new HashMap<>();
        defaultError = "0";
        defaultWeight = 1;
    }
    
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, float similarityThreshold, String errorMargin) {
        vertexList = new HashMap<>();
        edgeList = new HashMap<>();
        attributeList = restrictionList;
        this.vocabulary = new HashMap<>();
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0f, 1.0f, similarityThreshold);
        combinedVertexList = new HashMap<>();
        duplicateEdges = new HashMap<>();
        defaultError = errorMargin;
        defaultWeight = 1;
    }
    
    public GraphMatching(Map<String, AttributeErrorMargin> restrictionList, float similarityThreshold, String errorMargin, float weight) {
        vertexList = new HashMap<>();
        edgeList = new HashMap<>();
        attributeList = restrictionList;
        this.vocabulary = new HashMap<>();
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0f, 1.0f, similarityThreshold);
        combinedVertexList = new HashMap<>();
        duplicateEdges = new HashMap<>();
        defaultError = errorMargin;
        defaultWeight = weight;
    }

    /**
     * Constructor without a list of attributes with their error margins
     *
     * @param similarityThreshold is the percentage used to define when two
     * vertices are considered similar. Varies from 0 to 1.0
     */
    public GraphMatching(float similarityThreshold) {
        vertexList = new HashMap<>();
        edgeList = new HashMap<>();
        attributeList = new HashMap<>();
        vocabulary = new HashMap<>(); 
        threshold = similarityThreshold;
        threshold = Utils.clamp(0.0f, 1.0f, similarityThreshold);
        combinedVertexList = new HashMap<>();
        duplicateEdges = new HashMap<>();
        defaultError = "0";
        defaultWeight = 1;
    }

    /**
     * Return the vertices from the combined graph
     *
     * @return
     */
    public Collection<Object> getVertexList() {
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
     * Method to return the restriction list
     * @return 
     */
    public Map<String, AttributeErrorMargin> getRestrictionList() {
        return attributeList;
    }
    
    /**
     * Method to update the restriction list 
     * @param list is the new restriction list
     */
    public void setRestrictionList(Map<String, AttributeErrorMargin> list) {
//        attributeList.clear();
//        attributeList.putAll(list);
        attributeList = new HashMap<>(list);
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
        float similarity = 0.0F;

        // Compare vertex types: If different types than it is not similar
        if (!GraphUtils.isSameVertexTypes(v1, v2)) {
            return false;
        }

        Map<String, String> attributes = new HashMap<>();

        // Check all v1 attributes
        for (GraphAttribute attribute : v1.getAttributes()) {
//            if(!attribute.getName().equalsIgnoreCase("GraphFile")) {
            attributes.put(attribute.getName(), attribute.getName());
            similarity = compareAttributes(attribute, v2, similarity);
//            }
        }

        // Now check all v2 attributes
        for (GraphAttribute attribute : v2.getAttributes()) {
//            if(!attribute.getName().equalsIgnoreCase("GraphFile")) {
            // Do not check the same attributes already verified when checking v1
            if (!attributes.containsKey(attribute.getName())) {
                attributes.put(attribute.getName(), attribute.getName());
                similarity = compareAttributes(attribute, v1, similarity);
            }
//            }
        }

        // Compute the number of attributes, considering their weights, for the similarity check
        float size = 0;
        for (String att : attributes.values()) {
            float weight = defaultWeight;
            if (attributeList.get(att) != null) {
                weight = attributeList.get(att).getWeight();
            }
            size = size + (1 * weight);
        }
//        if(v1.getLabel().equalsIgnoreCase("Player") && v2.getLabel().equalsIgnoreCase("Player")) {
//            System.out.println(v1.getID() + " " + v2.getID());
//            System.out.println("Similarity: " + similarity);
//            System.out.println("Size: " + size);
//        }
        similarity = similarity / size;

//        System.out.println("Match Similarity between " + v1.getID() + " and " + v2.getID() + ": " + similarity);
        if (similarity >= threshold) {
            isSimilar = true;
            v1.addAttribute(new GraphAttribute(VariableNames.similarityAttribute, similarity * 100 + "%"));
        }

        return isSimilar;
    }

    /**
     * Function to compare all attributes from 2 verties
     *
     * @param attribute is the current attribute from v1
     * @param v2 is the second vertex
     * @param similarity is the similarity variable used to discern if vertices
     * are similar
     * @return
     */
    public float compareAttributes(GraphAttribute attribute, Vertex v2, float similarity) {
//        attributes.put(attribute.getName(), attribute.getName());
        String attName = attribute.getName();
        if (v2.getAttribute(attName) != null) {
            String av1 = attribute.getAverageValue();
            String av2 = v2.getAttribute(attName).getAverageValue();
            String errorMargin = defaultError;
            float weight = defaultWeight;
            if (attributeList.get(attName) != null) {
                errorMargin = attributeList.get(attName).getValue();
                weight = attributeList.get(attName).getWeight();
            }
            if (weight != 0) {
                // Dealing with numeric values
//                System.out.println(attribute.getName() + ": " + av1 + " / " + av2 + " error: " + errorMargin);
                if (Utils.tryParseFloat(av1) && Utils.tryParseFloat(av2)) { // && Utils.tryParseFloat(errorMargin)) {
                    if (Utils.tryParseFloat(errorMargin)) {
                        if (Utils.FloatEqualTo(Utils.convertFloat(av1), Utils.convertFloat(av2), Utils.convertFloat(errorMargin))) {
                            similarity = similarity + (1 * weight);
                        }
                    } else if (errorMargin.contains("%")) {
                        errorMargin = errorMargin.replaceAll("%", "");
                        if (Utils.FloatSimilar(Utils.convertFloat(av1), Utils.convertFloat(av2), Utils.convertFloat(errorMargin) * 0.01f)) {
                            similarity = similarity + (1 * weight);
                        }
                    }
                } // Dealing with a timeDate values
                else if (Utils.tryParseDate(av1) && Utils.tryParseDate(av2)) {
                    if (Utils.DoubleEqualTo(Utils.convertStringDateToFloat(av1), Utils.convertStringDateToFloat(av2), Utils.convertFloat(errorMargin))) {
                        similarity = similarity + (1 * weight);
                    }
                } // Dealing with string values: Checking if they are equals
                else if (av1.equalsIgnoreCase(av2)) {
                    similarity = similarity + (1 * weight);
                } // Dealing with String values: Checking if they are in the Vocabulary and thus synonymous
                else if (vocabulary.containsKey(av1.toLowerCase())) {
                    if (vocabulary.get(av1.toLowerCase()).contains(av2.toLowerCase() + " ")) {
                        similarity = similarity + (1 * weight);
                    }
                }
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
            combinedVertex = new ActivityVertex(v1.getID() + ", " + v2.getID(), v1.getLabel(), v1.getTimeString());
        } else if (v1 instanceof EntityVertex) {
            combinedVertex = new EntityVertex(v1.getID() + ", " + v2.getID(), v1.getLabel(), v1.getTimeString());
        } else {
            combinedVertex = new AgentVertex(v1.getID() + ", " + v2.getID(), v1.getLabel(), v1.getTimeString());
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
//        combinedVertex.setID(combinedVertex.getID().replace(" (Merged)", ""));
//        combinedVertex.setID(combinedVertex.getID() + "_" + v2.getID() + " (Merged)");
//        if(!combinedVertex.getLabel().equalsIgnoreCase(v2.getLabel()))
//            combinedVertex.setLabel(combinedVertex.getLabel() + "_" + v2.getLabel());        

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
    public void addVertices(Collection<Object> vertices) {
        for (Object vertex : vertices) {
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
        Collection<Edge> newEdges = new ArrayList<>();

        for (Edge edge : edges) {
            Edge updatedEdge = edge;
            if (combinedVertexList.containsKey(((Vertex)edge.getSource()).getID())) {
                updatedEdge.setSource(combinedVertexList.get(((Vertex)edge.getSource()).getID()));
            }
            if (combinedVertexList.containsKey(((Vertex)edge.getTarget()).getID())) {
                updatedEdge.setTarget(combinedVertexList.get(((Vertex)edge.getTarget()).getID()));
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
        Collection<Edge> values = new ArrayList<>();
        values.addAll(edgeList.values());
        duplicateEdges = new HashMap<>();
        
        for (Edge e1 : values) {
            for (Edge e2 : values) {
                if (!(duplicateEdges.containsKey(e1.getID()) && duplicateEdges.containsKey(e2.getID()))) {
                    if(!e1.getID().equalsIgnoreCase(e2.getID())) {
                        if(e1.getType().equalsIgnoreCase(e2.getType())) {
                            if(((Vertex)e1.getSource()).getID().equalsIgnoreCase(((Vertex)e2.getSource()).getID())) {
                                if(((Vertex)e1.getTarget()).getID().equalsIgnoreCase(((Vertex)e2.getTarget()).getID())) {
                                    edgeList.remove(e1.getID());
                                    edgeList.remove(e2.getID());
                                    // Merge E1 with E2
                                    Edge mergedEdge = new Edge(((Edge)e1).getID(), ((Edge)e1).getType(), ((Edge)e1).getStringValue(), ((Edge)e1).getLabel(), ((Edge)e1).attributes, ((Edge)e1).getTarget(), ((Edge)e1).getSource());
                                    mergedEdge.merge(e2, "From_GraphMerge");
                                    edgeList.put(mergedEdge.getID(), mergedEdge);
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
    public DirectedGraph<Object, Edge> getCombinedGraph() {
        DirectedGraph<Object, Edge> combinedGraph = new DirectedSparseMultigraph<>();

        removeDuplicateEdges();

        for (Edge edge : this.getEdges()) {
            combinedGraph.addEdge(edge, (Vertex) edge.getSource(), (Vertex) edge.getTarget());
        }

        return combinedGraph;
    }
    
    public DirectedGraph<Object, Edge> CG() {
        return cg;
    }
    
    public void asd(DirectedGraph<Object, Edge> graph_01, DirectedGraph<Object, Edge> graph_02) {
        for(Object v : graph_01.getVertices()) {
            cg.addVertex(v);
        }
        for(Object v : graph_02.getVertices()) {
            cg.addVertex(v);
        }
        for(Edge e : graph_01.getEdges()) {
            Pair endpoints = graph_01.getEndpoints(e);
            Object v1 = endpoints.getFirst();
            Object v2 = endpoints.getSecond();
            cg.addEdge(e, v1, v2);
        }
        for(Edge e : graph_02.getEdges()) {
           Pair endpoints = graph_02.getEndpoints(e);
            Object v1 = endpoints.getFirst();
            Object v2 = endpoints.getSecond();
            cg.addEdge(e, v1, v2);
        }
    }
    
    public void combineVertices2(Vertex v1, Vertex v2) {
        DirectedGraph<Object, Edge> clusterGraph = new DirectedSparseMultigraph<>();
        Collection picked = new ArrayList();
        picked.add(v1);
        picked.add(v2);
        GraphCollapser gCollapser = new GraphCollapser(cg, false);
        GraphVertex combinedVertex = gCollapser.getClusterGraph(cg, picked);
        cg = (DirectedGraph<Object, Edge>) gCollapser.collapse(cg, combinedVertex);
//        combinedVertexList.put(v1.getID(), combinedVertex);
//        combinedVertexList.put(v2.getID(), combinedVertex);
    }
    
    private void createMergedVertex(DirectedGraph<Object, Edge> clusterGraph, Vertex v, DirectedGraph<Object, Edge> graph) {
        clusterGraph.addVertex(v);
        Collection edges = graph.getIncidentEdges(v);
        for (Object edge : edges) {
            Object source = ((Edge)edge).getSource();
            Object target = ((Edge)edge).getTarget();
//            if (picked.containsAll(endpoints)) {
            clusterGraph.addEdge((Edge)edge, source, target, graph.getEdgeType((Edge)edge));
//            }
        }
    }
}
