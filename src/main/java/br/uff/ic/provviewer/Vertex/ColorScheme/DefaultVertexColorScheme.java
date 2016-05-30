/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class DefaultVertexColorScheme extends ColorScheme {

    public DefaultVertexColorScheme(String attribute) {
        super(attribute);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        
        ComputeValue(variables.graph, true);
        if (v instanceof ActivityVertex || v instanceof EntityVertex) {
            return this.GetMinMaxColor(v, false);
        }
        return ((Vertex) v).getColor();
    }
}
