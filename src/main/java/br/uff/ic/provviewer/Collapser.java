/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class to Collapse/Expand selected vertices and edges
 *
 * @author Kohwalter
 *
 * TODO: Collapsing only with same Source. Need to collpase with same target as
 * well
 */
public class Collapser {

    /**
     * Method for resetting the Graph to the original state
     *
     * @param variables Variables type
     */
    public void ResetGraph(Variables variables) {
        //Reset graph to the original version
        variables.layout.setGraph(variables.graph);
        variables.collapsedGraph = variables.graph;
        variables.gCollapser = new GraphCollapser(variables.graph);
        //Remove collapsed edges, otherwise they will be invisible when Reset
        for (Object node : variables.graph.getVertices()) {
            RemoveCollapsedEdges(variables, node);
        }
        variables.ComputeEdgeTypeValues();
        variables.filter.RemoveFilters(variables);
    }

    //
    /**
     * Method to remove collapsed edges from the node and un-hide edges
     *
     * @param variables Variable type
     * @param vertex Vertex used to get incident edges
     */
    public void RemoveCollapsedEdges(Variables variables, Object vertex) {
        Graph newGraph = variables.layout.getGraph();
        List edges = new ArrayList(variables.layout.getGraph().getIncidentEdges(vertex));
        for (Object edge : edges) {
            ((Edge) edge).setHide(false);
            if (((Edge) edge).isCollapased()) {
                //remove edge
                newGraph.removeEdge(edge);
            }
        }
    }

    /**
     * Method used to collapse vertices
     *
     * @param variables Variables type
     * @param picked Collection of selected vertices
     * @param refresh
     */
    public void Collapse(Variables variables, Collection picked, boolean refresh) {
        //add all filters to avoid losing information
        variables.filter.AddFilters(variables);
        //If selected more than 1 vertex
        if (picked.size() > 1) {
            //Graph inGraph = layout.getGraph();
            Graph clusterGraph = variables.gCollapser.getClusterGraph(variables.layout.getGraph(), picked);

            variables.collapsedGraph = (DirectedGraph<Object, Edge>) variables.gCollapser.collapse(variables.layout.getGraph(), clusterGraph);
            //Compute the collapsed vertex position from each vertex in it
            double sumx = 0;
            double sumy = 0;
            for (Object v : picked) {
                Point2D p = (Point2D) variables.layout.transform(v);
                sumx += p.getX();
                sumy += p.getY();
            }

            //store position
            Point2D cp = new Point2D.Double(sumx / picked.size(), sumy / picked.size());
            variables.view.getRenderContext().getParallelEdgeIndexFunction().reset();
            //set layout to be the collapsed graph
            variables.layout.setGraph(variables.collapsedGraph);
            //set the collapsed vertex position
            variables.layout.setLocation(clusterGraph, cp);
            variables.view.getPickedVertexState().clear();
            
            if(refresh)
                RefreshGraph(variables);
        }

    }
    
    public void RefreshGraph(Variables variables)
    {
        CollapseEdges(variables);
        variables.ComputeEdgeTypeValues();
        variables.filter.RemoveFilters(variables);
        variables.view.repaint();
//        System.out.println("#Vertices: " + variables.collapsedGraph.getVertexCount());
    }
    /**
     * Collapse edges from the same type and target Problem: Between Collapsed
     * Vertices
     *
     * @param variables Variables type
     */
    // TODO: Change how edge collapse works
    // probably it is better to override the original Collapse function from JUNG
    public void CollapseEdges(final Variables variables) {
        Graph newGraph = variables.layout.getGraph();
//        Map<String, Edge> addEdges = new HashMap<String, Edge>();
        Collection<Edge> addEdges = new ArrayList<>();
        Collection<Edge> removeEdges = new ArrayList<>();
        
        checkEdges(variables, addEdges, removeEdges);
        //Add collapsed edges in the graph
        for (Edge edge : addEdges) {
//            System.out.println(edge.getEdgeInfluence());
            newGraph.addEdge(edge, edge.getSource(), edge.getTarget());
        }
        //Remove old collapsed edges
        for (Edge edge : removeEdges) {
            newGraph.removeEdge(edge);
        }
        variables.layout.setGraph(newGraph);
//        variables.view.repaint();
    }
    
    /**
     * Method underdevelopment to solve a bug with collapsing edges depending on the order of the collapse
     * Use checkEdges method instead
     * @param variables
     * @param addEdges
     * @param removeEdges 
     */
    public void checkEdges_Prototype(final Variables variables, Map<String, Edge> addEdges, Collection<Edge> removeEdges) {
        for (Edge e1 : variables.layout.getGraph().getEdges()) {
            boolean collapse = false;
            Object target = e1.getTarget();
            Object source = e1.getSource();
            float value = e1.getValue();
            int count = 0;
            for (Edge e2 : variables.layout.getGraph().getEdges()) {
                if(!e1.getID().equalsIgnoreCase(e2.getID())) {
                    if(e1.getType().equalsIgnoreCase(e2.getType())) {
                        if(e1.getLabel().equalsIgnoreCase(e2.getLabel())) {
                            boolean targetSameGraph = false;
                            //check if there is any graph_vertex.
                            for (Object g : variables.layout.getGraph().getVertices()) {
                                if (g instanceof Graph) {
                                    //If the graph has the target vertex, then the graph is the target
                                    if (((Graph) g).containsVertex(e1.getTarget()) 
                                            && ((Graph) g).containsVertex(e2.getTarget())) {
                                        targetSameGraph = true;
                                        target = g;
                                    }
                                }
                            }
                            if ((e1.getTarget().equals(e2.getTarget())) || targetSameGraph) {
                                if (e1.isCollapased()) {
                                    removeEdges.add(e1);
                                }
                                else if (e2.isCollapased()) {
                                    removeEdges.add(e2);
                                }
                                else {
                                    value += e2.getValue();
                                    e1.setHide(true);
                                    e2.setHide(true);
                                    collapse = true;
                                    count++;
                                }
                            } // if SAME TARGET
                        } // if LABEL
                    } // if TYPE
                }// if ID
                
            } // end for e2
            
            if (collapse) {
                if (!e1.addInfluence(variables)) {
                    value = value / count;
                }
                String influence;

                influence = e1.getLabel();
                //Create collpased edge and add it in the graph                        
                Edge edge;
                
                for (Object g : variables.layout.getGraph().getVertices()) {
                    if (g instanceof Graph) {
                        //If the graph has the target vertex, then the graph is the target
                        if (((Graph) g).containsVertex(e1.getSource()))
                            source = g;
                    }
                }
                edge = CollapsedEdgeType(target, source, influence, Float.toString(value));
//                if (target == null) {
//                    target = e1.getTarget();
//                    edge = CollapsedEdgeType(target, e1.getSource(), influence, Float.toString(value));
//                } else {
//                    edge = CollapsedEdgeType(target, e1.getSource(), influence, Float.toString(value));
//                }
            
                edge.setCollapse(true);
                addEdges.put(target + " " + source + " " + influence, edge);
            }
        } // end for e1
    }
    public void checkEdges(final Variables variables, Collection<Edge> addEdges, Collection<Edge> removeEdges){
        for (Object node : variables.layout.getGraph().getVertices()) {
            //Only need to collapse if the node is a graph
//            if (node instanceof Graph) {
                List sorted = new ArrayList(variables.layout.getGraph().getOutEdges(node));//.getInEdges(node));
                //Type comparator
                Comparator comparator = new Comparator<Edge>() {
                    @Override
                    public int compare(Edge s1, Edge s2) {
                        String targetS1 = ((Object) s1.getTarget()).toString();
                        String targetS2 = ((Object) s2.getTarget()).toString();
                        for (Object g : variables.layout.getGraph().getVertices()) {
                            if (g instanceof Graph) {
                                if (((Graph) g).containsVertex((s1).getTarget())){
                                    targetS1 = Integer.toString(g.hashCode());
                                }
                                if (((Graph) g).containsVertex((s2).getTarget())){
                                    targetS2 = Integer.toString(g.hashCode());
                                }
                            }
                        }
                        return (s1.getLabel() + targetS1).compareTo((s2.getLabel() + targetS2));
                    }
                };
                //Sort edges by type and target (alphabetical order)
                Collections.sort(sorted, comparator);
                //Run the list of edges
                int j = 0;
                //run the list
                while (j < (sorted.size())) {
                    //for each edge check if their types and destinations are the same
                    //if so, hide them and make a new collapsed edge
                    boolean collapse = false;
                    Object target = null;
                    int count = 1;
                    float value = ((Edge) sorted.get(j)).getValue();

                    // while same type and target
                    while ((j < (sorted.size() - 1)) && 
                            ((Edge) sorted.get(j)).getLabel().equals(((Edge) sorted.get(j + 1)).getLabel())) {
                        boolean targetSameGraph = false;
                        //check if there is any graph_vertex.
                        for (Object g : variables.layout.getGraph().getVertices()) {
                            if (g instanceof Graph) {
                                //If the graph has the target vertex, then the graph is the target
                                if ((((Graph) g).containsVertex(((Edge) sorted.get(j)).getTarget())) 
                                        && (((Graph) g).containsVertex(((Edge) sorted.get(j + 1)).getTarget()))) {
                                    targetSameGraph = true;
                                    target = g;
                                }
                            }
                        }
                        //If they have the same target or a graph has the target
                        if ((((Edge) sorted.get(j)).getTarget().equals(((Edge) sorted.get(j + 1)).getTarget())) || targetSameGraph) {
                            //If the edge is a collapsed one, then remove it
                            //Deal only with the original edges
                            if (((Edge) sorted.get(j + 1)).isCollapased()) {
                                removeEdges.add(((Edge) sorted.get(j + 1)));
                            }
                            else {
                                value += ((Edge) sorted.get(j + 1)).getValue();
                                ((Edge) sorted.get(j)).setHide(true);
                                ((Edge) sorted.get(j + 1)).setHide(true);
                                collapse = true;
                                count++;
                            }
                            j++;
                        } else {
                            break;
                        }
                    }//end-while
                    //If collapased any edge, create it in the graph
                    if (collapse) {
                        if (!((Edge) sorted.get(j)).addInfluence(variables)) {
                            value = value / count;
                        }
                        String influence;
                        
                        influence = ((Edge) sorted.get(j)).getLabel();
                        //Create collpased edge and add it in the graph                        
                        Edge edge;
                        if (target == null) {
                            target = ((Edge) sorted.get(j)).getTarget();
                            edge = CollapsedEdgeType(target, node, influence, Float.toString(value));
                        } else {
                            edge = CollapsedEdgeType(target, node, influence, Float.toString(value));
                        }
                        //======================================================

                        edge.setCollapse(true);
                        addEdges.add(edge);
                    }
                    j++;
                }//end while
//            }//end if
        }
    }

    /**
     * Method for expanding the selected vertices, if they are collapsed
     * vertices
     *
     * @param variables Variables type
     * @param picked Collection of selected vertices
     */
    public void Expander(Variables variables, Collection picked) {
        //for each selected vertex
        for (Object v : picked) {
            //if vertex is a collapsed graph (multiple vertices)
            if (v instanceof Graph) {
                //TODO: Save current filters state
                //Add all filters to not lose edges/information
                variables.filter.AddFilters(variables);
                RemoveCollapsedEdges(variables, v);
                //Expand the vertex
                variables.collapsedGraph = (DirectedGraph<Object, Edge>) variables.gCollapser.expand(variables.layout.getGraph(), (Graph) v);
                variables.view.getRenderContext().getParallelEdgeIndexFunction().reset();
                variables.layout.setGraph(variables.collapsedGraph);
                //TODO: Load filters state
                //Remove filters to clean the visualization
                variables.filter.RemoveFilters(variables);
//                Filters(variables);
            }
        }
        variables.view.getPickedVertexState().clear();
        variables.ComputeEdgeTypeValues();
        variables.view.repaint();
        CollapseEdges(variables);
    }


    /**
     * Method used to create the collapsed edge using the application's edge
     * type
     *
     * @param target (Vertex) Target of the edge
     * @param source (Vertex) source of the edge
     * @param label the label for the edge
     * @param value the value for the edge
     * @return
     */
    public Edge CollapsedEdgeType(Object target, Object source, String label, String value) {
        return new Edge("C", "Collapsed", label, value, target, source);
    }

    public void CollapseIrrelevant(Variables variables, String list, String edgetype) {

        Collection selected = new ArrayList();
        //System.out.println( "L = " + list);
        List<String> collapsegroup = new ArrayList<>();
        List<String> used = new ArrayList<>();

        String[] elements = list.split(" ");

        collapsegroup.addAll(Arrays.asList(elements));
        //Sort list by decreasing order of string.length
        Comparator comparator = new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return c2.length() - c1.length();
            }
        };
        Collections.sort(collapsegroup, comparator);

        Object[] nodes = new Object[variables.graph.getVertexCount()];
        nodes = (variables.graph.getVertices()).toArray();

        //For each elements of collapses
        for (String collapsegroup1 : collapsegroup) {
            //System.out.println("Current Group = " + collapsegroup.get(i));
            String[] vertexlist = collapsegroup1.split(",");
            //For each vertex in the elements
            for (String vertexlist1 : vertexlist) {
                //If vertex was not processed yet
                if (!used.contains(vertexlist1)) {
                    used.add(vertexlist1);
                    //Find the vertex in the graph by its ID
                    for (Object node1 : nodes) {
                        if (((Vertex) (node1)).getID().equalsIgnoreCase(vertexlist1)) {
                            Vertex node = (Vertex) node1;
                            selected.add(node);
                        }
                    }
                }
            }
            //Collapse selected vertices
            if (!selected.isEmpty() && (selected.size() > 1)) {
                Collapse(variables, selected, false);
            } else {
                //If there is only one vertex, then there is no collapse
                Iterator itr = selected.iterator();
                while (itr.hasNext()) {
                    used.remove(((Vertex) itr.next()).getID());
                }
            }
            selected.clear();
        }
        RefreshGraph(variables);
    }

}

//Drafts
    /**
     * Example of mass collapse by Date, grouping vertices from the same week
     * TODO: Correct the vertices position. Manually collapsing preserves
     * position but not when using this function
     *
     * @param variables Variables type
     * @param filter Filters type
     * @param vertex Vertex used as pivot to collapse the neighbors. I.e. Agent
     * vertex for collapsing all his activity vertices
     * @param gran The granularity (int) used to collapse vertices. I.e. 7 by 7
     */
//    collapser.ResetGraph(variables, filter);
//        //Collapse agent's nodes 7 by 7
//        for(Object z : variables.layout.getGraph().getVertices())
//        {
//            if(z instanceof AgentVertex)
//            {
//                collapser.Granularity(variables, filter, z, 7);
//            }
//        }  
    /*
    public void Granularity(Variables variables, Filters filter, Object vertex, int gran) {
        //gran = 7;
        if (vertex instanceof Vertex) {
            //ResetGraph();
            List sorted = new ArrayList(variables.layout.getGraph().getNeighbors(vertex));
            //Date comparator
            Comparator comparator = new Comparator<Vertex>() {
                @Override
                public int compare(Vertex c1, Vertex c2) {
                    return (int)(c1.getDate() - c2.getDate());
                }
            };
            //Sort nodes by date
            Collections.sort(sorted, comparator);
            Collection selected = new ArrayList();
            //Collpase each week
            int j = 0;
            //run the list
            while (j < sorted.size()) {
                //collapse nodes in a factor of "gran" (ex: gran = 7, then collapse by 7 by 7 days
                for (int i = 0; i < gran; i++) {
                    //we want an organized collapse (not only 7 by 7, but by week).
                    while ((j < sorted.size()) && (((ActivityVertex) sorted.get(j)).getDate() % gran) == i) {
                        selected.add(sorted.get(j));
                        j++;
                    }
                }
                //Collection picked = new HashSet(a);
                //Collapse selected vertices
                if (!selected.isEmpty()) {
                    Collapse(variables, filter, selected);
                }
                selected.clear();
            }//end while(list)
        }
    }

 public Graph collapse(Graph inGraph, Graph clusterGraph) {
         
         if(clusterGraph.getVertexCount() < 2) return inGraph;
 
         Graph graph = inGraph;
         try {
             graph = createGraph();
         } catch(Exception ex) {
             ex.printStackTrace();
         }
         Collection cluster = clusterGraph.getVertices();
         
         // add all vertices in the delegate, unless the vertex is in the
         // cluster.
         for(Object v : inGraph.getVertices()) {
             if(cluster.contains(v) == false) {
                 graph.addVertex(v);
             }
         }
         // add the clusterGraph as a vertex
         graph.addVertex(clusterGraph);
         
         //add all edges from the inGraph, unless both endpoints of
         // the edge are in the cluster
         for(Object e : (Collection<?>)inGraph.getEdges()) {
             Pair endpoints = inGraph.getEndpoints(e);
             // don't add edges whose endpoints are both in the cluster
             if(cluster.containsAll(endpoints) == false) {
 
                 if(cluster.contains(endpoints.getFirst())) {
                        //Target = endpoints.getSecond()
                        //Need to keep track of processed targets and edge
                 	graph.addEdge(e, clusterGraph, endpoints.getSecond(), inGraph.getEdgeType(e));
 
                 } else if(cluster.contains(endpoints.getSecond())) {
                 	graph.addEdge(e, endpoints.getFirst(), clusterGraph, inGraph.getEdgeType(e));
 
                 } else {
                 	graph.addEdge(e,endpoints.getFirst(), endpoints.getSecond(), inGraph.getEdgeType(e));
                 }
             }
         }
         return graph;
     }
    */