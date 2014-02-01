/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Variables;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public abstract class VertexPaintMode {
    public Object v;
    public Variables variables;
    public String attribute;
    public double valueGreenThreshold;
    public double valueYellowThreshold;
    
    
    public VertexPaintMode(Object v, final Variables variables, String attribute)
    {
        this.v = v;
        this.variables = variables;
        this.attribute = attribute;
        this.valueGreenThreshold = 75;
        this.valueYellowThreshold = 40;
    }
    public VertexPaintMode(Object v, final Variables variables, String attribute, double g, double y)
    {
        this.v = v;
        this.variables = variables;
        this.attribute = attribute;
        this.valueGreenThreshold = g;
        this.valueYellowThreshold = y;
    }
    
    public abstract Paint Execute();
    public abstract Paint CompareValue(int value, float constant);

}
