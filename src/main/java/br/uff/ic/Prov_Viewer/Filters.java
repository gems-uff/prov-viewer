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
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import org.apache.commons.collections15.Predicate;

/**
 * Class to filter information
 * @author Kohwalter
 */
public class Filters {
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
    public Filter<Object, Edge> FilterEdges(final boolean hiddenEdges)
    {
        Filter<Object, Edge> filterEdge = new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
//                String[] line = edge.toString().split(" ");
                if(!GraphFrame.FilterEdgeCreditsButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeCreditsButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeNeutralButton.isSelected())
                {
                    if(edge.isNeutral()) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeQualityButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeQualityButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeAidButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeAidButton.getText())){
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeProgressButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeProgressButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeValButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeValButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeDiscoveryButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeDiscoveryButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeRepairButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeRepairButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeBugsButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeBugsButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeTCButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeTCButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeMoraleButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeMoraleButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeStaminaButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeStaminaButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgePrototypeButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgePrototypeButton.getText())) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeNegotiationButton.isSelected())
                {
                    if(edge.getInfluence().contains(GraphFrame.FilterEdgeNegotiationButton.getText())) {
                        return false;
                    }
                }
                if(hiddenEdges)
                {
                    if(edge.isHidden()) 
                    {
                        return false;
                    }
                }
                return true;
            }
        });
        return filterEdge;
    }
    
    /**
     * Method for filtering vertices
     * @return new VertexPredicateFilter<Object, Edge>(new Predicate<Object>()
     */
    public Filter<Object, Edge> FilterVertex()
    {
        Filter<Object, Edge> filterVertex = new VertexPredicateFilter<Object, Edge>(new Predicate<Object>() {
        @Override
        public boolean evaluate(Object vertex) {
            final Graph test = filteredGraph;
            if(GraphFrame.FilterNodeAgentButton.isSelected())
            {
                if(vertex instanceof AgentVertex) {
                    return false;
                }
            }
            if(GraphFrame.FilterNodeLonelyButton.isSelected())
            {
                if(test.getNeighborCount(vertex) == 0) {
                    return false;
                }
            }
            return true;
        }
        });
        return filterVertex;
    }
}
