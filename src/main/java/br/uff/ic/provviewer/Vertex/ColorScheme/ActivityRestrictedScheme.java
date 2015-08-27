/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class ActivityRestrictedScheme extends ColorScheme {

    public ActivityRestrictedScheme(String attribute, String empty, String g, String y, boolean l, String aR, String aV) {
        super(attribute, empty, g, y, l, aR, aV);
    }
    
    @Override
    public String GetName() {
        return attribute + "(" + this.restrictedValue + ")";
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        
        ComputeRestrictedValue(variables.graph, true, this.restrictedAttribute, this.restrictedValue);
        if ((v instanceof ActivityVertex) && ((ActivityVertex) v).getAttributeValue(this.restrictedAttribute).equalsIgnoreCase(this.restrictedValue)) {
            return this.GetMinMaxColor(v);
        }
        return ((Vertex) v).getColor();
    }
}
