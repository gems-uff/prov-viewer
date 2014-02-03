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

    public String attribute;
    public double valueGreenThreshold;
    public double valueYellowThreshold;

    public VertexPaintMode(String attribute) {
        this.attribute = attribute;
        this.valueGreenThreshold = 75;
        this.valueYellowThreshold = 40;
    }

    public VertexPaintMode(String attribute, double g, double y) {
        this.attribute = attribute;
        this.valueGreenThreshold = g;
        this.valueYellowThreshold = y;
    }

    public String GetName() {
        return attribute;
    }

    public abstract Paint Execute(Object v, final Variables variables);

    public abstract Paint CompareValue(int value, float constant);
}
