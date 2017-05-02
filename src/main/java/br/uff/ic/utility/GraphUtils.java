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

package br.uff.ic.utility;

import br.uff.ic.provviewer.Vertex.ColorScheme.ColorScheme;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author Kohwalter
 */
public class GraphUtils {

    public static Object hasAgentVertex(Object v) {
        Object activity = null;
        Object entity = null;
        if (v instanceof Graph) {
            for (Object vertex : ((Graph) v).getVertices()) {
                if (vertex instanceof AgentVertex) {
                    return vertex;
                } else if (vertex instanceof Graph) {
                    return hasAgentVertex(vertex);
                } else if (vertex instanceof ActivityVertex) {
                    activity = vertex;
                } else if (vertex instanceof EntityVertex) {
                    entity = vertex;
                }
            }
        } else {
            return v;
        }
        if (activity != null) {
            return activity;
        } else {
            return entity;
        }
    }

    public static int getCollapsedVertexSize(Object v) {
        int graphSize = 0;
        if (v instanceof Graph) {
            for (Object vertex : ((Graph) v).getVertices()) {
                if (vertex instanceof Graph) {
                    graphSize = graphSize + getCollapsedVertexSize(vertex);
                } else {
                    graphSize++;
                }
            }
        }
        return graphSize;
        //int graphSize = ((Graph) v).getVertexCount();
    }

    // TO DO: Get the mean of slopes if there are more than 1 vertex with the attribute
    // TO DO: Allow for jumping vertices until finding the vertex with the same attribute (e.g., skip an entity between two activities)
    public static float getSlope(Object node, ColorScheme colorScheme) {
        float slope = Float.NEGATIVE_INFINITY;
        for (Edge e : colorScheme.variables.graph.getOutEdges(node)) {
            if (!((Vertex) e.getTarget()).getAttributeValue(colorScheme.attribute).contentEquals("Unknown")) {
                float attValue = ((Vertex) node).getAttributeValueFloat(colorScheme.attribute) - ((Vertex) e.getTarget()).getAttributeValueFloat(colorScheme.attribute);
                float time = ((Vertex) node).getTime() - ((Vertex) e.getTarget()).getTime();
                if (time != 0) {
                    slope = attValue / time;
                } else if ((attValue != 0) && (time == 0)) {
                    slope = attValue;
                } else if (time == 0) {
                    slope = 0;
                }
            }
        }
        return slope;
    }
}
