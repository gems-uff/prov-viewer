/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class EntityInvertedRestrictedScheme extends ColorScheme {

    public EntityInvertedRestrictedScheme(String attribute, String empty, String g, String y, boolean l) {
        super(attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {

        ComputeRestrictedValue(variables.graph, true, this.restrictedAttribute, this.restrictedValue);
        if ((v instanceof EntityVertex) && ((EntityVertex) v).getAttributeValue(this.restrictedAttribute).equalsIgnoreCase(this.restrictedValue)) {
            return this.GetInvertedMinMaxColor(v);
        }
        return ((Vertex) v).getColor();
    }

}
