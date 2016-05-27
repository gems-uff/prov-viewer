/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;

/**
 *
 * @author Kohwalter
 */
public abstract class ColorScheme {

    public String attribute;
    public String[] value;
    public double max;
    public double min;
    public String givenMax;
    public String givenMin;
    public boolean limited;
    private boolean computedMinMax;
    public String restrictedAttribute;
    public String restrictedValue;

    /**
     * This constructor is used by the Default color scheme
     *
     * @param attribute
     */
    public ColorScheme(String attribute) {
        this.attribute = attribute;
        this.value = null;
        this.givenMax = null;
        this.givenMin = null;
        this.limited = false;
        this.computedMinMax = false;
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }

    /**
     * All new Vertex Paint Mode classes must use a constructor with 4 params,
     * in the following order and types: String, String, double, double So it
     * can be recognized by config.java Note that the second String actually
     * goes to to a String[] variable and is split with " " due to how XML list
     * works
     *
     * @param attribute
     * @param value
     * @param max
     * @param min
     * @param limited
     */
    public ColorScheme(String attribute, String value, String max, String min, boolean limited) {
        this.attribute = attribute;
        this.value = value.split(" ");
        this.givenMax = max;
        this.givenMin = min;
        this.limited = limited;
        this.computedMinMax = false;
    }

    public ColorScheme(String attribute, String value, String max, String min, boolean limited, String rA, String rV) {
        this.attribute = attribute;
        this.value = value.split(" ");
        this.givenMax = max;
        this.givenMin = min;
        this.limited = limited;
        this.computedMinMax = false;
        this.restrictedAttribute = rA;
        this.restrictedValue = rV;
    }

    public String GetName() {
        return attribute;
    }

    public Paint CompareValue(float value, double min, double max) {
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        return new Color(Math.min(255, 510 - proportion), Math.min(255, proportion), 0);
//        if(value > 0 && max > min)
//            return compareValueGreen(value, min, max);
//        else
//            return compareValueRed(value, min, max);
    }
    
    public Paint compareValueGreen(float value, double min, double max){
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        proportion = Math.max(proportion, 0);
        return new Color(0, Math.min(255, proportion), 0);
    }
    public Paint compareValueRed(float value, double min, double max){
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        proportion = Math.min(proportion, 510);
        return new Color(Math.min(255, 510 - proportion), 0, 0);
    }

    public Paint GetMinMaxColor(Object v) {
        if(!((Vertex) v).getAttributeValue(this.attribute).contentEquals("Unknown"))
        {
            if (!limited) {
                return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), this.min, this.max);
            } else {
                if (this.givenMax == null) {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMin), this.max);
                }
                if (this.givenMin == null) {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), this.min, Double.parseDouble(this.givenMax));
                } else {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMin), Double.parseDouble(this.givenMax));
                }
            }
        }
        return ((Vertex) v).getColor();
    }

    public Paint GetInvertedMinMaxColor(Object v) {
        if(!((Vertex) v).getAttributeValue(this.attribute).contentEquals("Unknown"))
        {
            if (!limited) {
                return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), this.max, this.min);
            } else {
                if (this.givenMax == null) {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), this.max, Double.parseDouble(this.givenMin));
                }
                if (this.givenMin == null) {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMax), this.min);
                } else {
                    return this.CompareValue(((Vertex) v).getAttributeValueFloat(this.attribute), Double.parseDouble(this.givenMax), Double.parseDouble(this.givenMin));
                }
            }
        }
        return ((Vertex) v).getColor();
    }

    public void ComputeValue(DirectedGraph<Object, Edge> graph, boolean isActivity) {
        if (!computedMinMax) {
            Collection<Object> nodes = graph.getVertices();
            for (Object node : nodes) {
                if(!((Vertex) node).getAttributeValue(this.attribute).contentEquals("Unknown"))
                {
//                if (node instanceof ActivityVertex) {
                    this.max = Math.max(this.max, ((Vertex) node).getAttributeValueFloat(this.attribute));
                    this.min = Math.min(this.min, ((Vertex) node).getAttributeValueFloat(this.attribute));
//                } else if (node instanceof EntityVertex) {
//                    this.max = Math.max(this.max, ((EntityVertex) node).getAttributeValueFloat(this.attribute));
//                    this.min = Math.min(this.min, ((EntityVertex) node).getAttributeValueFloat(this.attribute));
                }
            }
            computedMinMax = true;
        }
    }

    public void ComputeRestrictedValue(DirectedGraph<Object, Edge> graph, boolean isActivity, String aRestriction, String aValue) {
        if (!computedMinMax) {
            Collection<Object> nodes = graph.getVertices();
            for (Object node : nodes) {
                if(!((Vertex) node).getAttributeValue(this.attribute).contentEquals("Unknown")) {
                    if (((Vertex) node).getAttributeValue(aRestriction).equalsIgnoreCase(aValue)) {
                        this.max = Math.max(this.max, ((Vertex) node).getAttributeValueFloat(this.attribute));
                        this.min = Math.min(this.min, ((Vertex) node).getAttributeValueFloat(this.attribute));
                    }
//                } else if (node instanceof EntityVertex && !isActivity) {
//                    this.max = Math.max(this.max, ((EntityVertex) node).getAttributeValueFloat(this.attribute));
//                    this.min = Math.min(this.min, ((EntityVertex) node).getAttributeValueFloat(this.attribute));
                }
            }
            computedMinMax = true;
        }
    }

    public abstract Paint Execute(Object v, final Variables variables);

}
