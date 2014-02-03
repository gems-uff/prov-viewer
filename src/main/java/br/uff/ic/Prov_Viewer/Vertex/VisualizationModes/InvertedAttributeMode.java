/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Variables;
import br.uff.ic.Prov_Viewer.Vertex.ActivityVertex;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class InvertedAttributeMode extends VertexPaintMode{

    public InvertedAttributeMode(String attribute) {
        super(attribute);
    }
    
    public InvertedAttributeMode(String attribute, double g, double y)
    {
        super(attribute, g, y);
    }
    
    @Override
    public Paint Execute(Object v, final Variables variables) {
        if (v instanceof ActivityVertex) {
            return this.CompareValue(((ActivityVertex) v).getAttributeValueInteger(this.attribute), 0);
        }
        return ((Vertex) v).getColor();
    }

    @Override
    public Paint CompareValue(int value, float constant) {
        if(value < this.valueGreenThreshold) {
            return new Color(0,255,0);
        }
        else
        {
            if(value < this.valueYellowThreshold) {
                return new Color(255,255,0);
            }
            else {
                return new Color(255,0,0);
            }
        }
    }
    
}
