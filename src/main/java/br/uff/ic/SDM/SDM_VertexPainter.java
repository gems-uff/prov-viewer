/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Edge;
import br.uff.ic.Prov_Viewer.Variables;
import br.uff.ic.Prov_Viewer.Vertex;
import br.uff.ic.Prov_Viewer.VertexFilterPainter;
import br.uff.ic.Prov_Viewer.VertexPainter;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Color;
import java.awt.Paint;
import org.apache.commons.collections15.Transformer;

/**
 * Subclass of VertexPainter
 * @author Kohwalter
 */
public class SDM_VertexPainter extends VertexPainter{

    static VertexFilterPainter vertexDefaultFilter = new VertexFilterPainter(75, 40, 6, 12, 0.7, 0.3);
    
    /**
     * Compute Vertex Paint
     * @param view
     * @param variables 
     */
    public static void VertexPainter(VisualizationViewer<Object, Edge> view, final Variables variables)
    {
        Transformer vertexPainter = new Transformer<Object,Paint>() {

            @Override
            public Paint transform(Object v) {
                if(v instanceof Graph) 
                {
                    String text = ((Graph)v).toString();
                    if(text.contains("Project")) {
                        return new Color(255,222,173);
                    }
                    else if(text.contains("Agent")) {
                        return new Color(119,136,153);
                    }
                    else if(text.contains("Process")) 
                    {
                        if(text.contains("Hired")) 
                        {
                            return new Color(139,136,120);
                        }
                        else 
                        {
                            return new Color(190,190,190);
                        }
                    }
                    else {
                        return new Color(150,150,150);
                    }
                }
                else
                {
                    if((v instanceof SDM_AgentVertex) || (v instanceof SDM_ClientVertex) || (v instanceof SDM_ArtifactVertex)) {
                         return ((Vertex)v).getColor();
                    }
                    if(((SDM_Variables)variables).showWeekend)
                    {
                        String day = ((Vertex)v).getDayName();
                        if(day.equalsIgnoreCase("Sat") || day.equalsIgnoreCase("Sun"))
                        {
                            return new Color(255,0,0);
                        }
                    }
                    if(v instanceof SDM_ProjectVertex)
                    {
                        if(((SDM_Variables)variables).showCredits)  
                        {
                            return vertexDefaultFilter.ConstantCompareColor((((SDM_ProjectVertex)v).getCredits()), ((SDM_Variables)variables).funds);
                        }
                        return ((SDM_ProjectVertex)v).getColor();
                    }
                    
                    if(v instanceof SDM_ProcessVertex)     
                    {
                        if(((SDM_Variables)variables).showMorale)
                        {
                            if(((SDM_ProcessVertex)v).getTask().equalsIgnoreCase("Fired")) {
                                return ((SDM_ProcessVertex)v).getFiredColor();
                            }
                            return vertexDefaultFilter.ValueCompareColor(((SDM_ProcessVertex)v).getMorale());
                            
                        }
                        else if(((SDM_Variables)variables).showStamina)
                        {
                            if(((SDM_ProcessVertex)v).getTask().equalsIgnoreCase("Fired")) {
                                return ((SDM_ProcessVertex)v).getFiredColor();
                            }
                            return vertexDefaultFilter.ValueCompareColor(((SDM_ProcessVertex)v).getStamina());
                        }
                        else if(((SDM_Variables)variables).showHours)
                        {
                            return vertexDefaultFilter.InvertedValueCompareColor(((SDM_ProcessVertex)v).getHours());
                        }
                        else if(((SDM_Variables)variables).showRole)
                        {
                            return GetRoleColor(((SDM_ProcessVertex)v).getRole());
                        }
                        else if(((SDM_Variables)variables).showCredits)
                        {
                            return new Color(190,190,190);
                        }
                        return ((SDM_ProcessVertex)v).getColor();
                    }
                }
                return new Color(0,0,0);
            }
        };
        view.getRenderContext().setVertexFillPaintTransformer(vertexPainter);
    }
    
    /**
     * SDM custom colors for roles
     * @param value
     * @return 
     */
    public static Paint GetRoleColor(String value)
    {
        if(value.equalsIgnoreCase("Analyst")) {
//            return new Color(255,153,51);
            return new Color(0,0,204);
        }
        else if(value.equalsIgnoreCase("Architect")) 
        {
//            return new Color(255,255,51);
            return new Color(102,255,255);
        }
        else if(value.equalsIgnoreCase("Manager")) 
        {
            return new Color(153,255,51);
        }
        else if(value.equalsIgnoreCase("Marketing")) 
        {
//            return new Color(51,255,255);
            return new Color(255,153,51);
        }
        else if(value.equalsIgnoreCase("Programmer")) 
        {
//            return new Color(51,204,255);
            return new Color(0,204,102);
        }
        else if(value.equalsIgnoreCase("Tester")) 
        {
//            return new Color(153,51,255);
            return new Color(204,204,0);
        }
        return new Color(128,128,128);
    }
    
}
