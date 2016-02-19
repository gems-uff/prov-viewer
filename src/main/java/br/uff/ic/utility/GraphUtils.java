/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
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
        if(v instanceof Graph) {
            for(Object vertex : ((Graph) v).getVertices()) {
                if(vertex instanceof AgentVertex)
                    return vertex;
                else if(vertex instanceof Graph)
                    return hasAgentVertex(vertex);
                else if(vertex instanceof ActivityVertex)
                    activity = vertex;
                else if (vertex instanceof EntityVertex)
                    entity = vertex;
            }
        } else
            return v;
        if(activity != null)
            return activity;
        else
            return entity;
    }
}
