/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Stroke;

import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Variables;
import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Class for setting the edge's width (labeled as stroke)
 *
 * @author Kohwalter
 */
public class EdgeStroke {

    /**
     * Defines neutral edges to have strokes (dash) of 5.0f Dashes might reduce
     * performance
     *
     * @param edge The edge to be analyzed
     * @return Stroke (neutral edges are dashed, others are not)
     */
    public static Stroke EdgeStroke(Edge edge) {
        float[] dash = {5.0f};

        if (edge.isNeutral()) {
            return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        }
        return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
    }

    /**
     * Allows to manually define (any) edge's stroke
     *
     * @param edge Edge to have the stroke changed
     * @param dash The array representing the dashing pattern
     * @return Stroke
     */
    public static Stroke EdgeStroke(Edge edge, float[] dash) {
        return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
    }

    /**
     * Method to define edge's width according to its value
     *
     * @param value Edge's value
     * @param average Edge's type average value
     * @return (Stroke) Edge's width proportional to all edges of the same type.
     */
    public static BasicStroke defineStroke(float value, float average) {
        float size;
        size = (float) (5 * (Math.abs(value) / average)) + 1;
        size = Math.min(6, size);
        if (Float.isNaN(size)) {
            size = 1;
        }
        return new BasicStroke(size, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
    }

    /**
     * Method to define edge stroke by type
     *
     * @param edge Edge
     * @param variables Variables type
     * @return Stroke
     */
    public static Stroke StrokeByType(Edge edge, Variables variables) {
        for (int i = 0; i < Config.edgetype.size(); i++) {
            if (edge.getLabel().contains(Config.edgetype.get(i).type)) {
                if(Config.edgetype.get(i).stroke.equalsIgnoreCase("MAX")){
                    float maxAbs = Math.max(Math.abs(Config.edgetype.get(i).max), Math.abs(Config.edgetype.get(i).min));
                    return defineStroke(edge.getValue(), maxAbs);
                }
                else{
                    return defineStroke(edge.getValue(), (Config.edgetype.get(i).total / Config.edgetype.get(i).count));
                }
            }
        }
        return EdgeStroke(edge);
    }
}
