/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class ActivityMultiScheme extends ColorScheme {
    
    public ActivityMultiScheme(String attribute, String valuesList, double g, double y, boolean l) {
        super(attribute, valuesList, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        if (v instanceof ActivityVertex) {
            return GetAttributeColor(((ActivityVertex) v).getAttributeValue(this.attribute));
        }
        return ((Vertex) v).getColor();
    }

    //Method to return 7 dif types of colors depending on the value
    public Paint GetAttributeColor(String value) {
        for (int i = 0; i < this.value.length; i++) {
            if (value.equalsIgnoreCase(this.value[i])) {
                int r, g, b = 0;
                r = (int) (120);
                g = (int) (255 / (i + 1));
                b = (int) (255 / ((i + 1) * (i + 1)));
                return new Color(r, g, b);
            }
        }
        return new Color(128, 128, 128);
    }
}
