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
 * Uses a Normalized Vector to color the vertex accordingly
 * @author Kohwalter
 */
public class ActivityVectorScheme extends ColorScheme {

    public ActivityVectorScheme(String attribute, String empty, String g, String y, boolean l) {
        super(attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        String vecValue;
        if (v instanceof ActivityVertex) {
            vecValue = ((ActivityVertex) v).getAttributeValue(this.attribute);
            vecValue = vecValue.replace("(", "");
            vecValue = vecValue.replace(")", "");
            vecValue = vecValue.replace(" ", "");
            String[] vec3 = vecValue.split(","); 
            double vx = Float.parseFloat(vec3[0]);
            double vy = Float.parseFloat(vec3[1]);
            double vz = Float.parseFloat(vec3[2]);
            int cx = (int) ((vx * 0.5 + 0.5) * 255);
            int cy = (int) ((vy * 0.5 + 0.5) * 255);
            int cz = (int) ((vz * 0.5 + 0.5) * 255);
            
            return new Color (cx,cy,cz);

        }
        return ((Vertex) v).getColor();
    }
}
