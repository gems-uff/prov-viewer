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
package br.uff.ic.graphmatching.Heuristics;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.graphmatching.MatchingHeuristic;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class SimpleHeuristic implements MatchingHeuristic{

    @Override
    public void MatchGraphs(DirectedGraph<Object, Edge> graph_01, DirectedGraph<Object, Edge> graph_02, GraphMatching combiner) {
        List<Object> g1_vertices = new ArrayList(graph_01.getVertices());
        List<Object> g2_vertices = new ArrayList(graph_02.getVertices());
        Map<String, Object> v1List = new HashMap<>();
        Map<String, Object> v2List = new HashMap<>();
        Comparator comparator = new Comparator<Object>() {
            @Override
            public int compare(Object c1, Object c2) {
                if (!(c1 instanceof Graph) && !(c2 instanceof Graph)) {
                    double c1t = ((Vertex) c1).getTime();
                    double c2t = ((Vertex) c2).getTime();
                    return Double.compare(c1t, c2t);
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(g1_vertices, comparator);
        Collections.sort(g2_vertices, comparator);
        
        for (Object v1 : g1_vertices) {
            for (Object v2 : g2_vertices) {
                if(combiner.isSimilar((Vertex)v1, (Vertex)v2)) {
                    if(!(v1List.containsKey(((Vertex)v1).getID())) && !(v2List.containsKey(((Vertex)v2).getID()))) {
                        Object cv = combiner.combineVertices ((Vertex)v1, (Vertex)v2);
                        combiner.addVertex((Vertex)cv);
                        v1List.put(((Vertex)v1).getID(), v1);
                        v2List.put(((Vertex)v2).getID(), v2);
                    }
                    break;
                }
            }
        }
    }
    
}
