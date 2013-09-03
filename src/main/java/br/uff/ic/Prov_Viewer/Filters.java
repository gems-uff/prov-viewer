/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.Filter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import org.apache.commons.collections15.Predicate;

/**
 * Abstract class to filter information
 * @author Kohwalter
 */
public abstract class Filters {
    /**
     * Variable FilteredGraph
     */
    public DirectedGraph<Object,Edge> filteredGraph;
    /**
     * Variable EdgeFilter
     */
    public Filter<Object, Edge> EdgeFilter;
    /**
     * Variable VertexFilter
     */
    public Filter<Object, Edge> VertexFilter;
        
    /**
     * Initialize filters
     */
    public void FilterInit()
    {
        //All vertices are visiable
        VertexFilter = new VertexPredicateFilter<Object, Edge>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                return true;
            }
        });
        //All edges are visible
        EdgeFilter = new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
                return true;
            }
        });
    }
    
    /**
     * Method to use filters (Vertex and Edge)
     * @param view VisualizationViewer<Object, Edge> view
     * @param layout Layout<Object, Edge> layout
     * @param collapsedGraph DirectedGraph<Object,Edge> collapsedGraph
     * @param hiddenEdges Boolean (filter original edges that composes a collapsed one or not?)
     */
    public void Filter(VisualizationViewer<Object, Edge> view, 
            Layout<Object, Edge> layout, 
            DirectedGraph<Object,Edge> collapsedGraph,
            boolean hiddenEdges)
    {
        filteredGraph = collapsedGraph;
        
        EdgeFilter = FilterEdges(hiddenEdges);
        VertexFilter = FilterVertex();
        
        filteredGraph = (DirectedGraph<Object, Edge>)EdgeFilter.transform(filteredGraph);
        filteredGraph = (DirectedGraph<Object, Edge>)VertexFilter.transform(filteredGraph);
        layout.setGraph(filteredGraph);
        view.repaint();
    }
    
    /**
     * Method for filtering edges
     * @param hiddenEdges Boolean (consider hidden edges or not?)
     * @return new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>()
     */
    public abstract Filter<Object, Edge> FilterEdges(final boolean hiddenEdges);
    
    /**
     * Method for filtering vertices
     * @return new VertexPredicateFilter<Object, Edge>(new Predicate<Object>()
     */
    public abstract Filter<Object, Edge> FilterVertex();
}
