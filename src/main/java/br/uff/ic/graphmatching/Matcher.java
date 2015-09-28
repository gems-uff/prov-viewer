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
}
