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
public class ProvScheme extends ColorScheme {

    public ProvScheme(String attribute) {
        super(attribute);
    }

    public ProvScheme(boolean isZeroWhite, boolean isInverted, String attribute, String empty, String g, String y, boolean l) {
        super(false, false, attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        return ((Vertex) v).getColor();
    }
}
