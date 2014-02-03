/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.Input.Config;
import br.uff.ic.Prov_Viewer.Variables;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class WeekendMode extends VertexPaintMode{
    public String sat;
    public String sun;

    public WeekendMode(String attribute, String sat, String sun) {
        super(attribute);
        this.sat = sat;
        this.sun = sun;
    }
    
    public WeekendMode(String attribute, String sat, String sun, double g, double y)
    {
        super(attribute, g, y);
        this.sat = sat;
        this.sun = sun;
    }
    
    @Override
    public Paint Execute(Object v, final Variables variables) {
        String day = ((Vertex) v).getDayName();
        if (day.equalsIgnoreCase(sat) || day.equalsIgnoreCase(sun)) {
            return new Color(255, 0, 0);
        }
        return ((Vertex) v).getColor();
    }

    @Override
    public Paint CompareValue(int value, float constant) {
        if(value > this.valueGreenThreshold) {
            return new Color(0,255,0);
        }
        else
        {
            if(value > this.valueYellowThreshold) {
                return new Color(255,255,0);
            }
            else {
                return new Color(255,0,0);
            }
        }
    }
    
}
