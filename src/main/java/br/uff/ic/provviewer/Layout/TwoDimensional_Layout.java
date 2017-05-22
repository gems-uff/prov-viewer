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
package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class TwoDimensional_Layout<V, E> extends ProvViewerLayout<V, E> {

    public TwoDimensional_Layout(Graph<V, E> g, Variables variables) {
        super(g, variables);
    }

    @Override
    public void reset() {
        doInit();
    }

    @Override
    public void initialize() {
        doInit();
    }

    /**
     * Initialize layout
     */
    private void doInit() {
//        Collection<String> values = Utils.DetectAllPossibleValuesFromAttribute(variables.graph.getVertices(), "GraphFile");
        setVertexOrder();
        String x_att = variables.layout_attribute_X;
        String y_att = variables.layout_attribute_Y;
        boolean isReverse_X;
        boolean isReverse_Y;

        isReverse_X = Utils.getMinusSign(x_att);
        isReverse_Y = Utils.getMinusSign(y_att);
        x_att = Utils.removeMinusSign(x_att);
        y_att = Utils.removeMinusSign(y_att);

//        int yGraphOffset = 0;
        int scale = (int) (1 * variables.config.vertexSize);
        for (V v : vertex_ordered_list) {
            int attValue_x;
            int attValue_y;
            if (Utils.isItTime(x_att)) {
                attValue_x = (int) ((Vertex) v).getNormalizedTime();
                attValue_x = (int) Utils.convertTime(variables.config.timeScale, attValue_x, variables.selectedTimeScale);
            } else {
                attValue_x = (int) ((Vertex) v).getAttributeValueFloat(x_att);
            }
            if (Utils.isItTime(y_att)) {
                attValue_y = (int) ((Vertex) v).getNormalizedTime();
                attValue_y = (int) Utils.convertTime(variables.config.timeScale, attValue_y, variables.selectedTimeScale);
            } else {
                attValue_y = (int) ((Vertex) v).getAttributeValueFloat(y_att);
            }
            Point2D coord = transform(v);
//            int j = 0;
//            for(String g : values) {
//                if(((Vertex)v).getAttributeValue("GraphFile").contains(g)) {
//                    yGraphOffset = (int) (variables.config.vertexSize * j);
//                    break;
//                }
//                j++;
//            }
            if (isReverse_X) {
                attValue_x *= -1;
            }
            if (isReverse_Y) {
                attValue_y *= -1;
            }

            coord.setLocation(attValue_x * scale, attValue_y * scale * -1);
        }
    }

    /**
     * This one is an incremental visualization.
     *
     * @return true
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
     *
     * @return true
     */
    @Override
    public boolean done() {
//        if (currentIteration > mMaxIterations || temperature < 1.0/max_dimension)
//        {
//            return true;
//        }
//        return false;
        return true;
    }

    @Override
    public void step() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
