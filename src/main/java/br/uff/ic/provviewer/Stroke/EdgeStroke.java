/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package br.uff.ic.provviewer.Stroke;

import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.utility.graph.Edge;
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
     * Defines neutral edges stroke
     *
     * @param edge The edge to be analyzed
     * @param isStrokeByValue define if we want to use the stroke size based on the edge's value (TRUE) or by the #graphs it belongs (FALSE)
     * @return Stroke (neutral edges are dashed, others are not)
     */
    public static Stroke EdgeStroke(Edge edge, boolean isStrokeByValue) {
//        float[] dash = {5.0f};
//
//        if (edge.isNeutral()) {
//            return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
//        }
        float size = 1;
        if(!isStrokeByValue) {
//            Vertex target = (Vertex) edge.getTarget();
            String[] graphs = edge.getAttributeValues("GraphFile");
            size = 1 + (graphs.length - 1) * 2;
        }
        return new BasicStroke(size, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
    }

    /**
     * Allows to manually define (any) edge's stroke
     * @deprecated
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
        if(variables.isStrokeByValue) {
            for (EdgeType edgetype : variables.config.edgetype) {
                if (edge.getLabel().contains(edgetype.type)) {
                    if (edgetype.stroke.equalsIgnoreCase("MAX")) {
                        float maxAbs = Math.max(Math.abs(edgetype.max), Math.abs(edgetype.min));
                        return defineStroke(edge.getValue(), maxAbs);
                    } else {
                        return defineStroke(edge.getValue(), edgetype.total / edgetype.count);
                    }
                }
            }
        }
        return EdgeStroke(edge, variables.isStrokeByValue);
    }
}
