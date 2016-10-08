/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Filter;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.algorithms.filters.EdgePredicateFilter;
import edu.uci.ics.jung.algorithms.filters.Filter;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
//    public void FilterInit() {
    public void Filters() {
        //All vertices are visiable
        VertexFilter = new VertexPredicateFilter<>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                return true;
            }
        });
        //All edges are visible
        EdgeFilter = new EdgePredicateFilter<>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
                return true;
            }
        });
    }
    
    /**
     * Method for filtering the Graph
     *
     * @param variables Variables type
     * @param hiddenEdges Boolean used to decide if hidden (original edges that
     * composes the collapsed edge) edges will be filtered
     */
    public void Filters(Variables variables, boolean hiddenEdges) {
        variables.filter.filterVerticesAndEdges(variables.view,
                variables.layout,
                variables.collapsedGraph,
                hiddenEdges);
    }

    /**
     * Overload of the Filters method. hiddenEdges is always set as true,
     * filtering the edges that composes the collapsed one
     *
     * @param variables Variables type
     */
    public void Filters(Variables variables) {
        Filters(variables, true);
    }
    
    /**
     * Method to apply filters after an operation
     *
     * @param variables
     */
    public void AddFilters(Variables variables) {
        GraphFrame.FilterList.setSelectionInterval(0, variables.config.edgetype.size() - 1);
        Filters(variables, false);
    }

    /**
     * Method to remove filters before an operation, avoiding the loss of
     * information
     *
     * @param variables
     */
    public void RemoveFilters(Variables variables) {
        GraphFrame.FilterList.setSelectedIndex(0);
        GraphFrame.FilterEdgeAgentButton.setSelected(false);
        GraphFrame.FilterNodeAgentButton.setSelected(false);
        GraphFrame.FilterNodeEntityButton.setSelected(false);
        GraphFrame.FilterNodeLonelyButton.setSelected(false);
        GraphFrame.TemporalFilterToggle.setSelected(false);
        Filters(variables);
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
    public void filterVerticesAndEdges(VisualizationViewer<Object, Edge> view,
            Layout<Object, Edge> layout,
            DirectedGraph<Object, Edge> collapsedGraph,
            boolean hiddenEdges) {
        filteredGraph = collapsedGraph;

        EdgeFilter = filterEdges(hiddenEdges);
        VertexFilter = filterVertex();

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
    private Filter<Object, Edge> filterEdges(final boolean hiddenEdges) {
        Filter<Object, Edge> filterEdge = new EdgePredicateFilter<>(new Predicate<Edge>() {
            @Override
            public boolean evaluate(Edge edge) {
                if (hiddenEdges) {
                    if (edge.isHidden()) {
                        return false;
                    }
                }
                if (edgeTypeFilter(edge)) {
                    return false;
                }

                return !edgeAgentFilter(edge);
            }
        });
        return filterEdge;
    }

    /**
     * Edge filter to show only edges from the selected type or label
     * @param edge
     * @return if the edge will be hidden
     */
    private boolean edgeTypeFilter(Edge edge) {
        List filtersL = GraphFrame.FilterList.getSelectedValuesList();
        for (Object filtersL1 : filtersL) {
            String filter = (String) filtersL1;
            if (filter.equalsIgnoreCase("All Edges")) {
                return false;
            }
            if (edge.getLabel().contains(filter) || edge.getType().contains(filter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method to filter edges that connect to agents
     *
     * @param edge
     * @return if the edge will be hidden
     */
    private boolean edgeAgentFilter(Edge edge) {
        if (GraphFrame.FilterEdgeAgentButton.isSelected()) {
            if (filteredGraph.getDest(edge) instanceof AgentVertex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vertex filter method
     *
     * @return if the vertex will be hidden
     */
    private Filter<Object, Edge> filterVertex() {

        Filter<Object, Edge> filterVertex = new VertexPredicateFilter<>(new Predicate<Object>() {
            @Override
            public boolean evaluate(Object vertex) {
                if (vertexTypeFilter(vertex)) {
                    return false;
                }
                if (vertexLonelyFilter(vertex)) {
                    return false;
                }
                return !vertexTemporalFilter(vertex);
            }
        });
        return filterVertex;
    }

    /**
     * Vertex filter for filtering lonely vertices (vertices without edges)
     *
     * @param vertex
     * @return if the vertex will be hidden
     */
    private boolean vertexLonelyFilter(Object vertex) {
        if (GraphFrame.FilterNodeLonelyButton.isSelected()) {
            final Graph test = filteredGraph;
            if (test.getNeighborCount(vertex) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vertex filter to filter vertices of the selected type
     *
     * @param vertex
     * @return if the vertex will be hidden
     */
    private boolean vertexTypeFilter(Object vertex) {
        if (GraphFrame.FilterNodeAgentButton.isSelected()) {
            if (vertex instanceof AgentVertex) {
                return true;
            }
        }
        if (GraphFrame.FilterNodeEntityButton.isSelected()) {
            if (vertex instanceof EntityVertex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vertex filter to filter vertices that is outside the desired temporal
     * range
     *
     * @param vertex
     * @return if the vertex will be hidden
     */
    private boolean vertexTemporalFilter(Object vertex) {
        if (GraphFrame.TemporalFilterToggle.isSelected()) {
            if (!(vertex instanceof AgentVertex)) {
                while (vertex instanceof Graph) {
                    vertex = ((Graph) vertex).getVertices().toArray()[0];
                }
                double timeDate = ((Vertex) vertex).getTime();
                double time = ((Vertex) vertex).getNormalizedTime();

                if (GraphFrame.temporalDaysButton.isSelected()) {
                    time = TimeUnit.MILLISECONDS.toDays((long) time);
                } else if (GraphFrame.temporalWeeksButton.isSelected()) {
                    time = (int) TimeUnit.MILLISECONDS.toDays((long) time) / 7;
                } else if (GraphFrame.temporalHoursButton.isSelected()) {
                    time = TimeUnit.MILLISECONDS.toHours((long) time);
                } else if (GraphFrame.temporalMinutesButton.isSelected()) {
                    time = TimeUnit.MILLISECONDS.toMinutes((long) time);
                }

                if (Utils.tryParseDouble(GraphFrame.FilterVertexMinValue.getText())) {
                    double minTime = Float.parseFloat(GraphFrame.FilterVertexMinValue.getText());
                    if (time < minTime) {
                        return true;
                    }
                } else if (Utils.tryParseDate(GraphFrame.FilterVertexMinValue.getText())) {
                    double minTime = Utils.convertStringDateToDouble(GraphFrame.FilterVertexMinValue.getText());
                    if (timeDate < minTime) {
                        return true;
                    }
                }
                if (Utils.tryParseDouble(GraphFrame.FilterVertexMaxValue.getText())) {
                    double maxTime;
                    maxTime = Float.parseFloat(GraphFrame.FilterVertexMaxValue.getText());
                    if (time > maxTime) {
                        return true;
                    }
                } else if (Utils.tryParseDate(GraphFrame.FilterVertexMaxValue.getText())) {
                    double maxTime = Utils.convertStringDateToDouble(GraphFrame.FilterVertexMaxValue.getText());
                    if (timeDate > maxTime) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
