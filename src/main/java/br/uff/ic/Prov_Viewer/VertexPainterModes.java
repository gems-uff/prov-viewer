/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class VertexPainterModes {

    static VertexFilterPainter vertexDefaultFilter = new VertexFilterPainter(75, 40, 6, 12, 0.7, 0.3);

    //Activity Attribute evaluation, default colors
    public static Paint Mode1(Object v, final Variables variables) {
        if ((((Variables) variables).showMode1) && (v instanceof ActivityVertex)) {
            return vertexDefaultFilter.ValueCompareColor(((ActivityVertex) v).getAttributeValueInteger(Config.VPMattMode1));
        }
        return ((Vertex) v).getColor();
    }

    //Activity Attribute evaluation, default colors
    public static Paint Mode2(Object v, final Variables variables) {
        if ((((Variables) variables).showMode2) && (v instanceof ActivityVertex)) {
            return vertexDefaultFilter.ValueCompareColor(((ActivityVertex) v).getAttributeValueInteger(Config.VPMattMode2));
        }
        return ((Vertex) v).getColor();
    }

    //Activity Attribute evaluation, inverted colors
    public static Paint Mode3(Object v, final Variables variables) {
        if ((((Variables) variables).showMode3) && (v instanceof ActivityVertex)) {
            return vertexDefaultFilter.InvertedValueCompareColor(((ActivityVertex) v).getAttributeValueInteger(Config.VPMattMode3));
        }
        return ((Vertex) v).getColor();
    }
    
    //Entity Attribute evaluation
    public static Paint Mode4(Object v, final Variables variables) {
        if (v instanceof EntityVertex) {
            return vertexDefaultFilter.ConstantCompareColor(((EntityVertex) v).getAttributeValueInteger(Config.VPMattMode4), ((Variables) variables).entityValue[0]);
        }
        return ((Vertex) v).getColor();
    }

    //Activity specific Attribute value evaluation (6 possible values)
    public static Paint Mode5(Object v, final Variables variables) {
        if (v instanceof ActivityVertex) {
            return GetAttributeColor(((ActivityVertex) v).getAttributeValue(Config.VPMattMode5));
        }
        return ((Vertex) v).getColor();
    }
    
    //All vertices types: Date Evaluation marking Weekend days
    public static Paint Mode6(Object v, final Variables variables) {
        String day = ((Vertex) v).getDayName();
        if (day.equalsIgnoreCase(Config.VPMsaturday) || day.equalsIgnoreCase(Config.VPMsunday)) {
            return new Color(255, 0, 0);
        }
        return ((Vertex) v).getColor();
    }

    //Method to return 7 dif types of colors depending on the value
    public static Paint GetAttributeColor(String value) {
        if (value.equalsIgnoreCase(Config.VPMattValue1)) {
            return new Color(0, 0, 204);
        } else if (value.equalsIgnoreCase(Config.VPMattValue2)) {
            return new Color(102, 255, 255);
        } else if (value.equalsIgnoreCase(Config.VPMattValue3)) {
            return new Color(153, 255, 51);
        } else if (value.equalsIgnoreCase(Config.VPMattValue4)) {
            return new Color(255, 153, 51);
        } else if (value.equalsIgnoreCase(Config.VPMattValue5)) {
            return new Color(0, 204, 102);
        } else if (value.equalsIgnoreCase(Config.VPMattValue6)) {
            return new Color(204, 204, 0);
        }
        return new Color(128, 128, 128);
    }
}
