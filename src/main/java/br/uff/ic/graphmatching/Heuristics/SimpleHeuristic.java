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

    public void MatchGraphs(DirectedGraph<Object, Edge> graph_01, DirectedGraph<Object, Edge> graph_02, GraphMatching combiner) {
        Collection<Object> g1_vertices = graph_01.getVertices();
        Collection<Object> g2_vertices = graph_02.getVertices();
        Map<String, Object> v1List = new HashMap<String, Object>();
        Map<String, Object> v2List = new HashMap<String, Object>();
        
        for (Object v1 : g1_vertices) {
            for (Object v2 : g2_vertices) {
                if(combiner.isSimilar((Vertex)v1, (Vertex)v2)) {
                    if(!(v1List.containsKey(((Vertex)v1).getID())) && !(v2List.containsKey(((Vertex)v2).getID()))) {
                        Object cv = combiner.combineVertices ((Vertex)v1, (Vertex)v2);
                        combiner.addVertex((Vertex)cv);
                        v1List.put(((Vertex)v1).getID(), v1);
                        v2List.put(((Vertex)v2).getID(), v2);
                    }
                }
            }
        }
    }
    
}
