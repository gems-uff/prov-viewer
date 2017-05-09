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
package br.uff.ic.utility;

import br.uff.ic.utility.graph.Edge;
import java.util.Collection;
import java.util.logging.Logger;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.util.HashMap;
import java.util.Map;

public class GraphCollapser {

    private static final Logger logger = Logger.getLogger(GraphCollapser.class.getClass().getName());
    private Graph originalGraph;

    public GraphCollapser(Graph originalGraph) {
        this.originalGraph = originalGraph;

    }

    Graph createGraph() throws InstantiationException, IllegalAccessException {
        return (Graph) originalGraph.getClass().newInstance();
    }

    public Graph collapse(Graph inGraph, Graph clusterGraph) {
        if (clusterGraph.getVertexCount() < 2) {
            return inGraph;
        }
        Graph graph = inGraph;
        try {
            graph = createGraph();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Collection cluster = clusterGraph.getVertices();
        Map<String, Object> collapsedEdges = new HashMap<>();
        Map<String, Object> mergedEdges = new HashMap<>();

        // add all vertices in the delegate, unless the vertex is in the
        // cluster.
        for (Object v : inGraph.getVertices()) {
            if (cluster.contains(v) == false) {
                graph.addVertex(v);
            }
        }
        // add the clusterGraph as a vertex
        graph.addVertex(clusterGraph);

        //add all edges from the inGraph, unless both endpoints of
        // the edge are in the cluster
        for (Object e : (Collection<?>) inGraph.getEdges()) {
            Pair endpoints = inGraph.getEndpoints(e);
            // don't add edges whose endpoints are both in the cluster
            if (cluster.containsAll(endpoints) == false) {
                if (cluster.contains(endpoints.getFirst())) {
                        Object edge = hasEdge(collapsedEdges, mergedEdges, e, ((Edge)e).getType(), cluster.hashCode(), endpoints.getSecond().hashCode());
                        graph.addEdge(edge, clusterGraph, endpoints.getSecond(), inGraph.getEdgeType(e));
                        graph.addEdge(e, clusterGraph, endpoints.getSecond(), inGraph.getEdgeType(e));
                } else if (cluster.contains(endpoints.getSecond())) {
                        Object edge = hasEdge(collapsedEdges, mergedEdges, e, ((Edge)e).getType(), endpoints.getFirst().hashCode(), cluster.hashCode());
                        graph.addEdge(edge, endpoints.getFirst(), clusterGraph, inGraph.getEdgeType(e));
                        graph.addEdge(e, endpoints.getFirst(), clusterGraph, inGraph.getEdgeType(e));
                } else {
                    graph.addEdge(e, endpoints.getFirst(), endpoints.getSecond(), inGraph.getEdgeType(e));
                }
            }
        }
        
        return graph;
    }

    private Object hasEdge(Map<String, Object> collapsedEdges, Map<String, Object> mergedEdges, Object e, String type, int source, int target) {
        String key = type + " " + source + " " + target;
        if(collapsedEdges.containsKey(key)) {
            ((Edge)e).setHide(true);
            Object edge = collapsedEdges.get(key);
            ((Edge)edge).setHide(true);
            // If first time, create the edge
            if(!mergedEdges.containsKey(key)) {
                Edge newEdge = new Edge(((Edge)edge).getID(), ((Edge)edge).getType(), ((Edge)edge).getStringValue(), "(Merged) " + ((Edge)edge).getLabel(), ((Edge)edge).attributes, ((Edge)edge).getTarget(), ((Edge)edge).getSource());
                newEdge = newEdge.merge((Edge) e);
                mergedEdges.put(key, newEdge);
                return newEdge;
            }
            else {
                // Else update it
                Edge newEdge = ((Edge)mergedEdges.get(key)).merge((Edge) e);
                mergedEdges.put(key, newEdge);
                return newEdge;
                // Need to update the summarized edge values
                // Need to hide e
//            return true;
            }
        }
        else {
            collapsedEdges.put(key, e);
            return e;
//            ((Edge)e).setHide(true);
            // Need to create a single summarized edge
            // Need to hide e
//            return false;
        }
//        collapsedEdges.put(key, e);
    }
    public Graph expand(Graph inGraph, Graph clusterGraph) {
        Graph graph = inGraph;
        try {
            graph = createGraph();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Collection cluster = clusterGraph.getVertices();
        logger.fine("cluster to expand is " + cluster);

        // put all clusterGraph vertices and edges into the new Graph
        for (Object v : cluster) {
            graph.addVertex(v);
            for (Object edge : clusterGraph.getIncidentEdges(v)) {
                Pair endpoints = clusterGraph.getEndpoints(edge);
                graph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond(), clusterGraph.getEdgeType(edge));
            }
        }
        // add all the vertices from the current graph except for
        // the cluster we are expanding
        for (Object v : inGraph.getVertices()) {
            if (v.equals(clusterGraph) == false) {
                graph.addVertex(v);
            }
        }
        // now that all vertices have been added, add the edges,
        // ensuring that no edge contains a vertex that has not
        // already been added
        for (Object v : inGraph.getVertices()) {
            if (v.equals(clusterGraph) == false) {
                for (Object edge : inGraph.getIncidentEdges(v)) {
                    Pair endpoints = inGraph.getEndpoints(edge);
                    Object v1 = endpoints.getFirst();
                    Object v2 = endpoints.getSecond();
                    if (cluster.containsAll(endpoints) == false) {
                        if (clusterGraph.equals(v1)) {
                             if(!((Edge)edge).getLabel().contains("(Merged)")) {
                                // i need a new v1
    //                            Object originalV1 = originalGraph.getEndpoints(edge).getFirst();
                                Object originalV1 = ((Edge)edge).getSource();
                                Object newV1 = findVertex(graph, originalV1);
                                assert newV1 != null : "newV1 for " + originalV1 + " was not found!";
                                ((Edge)edge).setHide(false);
                                graph.addEdge(edge, newV1, v2, inGraph.getEdgeType(edge));
                             }
                        } else if (clusterGraph.equals(v2)) {
                             if(!((Edge)edge).getLabel().contains("(Merged)")) {
                                // i need a new v2
    //                            Object originalV2 = originalGraph.getEndpoints(edge).getSecond();
                                Object originalV2 = ((Edge)edge).getTarget();
                                Object newV2 = findVertex(graph, originalV2);
                                assert newV2 != null : "newV2 for " + originalV2 + " was not found!";
                                ((Edge)edge).setHide(false);
                                graph.addEdge(edge, v1, newV2, inGraph.getEdgeType(edge));
                             }
                        } else {
                            graph.addEdge(edge, v1, v2, inGraph.getEdgeType(edge));
                        }
                    }
                }
            }
        }
        return graph;
    }

    Object findVertex(Graph inGraph, Object vertex) {
        Collection vertices = inGraph.getVertices();
        if (vertices.contains(vertex)) {
            return vertex;
        }
        for (Object v : vertices) {
            if (v instanceof Graph) {
                Graph g = (Graph) v;
                if (contains(g, vertex)) {
                    return v;
                }
            }
        }
        return null;
    }

    private boolean contains(Graph inGraph, Object vertex) {
        boolean contained = false;
        if (inGraph.getVertices().contains(vertex)) {
            return true;
        }
        for (Object v : inGraph.getVertices()) {
            if (v instanceof Graph) {
                contained |= contains((Graph) v, vertex);
            }
        }
        return contained;
    }

    public Graph getClusterGraph(Graph inGraph, Collection picked) {
        Graph clusterGraph;
        try {
            clusterGraph = createGraph();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        for (Object v : picked) {
            clusterGraph.addVertex(v);
            Collection edges = inGraph.getIncidentEdges(v);
            for (Object edge : edges) {
                Pair endpoints = inGraph.getEndpoints(edge);
                Object v1 = endpoints.getFirst();
                Object v2 = endpoints.getSecond();
                if (picked.containsAll(endpoints)) {
                    clusterGraph.addEdge(edge, v1, v2, inGraph.getEdgeType(edge));
                }
            }
        }
        return clusterGraph;
    }
}
