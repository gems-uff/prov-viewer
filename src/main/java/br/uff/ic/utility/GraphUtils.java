/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author Kohwalter
 */
public class GraphUtils {
    public static Object hasAgentVertex(Object v) {
        if(v instanceof Graph) {
            for(Object ver : ((Graph) v).getVertices()) {
                if(ver instanceof AgentVertex) {
                        return ver;
                }
                else if(ver instanceof Graph) {
                    hasAgentVertex(ver);
                }
            }
        }
//        System.out.println(((Vertex)v).getNodeType());
        return v;
    }
}
