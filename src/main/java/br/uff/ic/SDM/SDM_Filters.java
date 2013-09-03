/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Edge;
import br.uff.ic.Prov_Viewer.Filters;
import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.Filter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Predicate;

/**
 * Subclass of Filters
 * @author Kohwalter
 */
public class SDM_Filters extends Filters{

    /**
     * ================================================
     * Initialize Filters (Vertex and Edge)
     * ================================================
     */
//    public void FilterInit()
//    {
//        VertexFilter = new VertexPredicateFilter<Object, Edge>(new Predicate<Object>() {
//            @Override
//            public boolean evaluate(Object vertex) {
//                return true;
//            }
//        });
//        EdgeFilter = new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>() {
//            @Override
//            public boolean evaluate(Edge edge) {
//                return true;
//            }
//        });
//    } 
    
    /**
     * ================================================
     * Edge Filter
     * ================================================
     */
    @Override
    public Filter<Object, Edge> FilterEdges(final boolean hiddenEdges)
    {
        Filter<Object, Edge> filterEdge = new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
//                String[] line = edge.toString().split(" ");
                if(!GraphFrame.FilterEdgeCreditsButton.isSelected())
                {
                    if(edge.getInfluence().contains("Credits")) {
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
                    if(edge.getInfluence().contains("Quality")) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeAidButton.isSelected())
                {
                    if((edge.getInfluence().contains("Aid")) || (edge.getInfluence().contains("Negotiation"))){
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeProgressButton.isSelected())
                {
                    if(edge.getInfluence().contains("Progress")) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeValButton.isSelected())
                {
                    if(edge.getInfluence().contains("Validation")) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeDiscoveryButton.isSelected())
                {
                    if((edge.getInfluence().contains("Discovery")) || (edge.getInfluence().contains("Prototype"))) {
                        return false;
                    }
                }
                if(!GraphFrame.FilterEdgeRepairButton.isSelected())
                {
                    if(edge.getInfluence().contains("Repair")) {
                        return false;
                    }
                }
                if(!GraphFrame.EdgeFilterBugsButton.isSelected())
                {
                    if(edge.getInfluence().contains("Bug")) {
                        return false;
                    }
                }
                if(!GraphFrame.EdgeFilterTCButton.isSelected())
                {
                    if(edge.getInfluence().contains("TC")) {
                        return false;
                    }
                }
                if(!GraphFrame.EdgeFilterMoraleButton.isSelected())
                {
                    if(edge.getInfluence().contains("Morale")) {
                        return false;
                    }
                }
                if(!GraphFrame.EdgeFilterStaminaButton.isSelected())
                {
                    if(edge.getInfluence().contains("Stamina")) {
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
     * ================================================
     * Vertex Filter
     * ================================================
     */
    @Override
    public Filter<Object, Edge> FilterVertex()
    {
        Filter<Object, Edge> filterVertex = new VertexPredicateFilter<Object, Edge>(new Predicate<Object>() {
        @Override
        public boolean evaluate(Object vertex) {
            final Graph test = filteredGraph;
            if(GraphFrame.FilterNodeAgentButton.isSelected())
            {
                if((vertex instanceof SDM_AgentVertex) || (vertex instanceof SDM_ClientVertex)) {
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
