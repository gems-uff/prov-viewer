/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Variables;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class WeekendMode extends ColorScheme {

//    public String sat;
//    public String sun;
//
//    public WeekendMode(String attribute, String sat, String sun) {
//        super(attribute);
//        this.sat = sat;
//        this.sun = sun;
//    }
//
//    public WeekendMode(String attribute, String sat, String sun, double g, double y) {
//        super(attribute, g, y);
//        this.sat = sat;
//        this.sun = sun;
//    }
    
    public WeekendMode(String attribute, String days, double g, double y) {
        super(attribute, days, g, y);
//        String[] names = days.split(" ");
//        this.sat = names[0];
//        this.sun = names[1];
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        String day = ((Vertex) v).getDayName();
        if (day.equalsIgnoreCase(this.value[0]) || day.equalsIgnoreCase(this.value[1])) {
            return new Color(255, 0, 0);
        }
        return ((Vertex) v).getColor();
    }

    @Override
    public Paint CompareValue(int value, float constant) {
        if (value > this.valueGreenThreshold) {
            return new Color(0, 255, 0);
        } else {
            if (value > this.valueYellowThreshold) {
                return new Color(255, 255, 0);
            } else {
                return new Color(255, 0, 0);
            }
        }
    }
}
