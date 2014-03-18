/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.Vertex;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class WeekendScheme extends ColorScheme {

    
    public WeekendScheme(String attribute, String days, double g, double y) {
        super(attribute, days, g, y);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        String day = ((Vertex) v).getDayName();
        if (day.equalsIgnoreCase(this.value[0]) || day.equalsIgnoreCase(this.value[1])) {
            return new Color(255, 0, 0);
        }
        return ((Vertex) v).getColor();
    }
}
