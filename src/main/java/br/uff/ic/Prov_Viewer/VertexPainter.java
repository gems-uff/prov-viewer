/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;

/**
 * Class to paint vertex according to its type (subclass). It also paints collapsed vertices (instance of graphs)
 * @author Kohwalter
 */
public class VertexPainter {

    /**
     * Method to compute each Vertex Paint from the graph
     * @param view VisualizationViewer<Object, Edge> view
     */
    public static void VertexPainter(VisualizationViewer<Object, Edge> view)
    {
        Transformer vertexPainter = new Transformer<Object,Paint>() {

            @Override
            public Paint transform(Object v) 
            {
                if(v instanceof Graph) 
                {
                    String text = ((Graph)v).toString();
                    if(text.contains("Agent")) {
                        return new Color(119,136,153);
                    }
                    else if(text.contains("Entity")) {
                        return new Color(255,222,173);
                    }
                    else if(text.contains("Activity")) {
                        return new Color(190,190,190);
                    }
                    else {
                        return new Color(150,150,150);
                    }
                }
                else
                {
                    if((v instanceof AgentVertex) || (v instanceof ActivityVertex) || (v instanceof EntityVertex)) 
                    {
                         return ((Vertex)v).getColor();
                    }
//                    else if(v instanceof AgentVertex)
//                    {
//                        if(filterON)  
//                        {
//                            return VertexFilterPainter.GetValueColor((((AgentVertex)v).getValue()), constant);
//                        }
//                        return ((AgentVertex)v).getColor();
//                    }
//                    else if(v instanceof ActivityVertex)
//                    {
//                        if(filterON)  
//                        {
//                            return VertexFilterPainter.GetValueColor((((ActivityVertex)v).getValue()), constant);
//                        }
//                        return ((ActivityVertex)v).getColor();
//                    }
//                    else if(v instanceof EntityVertex)
//                    {
//                        if(filterON)  
//                        {
//                            return VertexFilterPainter.GetValueColor((((EntityVertex)v).getValue()), constant);
//                        }
//                        return ((EntityVertex)v).getColor();
//                    }
                }
                return new Color(0,0,0);
            }
        };
        view.getRenderContext().setVertexFillPaintTransformer(vertexPainter);
    }
}
