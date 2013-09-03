/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;

/**
 * Abstract class containing graph related variables (View, Layout, GraphCollapser, Graph, CollapsedGraph)
 * @author Kohwalter
 */
public abstract class Variables extends Object{
    //Vertex filters mode
    /**
     * View Variable
     */
    public VisualizationViewer<Object, Edge> view;
    /**
     * Layout Variable
     */
    public Layout<Object, Edge> layout;
    /**
     * GraphCollapser Variable
     */
    public GraphCollapser gCollapser;
    /**
     * Graph Variable (Static because of the GUI)
     */
    public static DirectedGraph<Object,Edge> graph;
    /**
     * CollapsedGraph Variable
     */
    public DirectedGraph<Object,Edge> collapsedGraph;
    
    /**
     * Method to set the Attribute Status (vertex-based) visualization Mode
     * @param mode String (I.e. Default, Stamina, Morale)
     */
    public abstract void SetMode(String mode);

    /**
     * Set the graph to the default attribute status visualization mode
     */
    public abstract void SetDefault();
    
    /**
     * Find max values for each edge type. Used for edge width.
     * @param graph Graph
     */
    public abstract void FindMax(DirectedGraph<Object,Edge> graph);  
    
    /**
     * Return the max value between 2 values
     * @param type (String) Edge type to be analyzed
     * @param value (Float) Current max value
     * @param edge (Edge) Second value is extracted from this edge, if the influence type is the same
     * @return max value between value and edge.getValue
     */
    public float CompareMax(String type, float value, Edge edge)
    {
        if((edge.getInfluence().contains(type)))
        {
            value = Math.max(value, Math.abs(edge.getValue()));
        }
        return value;
    }
}
