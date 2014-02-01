/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.Variables;
import br.uff.ic.Prov_Viewer.Vertex.ActivityVertex;
import br.uff.ic.Prov_Viewer.Vertex.AgentVertex;
import br.uff.ic.Prov_Viewer.Vertex.EntityVertex;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
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
                }
                return new Color(0,0,0);
            }
        };
        view.getRenderContext().setVertexFillPaintTransformer(vertexPainter);
    }
    public static void VertexPainter(VisualizationViewer<Object, Edge> view, final Variables variables)
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
                    if(((Variables)variables).showMode1)
                    {
                        return VertexPainterModes.Mode1(v, variables);
                    }
                    if(((Variables)variables).showMode2)
                    {
                        return VertexPainterModes.Mode2(v, variables);
                    }
                    if(((Variables)variables).showMode3)
                    {
                        return VertexPainterModes.Mode3(v, variables);
                    }
                    if(((Variables)variables).showMode4)
                    {
                        return VertexPainterModes.Mode4(v, variables);
                    }
                    if(((Variables)variables).showMode5)
                    {
                        return VertexPainterModes.Mode5(v, variables);
                    }
                    if(((Variables)variables).showMode6)
                    {
                        return VertexPainterModes.Mode6(v, variables);
                    }
                    if((v instanceof AgentVertex) || (v instanceof ActivityVertex) || (v instanceof EntityVertex)) 
                    {
                         return ((Vertex)v).getColor();
                    }
                }
                return new Color(0,0,0);
            }
        };
        view.getRenderContext().setVertexFillPaintTransformer(vertexPainter);
    }
}
