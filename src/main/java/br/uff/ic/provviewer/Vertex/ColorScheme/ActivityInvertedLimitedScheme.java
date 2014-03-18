/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class ActivityInvertedLimitedScheme extends ColorScheme {

    public ActivityInvertedLimitedScheme(String attribute, String empty, double g, double y) {
        super(attribute, empty, g, y);
    }
    
    @Override
    public Paint Execute(Object v, final Variables variables) {
        
        if (v instanceof ActivityVertex) {
            return this.CompareValue(((ActivityVertex) v).getAttributeValueInteger(this.attribute), this.givenMax, this.givenMin);
        }
        return ((Vertex) v).getColor();
    }
}
