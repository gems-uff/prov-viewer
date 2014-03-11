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
public class InvertedAttributeMode extends ColorScheme {

//    public InvertedAttributeMode(String attribute) {
//        super(attribute);
//    }
//
//    public InvertedAttributeMode(String attribute, double g, double y) {
//        super(attribute, g, y);
//    }

    public InvertedAttributeMode(String attribute, String empty, double g, double y) {
        super(attribute, empty, g, y);
    }
    
    @Override
    public Paint Execute(Object v, final Variables variables) {
        if (v instanceof ActivityVertex) {
            return this.CompareValue(((ActivityVertex) v).getAttributeValueInteger(this.attribute), 0);
        }
        return ((Vertex) v).getColor();
    }

    @Override
    public Paint CompareValue(int value, float constant) {
        if (value < this.valueGreenThreshold) {
            return new Color(0, 255, 0);
        } else {
            if (value < this.valueYellowThreshold) {
                return new Color(255, 255, 0);
            } else {
                return new Color(255, 0, 0);
            }
        }
    }
}
