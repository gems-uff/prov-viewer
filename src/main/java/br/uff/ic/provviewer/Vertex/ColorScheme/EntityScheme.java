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
    
    public EntityScheme(String attribute, String empty, String g, String y, boolean l) {
        super(attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        
        ComputeValue(variables.graph, false);
        if (v instanceof EntityVertex) {
            if(!limited) {
                return this.CompareValue(((EntityVertex) v).getAttributeValueFloat(this.attribute), this.min, this.max);
            }
            else {
                if(this.givenMax == null) {
                    return this.CompareValue(((EntityVertex) v).getAttributeValueFloat(this.attribute), this.min, Double.parseDouble(this.givenMax));
                }
                if(this.givenMin == null) {
                    return this.CompareValue(((EntityVertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMin), this.max);
                }
                else {
                    return this.CompareValue(((EntityVertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMin), Double.parseDouble(this.givenMax));
                }
            }
        }
        return ((Vertex) v).getColor();
    }
}
