/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.graphmatching.Heuristics.SimpleHeuristic;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Vocabulary;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphMerger {
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
            Vocabulary vocabulary,
            float similarityThreshold) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary.getVocabulary(), similarityThreshold);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @param defaultError
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
            Vocabulary vocabulary,
            float similarityThreshold, String defaultError) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary.getVocabulary(), similarityThreshold, defaultError);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @param defaultError
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
             Map<String, String> vocabulary,
            float similarityThreshold, String defaultError) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary, similarityThreshold, defaultError);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param vocabulary
     * @param similarityThreshold
     * @param defaultError
     * @param defaultWeight
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList,
             Map<String, String> vocabulary,
            float similarityThreshold, String defaultError, float defaultWeight) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, vocabulary, similarityThreshold, defaultError, defaultWeight);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param similarityThreshold
     * @param defaultError
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList, 
            float similarityThreshold, String defaultError) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold, defaultError);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param restrictionList
     * @param similarityThreshold
     * @return 
     */
    public DirectedGraph<Object, Edge> Merging(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            Map<String, AttributeErrorMargin> restrictionList, 
            float similarityThreshold) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold);
        return MergeGraphs(graph_01, graph_02, combiner);
    }
    
    /**
     * 
     * @param graph_01
     * @param graph_02
     * @param combiner
     * @return 
     */
    private DirectedGraph<Object, Edge> MergeGraphs(
            DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            GraphMatching combiner) {
        // Correct IDs
        updateVertexIDs(graph_01, "Graph_01");
        updateVertexIDs(graph_02, "Graph_02");
        updateEdgeIDs(graph_01, "Graph_01");
        updateEdgeIDs(graph_02, "Graph_02");
        
        // Matching Heuristic
        MatchingHeuristic heuristic = new SimpleHeuristic();
        heuristic.MatchGraphs(graph_01, graph_02, combiner);
        
        // After matching all vertices
        combiner.addVertices(graph_01.getVertices());
        combiner.addVertices(graph_02.getVertices());
        Collection<Edge> updatedG1edges = combiner.updateEdges(graph_01.getEdges());
        Collection<Edge> updatedG2edges = combiner.updateEdges(graph_02.getEdges());
        combiner.addEdges(updatedG1edges);
        combiner.addEdges(updatedG2edges);
        
        return combiner.getCombinedGraph();
    }
    
    /**
     * 
     * @param graph
     * @param ID 
     */
    public void updateVertexIDs(DirectedGraph<Object, Edge> graph, String ID) {
        for (Object v : graph.getVertices()) {
            ((Vertex)v).setID(ID + "_" + ((Vertex)v).getID());
        }
    }
    
    /**
     * 
     * @param graph
     * @param ID 
     */
    public void updateEdgeIDs(DirectedGraph<Object, Edge> graph, String ID) {
        for (Edge e : graph.getEdges()) {
            e.setID(ID + "_" + e.getID());
        }
    }
}
