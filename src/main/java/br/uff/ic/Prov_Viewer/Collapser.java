/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class to Collapse/Expand selected vertices and edges
 * @author Kohwalter
 * 
 */
public class Collapser {
    /**
     * Method for resetting the Graph to the original state
     * @param variables Variables type
     * @param filter Filters type
     */
    public void ResetGraph(Variables variables, Filters filter)
    {
        //Reset graph to the original version
        variables.layout.setGraph(Variables.graph);
        variables.collapsedGraph = Variables.graph;
        //Remove collapsed edges, otherwise they will be invisible when Reset
        for(Object node : Variables.graph.getVertices())
        {
            RemoveCollapsedEdges(variables, node);
        }
        variables.FindMax((DirectedGraph)Variables.graph);
        RemoveFilters(variables, filter);
    }
    
    //
    /**
     * Method to remove collapsed edges from the node and un-hide edges
     * @param variables Variable type
     * @param vertex Vertex used to get incident edges
     */
    public void RemoveCollapsedEdges(Variables variables, Object vertex)
    {
        Graph newGraph = variables.layout.getGraph();
        List edges = new ArrayList(variables.layout.getGraph().getIncidentEdges(vertex));
        for (Object edge : edges)
        {
            ((Edge)edge).SetHide(false);
            if(((Edge)edge).isCollapased())
            {
                //remove edge
                newGraph.removeEdge(edge);
            }
        }
    }
    
    /**
     * Method used to collapse vertices
     * @param variables Variables type
     * @param filter Filters type
     * @param picked Collection of selected vertices
     */
    public void Collapse(Variables variables, Filters filter, Collection picked)
    {
        //add all filters to avoid losing information
        AddFilters(variables, filter);
        //If selected more than 1 vertex
        if(picked.size() > 1) {
            //Graph inGraph = layout.getGraph();
            Graph clusterGraph = variables.gCollapser.getClusterGraph(variables.layout.getGraph(), picked);

            variables.collapsedGraph = (DirectedGraph<Object, Edge>)variables.gCollapser.collapse(variables.layout.getGraph(), clusterGraph);
            //Compute the collapsed vertex position from each vertex in it
            double sumx = 0;
            double sumy = 0;
            for(Object v : picked) 
            {
                Point2D p = (Point2D)variables.layout.transform(v);
                sumx += p.getX();
                sumy += p.getY();
            }
            //store position
            Point2D cp = new Point2D.Double(sumx/picked.size(), sumy/picked.size());
            variables.view.getRenderContext().getParallelEdgeIndexFunction().reset();
            //set layout to be the collapsed graph
            variables.layout.setGraph(variables.collapsedGraph);
            //set the collapsed vertex position
            variables.layout.setLocation(clusterGraph, cp);
            variables.view.getPickedVertexState().clear();
            variables.view.repaint();
        }
        CollapseEdges(variables);
        variables.FindMax((DirectedGraph)Variables.graph);
        RemoveFilters(variables, filter);
    }
    
    /**
     * Collapse edges from the same type and source
     * Problem: Between Collapsed Vertices
     * @param variables Variables type
     */
    public void CollapseEdges(Variables variables)
    {
        Graph newGraph = variables.layout.getGraph();
        Collection<Edge> addEdges = new ArrayList<Edge>();
        Collection<Edge> removeEdges = new ArrayList<Edge>();
        for(Object node : variables.layout.getGraph().getVertices())
        {
            //Only need to collapse if the node is a graph
            if(node instanceof Graph)
            {
                List sorted = new ArrayList(variables.layout.getGraph().getInEdges(node));
//                sorted.addAll(layout.getGraph().getOutEdges(node));
                //Type comparator
                Comparator comparator = new Comparator<Edge>() 
                {
                    @Override
                    public int compare(Edge s1, Edge s2) {
                        return (s1.getType() + ((Object)s1.getSource()).toString()).compareTo((s2.getType() + ((Object)s2.getSource()).toString()));
                    }
                };
                //Sort edges by type and source (alphabetical order)
                Collections.sort(sorted, comparator);
                //Run the list of edges
                int j = 0;
                //run the list
                while(j < (sorted.size()))
                {
                    //for each edge check if their types and destinations are the same
                    //if so, hide them and make a new collapsed edge
                    boolean collapse = false;
                    Object source = null;
                    int count = 1;
                    float value = ((Edge)sorted.get(j)).getValue();
                    
                    // while same type and source
                    while((j < (sorted.size() - 1)) && ((Edge)sorted.get(j)).getType().equals(((Edge)sorted.get(j+1)).getType()))
                    {
                        boolean sameGraph = false;
                        //check if there is any graph_vertex.
                        for(Object g : variables.layout.getGraph().getVertices())
                        {
                            if(g instanceof Graph)
                            {
                                //If the graph has the source vertex, then the graph is the source
                                if((((Graph)g).containsVertex(((Edge)sorted.get(j)).getSource())) || (((Graph)g).containsVertex(((Edge)sorted.get(j+1)).getSource())))
                                {
                                    sameGraph = true;
                                    source = g;
                                }
                            }
                        }
                        //If they have the same source or a graph has the source
                        if((((Edge)sorted.get(j)).getSource().equals(((Edge)sorted.get(j+1)).getSource())) || sameGraph)
                        {
                            //If the edge is a collapsed one, then remove it
                            //Deal only with the original edges
                            if(((Edge)sorted.get(j+1)).isCollapased())
                            {
                                removeEdges.add(((Edge)sorted.get(j+1)));
                            }
                            else
                            {
                                value += ((Edge)sorted.get(j+1)).getValue();
                                ((Edge)sorted.get(j)).SetHide(true);
                                ((Edge)sorted.get(j+1)).SetHide(true);
                                collapse = true;
                                count++;
                            }
                            j++;
                        }
                        else {
                            break;
                        }
                    }//end-while
                    //If collapased any edge, create it in the graph
                    if(collapse)
                    {
//                        System.out.println("Value = " + value);
                        if(!((Edge)sorted.get(j)).AddInfluence())
                        {
//                            System.out.println("Count = " + count);
                            value = value / count;
                        }
                        String influence;
                        //If it is neutral, dont add the 0.0 value
                        if(((Edge)sorted.get(j)).getType().equalsIgnoreCase("Neutral")) {
                            influence = ((Edge)sorted.get(j)).getType();
                        }
                        else {
                            influence = value + " " + ((Edge)sorted.get(j)).getType();
                        }
                        //Create collpased edge and add it in the graph                        
                        Edge edge;
                        if(source == null)
                        {
                            source = ((Edge)sorted.get(j)).getSource();
                            edge = CollapsedEdgeType(node, source, influence);
                        }
                        else
                        {
                            edge = CollapsedEdgeType(node, source, influence);
                        }
                        //======================================================
                        
                        edge.SetCollapse(true);
                        addEdges.add(edge);
                    }
                    j++;
                }//end while
            }//end if
        }//end for
        //Add collapsed edges in the graph
        for (Edge edge : addEdges) 
        {
//            System.out.println(edge.getInfluence());
            newGraph.addEdge(edge, edge.getSource(), edge.getTarget());
        }
        //Remove old collapsed edges
        for (Edge edge : removeEdges) 
        {
            newGraph.removeEdge(edge);
        }
        variables.layout.setGraph(newGraph);
        variables.view.repaint();
    }

    /**
     * Method for expanding the selected vertices, if they are collapsed vertices
     * @param variables Variables type
     * @param filter Filters type
     * @param picked Collection of selected vertices
     */
    public void Expander(Variables variables, Filters filter, Collection picked)
    {
        //for each selected vertex
        for(Object v : picked) 
        {
            //if vertex is a collapsed graph (multiple vertices)
            if(v instanceof Graph) 
            {
                //TODO: Save current filters state
                //Add all filters to not lose edges/information
                AddFilters(variables, filter);
                RemoveCollapsedEdges(variables, v);
                //Expand the vertex
                variables.collapsedGraph = (DirectedGraph<Object, Edge>)variables.gCollapser.expand(variables.layout.getGraph(), (Graph)v);
                variables.view.getRenderContext().getParallelEdgeIndexFunction().reset();
                variables.layout.setGraph(variables.collapsedGraph);
                //TODO: Load filters state
                //Remove filters to clean the visualization
                RemoveFilters(variables, filter);
                Filters(variables, filter);
                
            }
        }
        variables.view.getPickedVertexState().clear();
        variables.FindMax((DirectedGraph)Variables.graph);
        variables.view.repaint();
        CollapseEdges(variables);
    }
    
    /**
     * Method for filtering the Graph
     * @param variables Variables type
     * @param filter Filters type
     * @param hiddenEdges Boolean used to decide if hidden (original edges that composes the collapsed edge) edges will be filtered
     */
    public void Filters(Variables variables, Filters filter, boolean hiddenEdges)
    {
        filter.Filter(variables.view, 
                variables.layout, 
                variables.collapsedGraph,
                hiddenEdges
                );
    }
    
    /**
     * Overload of the Filters method. hiddenEdges is always set as true, filtering the edges that composes the collapsed one
     * @param variables Variables type
     * @param filter Filters type
     */
    public void Filters(Variables variables, Filters filter)
    {
        Filters(variables, filter, true);
    }
    
    /**
     * Example of mass collapse by Date, grouping vertices from the same week
     * TODO: Correct the vertices position. Manually collapsing preserves position but not when using this function
     * @param variables Variables type
     * @param filter Filters type
     * @param vertex Vertex used as pivot to collapse the neighbors. I.e. Agent vertex for collapsing all his activity vertices
     * @param gran The granularity (int) used to collapse vertices. I.e. 7 by 7
     */
    public void Granularity (Variables variables, Filters filter, Object vertex, int gran)
    {
        //gran = 7;
        if(vertex instanceof Vertex)
        {
            //ResetGraph();
            List sorted = new ArrayList(variables.layout.getGraph().getNeighbors(vertex));
            //Date comparator
            Comparator comparator = new Comparator<Vertex>() {
                    @Override
                    public int compare(Vertex c1, Vertex c2) {
                        return c1.getDate() - c2.getDate();
                    }
                };
            //Sort nodes by date
            Collections.sort(sorted, comparator);
            Collection selected = new ArrayList();
            //Collpase each week
            int j = 0;
            //run the list
            while(j < sorted.size())
            {
                //collapse nodes in a factor of "gran" (ex: gran = 7, then collapse by 7 by 7 days
                for(int i = 0; i < gran; i++)
                {
                    //we want an organized collapse (not only 7 by 7, but by week).
                    while((j < sorted.size()) && (((ActivityVertex)sorted.get(j)).getDate() % gran) == i) 
                    {
                        selected.add(sorted.get(j));
                        j++;
                    }
                }
                //Collection picked = new HashSet(a);
                //Collapse selected vertices
                if(!selected.isEmpty()) 
                {
                    Collapse(variables, filter, selected);
                }
                selected.clear();
            }//end while(list)
        }
    }

    /**
     * Method used to create the collapsed edge using the application's edge type
     * @param target (Vertex) Target of the edge
     * @param source (Vertex) Source of the edge
     * @param influence (String) Edge's influence value and type (i.e. "+9 damage")
     * @return 
     */
    public Edge CollapsedEdgeType(Object target, Object source, String influence) {
        return new Edge(target, source, influence);
    }
    
    /**
     * Method to apply filters after an operation
     * @param variables
     * @param filter 
     */
    public void AddFilters(Variables variables, br.uff.ic.Prov_Viewer.Filters filter) {
        GraphFrame.FilterNodeAgentButton.setSelected(false);
        GraphFrame.FilterNodeLonelyButton.setSelected(false);
        GraphFrame.FilterEdgeQualityButton.setSelected(true);
        GraphFrame.FilterEdgeValButton.setSelected(true);
        GraphFrame.FilterEdgeProgressButton.setSelected(true);
        GraphFrame.FilterEdgeCreditsButton.setSelected(true);
        GraphFrame.FilterEdgeNeutralButton.setSelected(true);
        GraphFrame.FilterEdgeAidButton.setSelected(true);
        GraphFrame.FilterEdgeDiscoveryButton.setSelected(true);
        GraphFrame.FilterEdgeRepairButton.setSelected(true);
        GraphFrame.FilterEdgeBugsButton.setSelected(true);
        GraphFrame.FilterEdgeTCButton.setSelected(true);
        GraphFrame.FilterEdgePrototypeButton.setSelected(true);
        GraphFrame.FilterEdgeNegotiationButton.setSelected(true);
        Filters(variables, filter, false);
    }
    
    /**
     * Method to remove filters before an operation, avoiding the loss of information
     * @param variables
     * @param filter 
     */
    public void RemoveFilters(Variables variables, br.uff.ic.Prov_Viewer.Filters filter) {
        GraphFrame.FilterNodeAgentButton.setSelected(false);
        GraphFrame.FilterNodeLonelyButton.setSelected(false);
        GraphFrame.FilterEdgeQualityButton.setSelected(false);
        GraphFrame.FilterEdgeValButton.setSelected(false);
        GraphFrame.FilterEdgeProgressButton.setSelected(false);
        GraphFrame.FilterEdgeCreditsButton.setSelected(false);
        GraphFrame.FilterEdgeNeutralButton.setSelected(true);
        GraphFrame.FilterEdgeAidButton.setSelected(false);
        GraphFrame.FilterEdgeDiscoveryButton.setSelected(false);
        GraphFrame.FilterEdgeRepairButton.setSelected(false);
        GraphFrame.FilterEdgeBugsButton.setSelected(false);
        GraphFrame.FilterEdgeTCButton.setSelected(false);
        GraphFrame.FilterEdgePrototypeButton.setSelected(false);
        GraphFrame.FilterEdgeNegotiationButton.setSelected(false);
        Filters(variables, filter);
    }
    
}
