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

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class ListLayout<V, E> extends ProvViewerTimelineLayout<V, E> {

    public ListLayout(Graph<V, E> g, Variables variables) {
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

        Map<String, Integer> counts = new HashMap<>();
        int i = 0;
        int position = 0;
        int offset = scale * 15;
        int xGraphOffset;

        x_att = Utils.removeMinusSign(x_att);
        Comparator comp = Utils.getVertexAttributeComparator(x_att);
        setVertexOrder(comp, comp, comp);
        
        for (String gs : variables.graphNames) {
            counts.put(gs, 0);
        }
        
        for (V v : agent_ordered_list) {
            Point2D coord = transform(v);
            xGraphOffset = 2 * getYGraphOffSet(v);
            coord.setLocation(position + xGraphOffset, i);
            i += scale;
        }
        i = 0;
        position += offset;
        for (V v : vertex_ordered_list) {
            Point2D coord = transform(v);
            xGraphOffset = 2 * getYGraphOffSet(v);
            coord.setLocation(position + xGraphOffset, i);
            i += scale;
        }
        i = 0;
        position += offset;
        for (V v : entity_ordered_list) {
            Point2D coord = transform(v);
            xGraphOffset = 2 * getYGraphOffSet(v);
            coord.setLocation(position + xGraphOffset, i);
            i += scale;
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
