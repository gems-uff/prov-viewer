/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class EntityScheme extends ColorScheme {
    
    public EntityScheme(String attribute, String empty, double g, double y, boolean l) {
        super(attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        
        ComputeValue(variables.graph);
        if (v instanceof EntityVertex) {
            if(!limited) {
                return this.CompareValue(((EntityVertex) v).getAttributeValueInteger(this.attribute), this.min, this.max);
            }
            else {
                return this.CompareValue(((EntityVertex) v).getAttributeValueInteger(this.attribute), this.givenMin, this.givenMax);
            }
        }
        return ((Vertex) v).getColor();
    }
}
