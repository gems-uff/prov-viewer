/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching.Heuristics;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.graphmatching.MatchingHeuristic;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class SimpleHeuristic implements MatchingHeuristic{

    public void MatchGraphs(DirectedGraph<Vertex, Edge> graph_01, DirectedGraph<Vertex, Edge> graph_02, GraphMatching combiner) {
        Collection<Vertex> g1_vertices = graph_01.getVertices();
        Collection<Vertex> g2_vertices = graph_02.getVertices();
        Map<String, Vertex> v1List = new HashMap<String, Vertex>();
        Map<String, Vertex> v2List = new HashMap<String, Vertex>();
        
        for (Vertex v1 : g1_vertices) {
            for (Vertex v2 : g2_vertices) {
                if(combiner.isSimilar(v1, v2)) {
                    if(!(v1List.containsKey(v1.getID())) && !(v2List.containsKey(v2.getID()))) {
                        Vertex cv = combiner.combineVertices (v1, v2);
                        combiner.addVertex(cv);
                        v1List.put(v1.getID(), v1);
                        v2List.put(v2.getID(), v2);
                    }
                }
            }
        }
    }
    
}
