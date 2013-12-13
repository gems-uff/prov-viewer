/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import java.util.Collection;

/**
 * Class containing graph related variables (View, Layout, GraphCollapser, Graph, CollapsedGraph)
 * @author Kohwalter
 */
public class Variables extends Object{
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
    
    public float credits, quality, aid, progress, validation, discovery, bugs, repair, tc, funds, morale, stamina, negotiation, prototype;
    public boolean showMode1 = false;
    public boolean showMode2 = false;
    public boolean showMode3 = false;
    public boolean showMode4 = false;
    public boolean showMode5 = false;
    public boolean showMode6 = false;
    
    /**
     * Method to set the Attribute Status (vertex-based) visualization Mode
     * @param mode String (I.e. Default, Stamina, Morale)
     */
    public void SetMode(String mode)
    {
        if (mode.equalsIgnoreCase((String) GraphFrame.StatusFilterBox.getItemAt(0))) {
            SetDefault();
        }
        if (mode.equalsIgnoreCase((String) GraphFrame.StatusFilterBox.getItemAt(1))) {
            SetMode1();
        }
        if (mode.equalsIgnoreCase((String) GraphFrame.StatusFilterBox.getItemAt(2))) {
            SetMode2();
        }
        if (mode.equalsIgnoreCase((String) GraphFrame.StatusFilterBox.getItemAt(3))) {
            SetMode3();
        }
        if (mode.equalsIgnoreCase((String) GraphFrame.StatusFilterBox.getItemAt(4))) {
            SetMode4();
        }
        if (mode.equalsIgnoreCase((String) GraphFrame.StatusFilterBox.getItemAt(5))) {
            SetMode5();
        }
        if (mode.equalsIgnoreCase((String) GraphFrame.StatusFilterBox.getItemAt(6))) {
            SetMode6();
        }
    }
    /**
     * Set the graph to the default attribute status visualization mode
     */
    public void SetDefault()
    {
        showMode1 = false;
        showMode2 = false;
        showMode3 = false;
        showMode4 = false;
        showMode5 = false;
        showMode6 = false;
    }
    /**
     * Find max values for each edge type. Used for edge width.
     * @param graph Graph
     */
    public void FindMax(DirectedGraph<Object,Edge> graph)
    {
        credits = quality = aid = progress = validation = discovery = bugs = 
                repair = tc = funds = morale = stamina = prototype = negotiation = 0;
        int cCredits = 0;
        
        Collection<Edge> edges = graph.getEdges();
        for (Edge edge : edges)
        {
            quality = CompareMax(GraphFrame.FilterEdgeQualityButton.getText(), quality, edge);
            aid = CompareMax(GraphFrame.FilterEdgeAidButton.getText(), aid, edge);
            progress = CompareMax(GraphFrame.FilterEdgeProgressButton.getText(), progress, edge);
            validation = CompareMax(GraphFrame.FilterEdgeValButton.getText(), validation, edge);
            discovery = CompareMax(GraphFrame.FilterEdgeDiscoveryButton.getText(), discovery, edge);
            bugs = CompareMax(GraphFrame.FilterEdgeBugsButton.getText(), bugs, edge);
            repair = CompareMax(GraphFrame.FilterEdgeRepairButton.getText(), repair, edge);
            tc = CompareMax(GraphFrame.FilterEdgeTCButton.getText(), tc, edge);
            morale = CompareMax(GraphFrame.FilterEdgeMoraleButton.getText(), morale, edge);
            stamina = CompareMax(GraphFrame.FilterEdgeStaminaButton.getText(), stamina, edge);
            negotiation = CompareMax(GraphFrame.FilterEdgeNegotiationButton.getText(), negotiation, edge);
            prototype = CompareMax(GraphFrame.FilterEdgePrototypeButton.getText(), prototype, edge);
            if(edge.getInfluence().contains(GraphFrame.FilterEdgeCreditsButton.getText()))
            {
                credits += Math.abs(edge.getValue());
                cCredits ++;
            }
        }
        credits = credits / cCredits;
        Collection<Object> nodes = graph.getVertices();
        for (Object node : nodes)
        {
            if(node instanceof EntityVertex)
            {
                funds = Math.max(funds, Math.abs(((EntityVertex)node).getAttributeValueInteger("Credits")));
            }
        }
    }
    /**
     * Method to set mode 1
     */
    public void SetMode1()
    {
        SetDefault();
        showMode1 = true;
    }
    /**
     * Method to set mode 2
     */
    public void SetMode2()
    {
        SetDefault();
        showMode2 = true;
    }
    /**
     * Method to set mode 3
     */
    public void SetMode3()
    {
        SetDefault();
        showMode3 = true;
    }
    /**
     * Method to set mode 4
     */
    public void SetMode4()
    {
        SetDefault();
        showMode4 = true;
    }
    /**
     * Method to set mode 5
     */
    public void SetMode5()
    {
        SetDefault();
        showMode5 = true;
    }
    /**
     * Method to set mode 6
     */
    public void SetMode6()
    {
        SetDefault();
        showMode6 = true;
    }
}
