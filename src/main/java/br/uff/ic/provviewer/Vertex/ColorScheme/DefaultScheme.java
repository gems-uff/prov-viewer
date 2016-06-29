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
public class DefaultScheme extends ColorScheme {

    public DefaultScheme(String attribute) {
        super(attribute);
    }

    public DefaultScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(isZeroWhite, isInverted, attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        this.variables = variables;
        if (v instanceof ActivityVertex) {
            return ((ActivityVertex) v).getDefaultColor(variables);
        }
        else
            return ((Vertex) v).getColor();
    }
}
