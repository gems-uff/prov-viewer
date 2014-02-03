/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Filter;

import br.uff.ic.Prov_Viewer.Vertex.AgentVertex;
import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.GraphFrame;
import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.Filter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.util.List;
import org.apache.commons.collections15.Predicate;

/**
 * Class to filter information
 *
 * @author Kohwalter
 */
public class Filters {

    /**
     * Variable FilteredGraph
     */
    public DirectedGraph<Object, Edge> filteredGraph;
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
    public void FilterInit() {
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
     *
     * @param view VisualizationViewer<Object, Edge> view
     * @param layout Layout<Object, Edge> layout
     * @param collapsedGraph DirectedGraph<Object,Edge> collapsedGraph
     * @param hiddenEdges Boolean (filter original edges that composes a
     * collapsed one or not?)
     */
    public void Filter(VisualizationViewer<Object, Edge> view,
            Layout<Object, Edge> layout,
            DirectedGraph<Object, Edge> collapsedGraph,
            boolean hiddenEdges) {
        filteredGraph = collapsedGraph;

        EdgeFilter = FilterEdges(hiddenEdges);
        VertexFilter = FilterVertex();

        filteredGraph = (DirectedGraph<Object, Edge>) EdgeFilter.transform(filteredGraph);
        filteredGraph = (DirectedGraph<Object, Edge>) VertexFilter.transform(filteredGraph);
        layout.setGraph(filteredGraph);
        view.repaint();
    }

    /**
     * Method for filtering edges
     *
     * @param hiddenEdges Boolean (consider hidden edges or not?)
     * @return new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>()
     */
    public Filter<Object, Edge> FilterEdges(final boolean hiddenEdges) {
        Filter<Object, Edge> filterEdge = new EdgePredicateFilter<Object, Edge>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
//                String[] line = edge.toString().split(" ");

                if (hiddenEdges) {
                    if (edge.isHidden()) {
                        return false;
                    }
                }

                List filtersL = GraphFrame.FilterList.getSelectedValuesList();

                boolean returnValue = false;
                for (int i = 0; i < filtersL.size(); i++) {
                    String filter = (String) filtersL.get(i);
                    if (edge.getInfluence().contains(filter)) {
                        if (!returnValue) {
                            returnValue = true;
                        }
                    }
                }

                return returnValue;

//                
//                if(!GraphFrame.FilterEdge01.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge01.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdgeNeutral.isSelected())
//                {
//                    if(edge.isNeutral()) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge02.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge02.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge04.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge04.getText())){
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge03.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge03.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge05.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge05.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge06.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge06.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge07.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge07.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge08.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge08.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge09.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge09.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge10.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge10.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge11.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge11.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge12.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge12.getText())) {
//                        return false;
//                    }
//                }
//                if(!GraphFrame.FilterEdge13.isSelected())
//                {
//                    if(edge.getInfluence().contains(GraphFrame.FilterEdge13.getText())) {
//                        return false;
//                    }
//                }
            }
        });
        return filterEdge;
    }

    /**
     * Method for filtering vertices
     *
     * @return new VertexPredicateFilter<Object, Edge>(new Predicate<Object>()
     */
    public Filter<Object, Edge> FilterVertex() {
        Filter<Object, Edge> filterVertex = new VertexPredicateFilter<Object, Edge>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                final Graph test = filteredGraph;
                if (GraphFrame.FilterNodeAgentButton.isSelected()) {
                    if (vertex instanceof AgentVertex) {
                        return false;
                    }
                }
                if (GraphFrame.FilterNodeLonelyButton.isSelected()) {
                    if (test.getNeighborCount(vertex) == 0) {
                        return false;
                    }
                }
                return true;
            }
        });
        return filterVertex;
    }
}
