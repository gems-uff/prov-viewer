/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.Input.Config;
import br.uff.ic.Prov_Viewer.Vertex.EntityVertex;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.VertexPaintMode;
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
    /**
     * Return the added value between 2 values
     * @param type (String) Edge type to be analyzed
     * @param value (Float[]) Current value
     * @param edge (Edge) Second value is extracted from this edge, if the influence type is the same
     * @return added value between value and edge.getValue
     */
    public float[] Add(String type, float[] value, Edge edge)
    {
        if(edge.getInfluence().contains(type))
        {
            value[0] += Math.abs(edge.getValue());
            value[1]++;
        }
        return value;
    }
    
    /**
     * Return the average value
     * @param type (String) Edge type to be analyzed
     * @param value (Float[]) Current value and counter
     * @param edge (Edge) Second value is extracted from this edge, if the influence type is the same
     * @param average (Boolean) To compute the average or not
     * @return average value between value and edge.getValue
     */
    public float[] Average(float[] value, boolean average)
    {
        if(!average)
        {
            value[0] = value[0] / value[1];
        }
        return value;
    }
    
    /**
     * Return the new value
     * @param type (String) Edge type to be analyzed
     * @param value (Float[]) Current value and counter
     * @param edge (Edge) Second value is extracted from this edge, if the influence type is the same
     * @param max (Boolean) To compute the max between values or to add values
     * @return value, according to the operation, between value and edge.getValue
     */
    public float[] ComputeValue(float[] value, String type, Edge edge, boolean max) 
    {
        if (max) {
            value[0] = CompareMax(type, value[0], edge);
        } else {
            value = Add(type, value, edge);
        }
        return value;
    }
    public TwoFloat[] edgeArray = new TwoFloat[99];
    public float[] entityValue = new float[]{0,0};
    
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
        for(int i = 0; i < 99 ; i++)
        {
            edgeArray[i] = new TwoFloat();
        }
        Collection<Edge> edges = graph.getEdges();
        for (Edge edge : edges)
        {
            for(int i = 0; i < Config.length; i++)
            {
                GraphFrame.FilterList.setSelectedIndex(i);
                edgeArray[i].value = ComputeValue(edgeArray[i].value, GraphFrame.FilterList.getSelectedValue().toString(), edge, Config.EdgeStroke[i]);
            }
        }
        for(int i = 0; i < Config.length; i++)
        {
//            GraphFrame.FilterList.setSelectedIndex(i);
            edgeArray[i].value = Average(edgeArray[i].value, Config.EdgeStroke[i]);
        }
        //
        //For vertex painter mode 4
        //
        Collection<Object> nodes = graph.getVertices();
        
        for (Object node : nodes)
        {
            if(node instanceof EntityVertex)
            {
                if(Config.VPMattTypeMax) {
                    entityValue[0] = Math.max(entityValue[0], Math.abs(((EntityVertex)node).getAttributeValueInteger(Config.VPMattType)));
                }
                else {
                    entityValue[0] += Math.abs(((EntityVertex)node).getAttributeValueInteger(Config.VPMattType));
                    entityValue[1]++;
                }
            }
        }
        if(!Config.VPMattTypeMax)
        {
            entityValue[0] = entityValue[0] / entityValue[1];
        }
        GraphFrame.FilterList.setSelectedIndex(0);
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
