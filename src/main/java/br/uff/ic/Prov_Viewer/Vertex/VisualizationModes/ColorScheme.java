/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Variables;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public abstract class ColorScheme {

    public String attribute;
    public String[] value;
    public double valueGreenThreshold;
    public double valueYellowThreshold;

    /**
     * This constructor is used by the Default color scheme
     * @param attribute 
     */
    public ColorScheme(String attribute) {
        this.attribute = attribute;
        this.valueGreenThreshold = 75;
        this.valueYellowThreshold = 40;
    }
//
//    public ColorScheme(String attribute, double g, double y) {
//        this.attribute = attribute;
//        this.valueGreenThreshold = g;
//        this.valueYellowThreshold = y;
//    }
    /**
     * All new Vertex Paint Mode classes must use a constructor with 4 params, 
     * in the following order and types: String, String, double, double
     * So it can be recognized by  config.java
     * Note that the second String actually goes to to a String[] variable and 
     * is split with " " due to how XML list works
     * @param attribute 
     */
    public ColorScheme(String attribute, String value, double g, double y) {
        this.attribute = attribute;
        this.value = value.split(" ");
        this.valueGreenThreshold = g;
        this.valueYellowThreshold = y;
    }

    public String GetName() {
        return attribute;
    }
    
    public abstract Paint Execute(Object v, final Variables variables);

    public abstract Paint CompareValue(int value, float constant);
}
