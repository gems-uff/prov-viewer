/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;

/**
 *
 * @author Kohwalter
 */
public interface MatchingHeuristic {
    
    public void MatchGraphs(DirectedGraph<Object, Edge> graph_01, 
            DirectedGraph<Object, Edge> graph_02, 
            GraphMatching combiner);
    
}
