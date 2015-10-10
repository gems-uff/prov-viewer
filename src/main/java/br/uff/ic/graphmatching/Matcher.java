/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.graphmatching.Heuristics.SimpleHeuristic;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class Matcher {
    
    public DirectedGraph<Vertex, Edge> Matching(
            DirectedGraph<Vertex, Edge> graph_01, 
            DirectedGraph<Vertex, Edge> graph_02, 
            Map<String, GraphAttribute> restrictionList, 
            double similarityThreshold) {
        
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold);
        
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
    
    public void updateVertexIDs(DirectedGraph<Vertex, Edge> graph, String ID) {
        for (Vertex v : graph.getVertices()) {
            v.setID(ID + "_" + v.getID());
        }
    }
    
    public void updateEdgeIDs(DirectedGraph<Vertex, Edge> graph, String ID) {
        for (Edge e : graph.getEdges()) {
            e.setID(ID + "_" + e.getID());
        }
    }
}
