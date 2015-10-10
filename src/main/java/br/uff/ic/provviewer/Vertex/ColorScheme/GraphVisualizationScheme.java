/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author Kohwalter
 */
public class GraphVisualizationScheme extends ColorScheme {

    public GraphVisualizationScheme(String attribute) {
        super(attribute);
    }

    public GraphVisualizationScheme(String attribute, String empty, String g, String y, boolean l) {
        super(attribute, empty, g, y, l);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {
        if ((v instanceof ActivityVertex) && (((Vertex)v).getID().contains(this.attribute))&& (((Vertex)v).getID().contains(",")))
            return new Color(127, 0, 255);
        else if ((v instanceof ActivityVertex) && (((Vertex)v).getID().contains(this.attribute)))
            return new Color(255, 128, 0);
        else
            return ((Vertex) v).getColor();
    }
}
