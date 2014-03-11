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
public class MultiAttributeMode extends ColorScheme {

//    String[] values;

//    public MultiAttributeMode(String attribute, String[] array) {
//        super(attribute);
//        this.values = array;
//    }
//
//    public MultiAttributeMode(String attribute, double g, double y) {
//        super(attribute, g, y);
//        this.values = new String[]{"Empty"};
//    }
    
    public MultiAttributeMode(String attribute, String valuesList, double g, double y) {
        super(attribute, valuesList, g, y);
//        this.values = valuesList.split(" ");
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        if (v instanceof ActivityVertex) {
            return GetAttributeColor(((ActivityVertex) v).getAttributeValue(this.attribute));
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
//        if (value.equalsIgnoreCase(Config.VPMattValue1)) {
//            return new Color(0, 0, 204);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue2)) {
//            return new Color(102, 255, 255);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue3)) {
//            return new Color(153, 255, 51);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue4)) {
//            return new Color(255, 153, 51);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue5)) {
//            return new Color(0, 204, 102);
//        } else if (value.equalsIgnoreCase(Config.VPMattValue6)) {
//            return new Color(204, 204, 0);
//        }
        return new Color(128, 128, 128);
    }
}
