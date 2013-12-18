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
    
    public float[] edge01 = new float[]{0,0};
    public float[] edge02 = new float[]{0,0};
    public float[] edge03 = new float[]{0,0};
    public float[] edge04 = new float[]{0,0};
    public float[] edge05 = new float[]{0,0};
    public float[] edge06 = new float[]{0,0};
    public float[] edge07 = new float[]{0,0};
    public float[] edge08 = new float[]{0,0};
    public float[] edge09 = new float[]{0,0};
    public float[] edge10 = new float[]{0,0};
    public float[] edge11 = new float[]{0,0};
    public float[] edge12 = new float[]{0,0};
    public float[] edge13 = new float[]{0,0};
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
        Collection<Edge> edges = graph.getEdges();
        for (Edge edge : edges)
        {
            edge01 = ComputeValue(edge01, GraphFrame.FilterEdge01.getText(), edge, Config.ES01);
            edge02 = ComputeValue(edge02, GraphFrame.FilterEdge02.getText(), edge, Config.ES02);
            edge03 = ComputeValue(edge03, GraphFrame.FilterEdge03.getText(), edge, Config.ES03);
            edge04 = ComputeValue(edge04, GraphFrame.FilterEdge04.getText(), edge, Config.ES04);
            edge05 = ComputeValue(edge05, GraphFrame.FilterEdge05.getText(), edge, Config.ES05);
            edge06 = ComputeValue(edge06, GraphFrame.FilterEdge06.getText(), edge, Config.ES06);
            edge07 = ComputeValue(edge07, GraphFrame.FilterEdge07.getText(), edge, Config.ES07);
            edge08 = ComputeValue(edge08, GraphFrame.FilterEdge08.getText(), edge, Config.ES08);
            edge09 = ComputeValue(edge09, GraphFrame.FilterEdge09.getText(), edge, Config.ES09);
            edge10 = ComputeValue(edge10, GraphFrame.FilterEdge10.getText(), edge, Config.ES10);
            edge11 = ComputeValue(edge11, GraphFrame.FilterEdge11.getText(), edge, Config.ES11);
            edge12 = ComputeValue(edge12, GraphFrame.FilterEdge12.getText(), edge, Config.ES12);
            edge13 = ComputeValue(edge13, GraphFrame.FilterEdge13.getText(), edge, Config.ES13);
            
//            edge02[0] = CompareMax(GraphFrame.FilterEdge02.getText(), edge02[0], edge);
//            edge03[0] = CompareMax(GraphFrame.FilterEdge03.getText(), edge03[0], edge);
//            edge04[0] = CompareMax(GraphFrame.FilterEdge04.getText(), edge04[0], edge);
//            edge05[0] = CompareMax(GraphFrame.FilterEdge05.getText(), edge05[0], edge);
//            edge06[0] = CompareMax(GraphFrame.FilterEdge06.getText(), edge06[0], edge);
//            edge07[0] = CompareMax(GraphFrame.FilterEdge07.getText(), edge07[0], edge);
//            edge08[0] = CompareMax(GraphFrame.FilterEdge08.getText(), edge08[0], edge);
//            edge09[0] = CompareMax(GraphFrame.FilterEdge09.getText(), edge09[0], edge);
//            edge10[0] = CompareMax(GraphFrame.FilterEdge10.getText(), edge10[0], edge);
//            edge11[0] = CompareMax(GraphFrame.FilterEdge11.getText(), edge11[0], edge);
//            edge12[0] = CompareMax(GraphFrame.FilterEdge12.getText(), edge12[0], edge);
//            edge13[0] = CompareMax(GraphFrame.FilterEdge13.getText(), edge13[0], edge);
        }
        edge01 = Average(edge01, Config.ES01);
        edge02 = Average(edge02, Config.ES02);
        edge03 = Average(edge03, Config.ES03);
        edge04 = Average(edge04, Config.ES04);
        edge05 = Average(edge05, Config.ES05);
        edge06 = Average(edge06, Config.ES06);
        edge07 = Average(edge07, Config.ES07);
        edge08 = Average(edge08, Config.ES08);
        edge09 = Average(edge09, Config.ES09);
        edge10 = Average(edge10, Config.ES10);
        edge11 = Average(edge11, Config.ES11);
        edge12 = Average(edge12, Config.ES12);
        edge13 = Average(edge13, Config.ES13);
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
