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
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 *
 * @deprecated
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class TimelineGraphs_Layout<V, E> extends ProvViewerLayout<V, E> {

    public TimelineGraphs_Layout(Graph<V, E> g, Variables variables) {
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
        String x_att = variables.layout_attribute_X;
        Map<String, Integer> counts = new HashMap<>();
        int previous_Value = 0;
        int previous_yOffset = 0;
        int i = 0;
        int agentY = 0;
        double yPos = 0;
        double xPos = 0;
        int yGraphOffset = 0;
        int entityXPos;
        int scale = 2 * variables.config.vertexSize;

        x_att = Utils.removeMinusSign(x_att);
        setVertexOrder(Utils.getVertexAttributeComparator(x_att));
        entityXPos = (int) (vertex_ordered_list.size() * 0.5 - (entity_ordered_list.size() * 0.75));
        
        for (String gs : variables.graphNames) {
            counts.put(gs, 0);
        }
        
        entityXPos = entityXPos * scale;
        for (V v : vertex_ordered_list) {
            yPos = 0;
            Point2D coord = transform(v);
            int j = 0;
            String[] graphFiles = ((Vertex) v).getAttributeValues("GraphFile");

            int k = 0;
            for (String g : counts.keySet()) {
                if (((Vertex) v).getAttributeValue("GraphFile").contains(g)) {
                    yGraphOffset = (int) (variables.config.vertexSize * k);
                    break;
                }
                k++;
            }

            if (v instanceof AgentVertex) {
                yPos = agentY;
                agentY = agentY + variables.numberOfGraphs * scale;
                i = i + scale;
                xPos = i;
            } else if (v instanceof ActivityVertex) {
                if (layout_graph.getOutEdges(v) != null) {
                    for (E neighbor : layout_graph.getOutEdges(v)) {
                        //if the edge link to an Agent-node
                        if (layout_graph.getDest(neighbor) instanceof AgentVertex) {
                            Point2D agentPos = transform(layout_graph.getDest(neighbor));
                            yPos = agentPos.getY();
                        }
                        if (previous_Value != (int) ((Vertex) v).getAttributeValueFloat(x_att)) {
                            i = i + scale;
                        } else if (previous_yOffset == yGraphOffset) {
                            i = (int) (i + (scale * 0.25));
                        }
                        xPos = i;
                    }
                }
            } else {
                yPos = 0;
                i = i + scale;
                xPos = i;
            }
            yPos = yPos + yGraphOffset;
            coord.setLocation(xPos, yPos);
            previous_Value = (int) ((Vertex) v).getAttributeValueFloat(x_att);
            previous_yOffset = yGraphOffset;
        }
        for (V v : entity_ordered_list) {
            Point2D coord = transform(v);
            // Position then in the middle
            xPos = entityXPos;
            entityXPos = entityXPos + scale;
            yPos = -10 * scale;
            coord.setLocation(xPos, yPos);
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
