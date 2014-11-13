/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.EntityVertex;
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

    /**
     * This constructor is used by the Default color scheme
     * @param attribute 
     */
    public ColorScheme(String attribute) {
        this.attribute = attribute;
    }

    /**
     * All new Vertex Paint Mode classes must use a constructor with 4 params, 
     * in the following order and types: String, String, double, double
     * So it can be recognized by  config.java
     * Note that the second String actually goes to to a String[] variable and 
     * is split with " " due to how XML list works
     * @param attribute 
     */
    public ColorScheme(String attribute, String value, String max, String min, boolean inverted) {
        this.attribute = attribute;
        this.value = value.split(" ");
        this.givenMax = max;
        this.givenMin = min;
        this.limited = inverted;
    }

    public String GetName() {
        return attribute;
    }

    public Paint CompareValue(float value, double min, double max){
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        return new Color(Math.min(255, 510 - proportion), Math.min(255, proportion), 0);
    }

    public void ComputeValue(DirectedGraph<Object, Edge> graph) {
        Collection<Object> nodes = graph.getVertices();
        for (Object node : nodes) {
            if(node instanceof ActivityVertex) {
                this.max = Math.max(this.max, Math.abs(((ActivityVertex) node).getAttributeValueFloat(this.attribute)));
                this.min = Math.min(this.min, Math.abs(((ActivityVertex) node).getAttributeValueFloat(this.attribute)));
            }
            else if (node instanceof EntityVertex) {
                this.max = Math.max(this.max, Math.abs(((EntityVertex) node).getAttributeValueFloat(this.attribute)));
                this.min = Math.min(this.min, Math.abs(((EntityVertex) node).getAttributeValueFloat(this.attribute)));
            }
        }
//        System.out.println("Attribute = " + this.attribute);
//        System.out.println("Max = " + this.max);
//        System.out.println("Min = " + this.min);
    }
    
    public abstract Paint Execute(Object v, final Variables variables);
    
}
