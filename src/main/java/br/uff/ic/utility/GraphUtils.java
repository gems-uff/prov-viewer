/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
