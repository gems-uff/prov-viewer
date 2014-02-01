/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.Input.Config;
import br.uff.ic.Prov_Viewer.Variables;
import br.uff.ic.Prov_Viewer.Vertex.ActivityVertex;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class MultiAttributeMode extends VertexPaintMode{
    String[] values;

    public MultiAttributeMode(Object v, final Variables variables, String attribute, String[] array) {
        super(v, variables, attribute);
        this.values = array;
    }
    
    public MultiAttributeMode(Object v, final Variables variables, String attribute, double g, double y)
    {
        super(v, variables, attribute, g, y);
        this.values = new String[]{"Empty"};
    }
    
    @Override
    public Paint Execute(DirectedGraph<Object,Edge> graph) {
        if (v instanceof ActivityVertex) {
            return GetAttributeColor(((ActivityVertex) v).getAttributeValue(this.attribute));
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
    
    //Method to return 7 dif types of colors depending on the value
    public Paint GetAttributeColor(String value) {
        for (int i = 0; i < values.length; i++)
        {
            if (value.equalsIgnoreCase(values[i])) {
                int r, g, b = 0;
                r = (int) (255);
                g = (int) (255 / i);
                b = (int) (255 / (i * i));
                return new Color(r, g, b);
            }
        }
//        if (value.equalsIgnoreCase(Config.VPMattValue1)) {
//            return new Color(0, 0, 204);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue2)) {
//            return new Color(102, 255, 255);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue3)) {
//            return new Color(153, 255, 51);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue4)) {
//            return new Color(255, 153, 51);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue5)) {
//            return new Color(0, 204, 102);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue6)) {
//            return new Color(204, 204, 0);
//        }
        return new Color(128, 128, 128);
    }
    
}
