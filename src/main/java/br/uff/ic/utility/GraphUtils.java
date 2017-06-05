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

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Vertex.ColorScheme.ColorScheme;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.GraphVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        if (v instanceof GraphVertex) {
            for (Object vertex : ((GraphVertex) v).clusterGraph.getVertices()) {
                if (vertex instanceof GraphVertex) {
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
        double slope = Double.NEGATIVE_INFINITY;
        for (Edge e : colorScheme.variables.graph.getOutEdges(node)) {
            if (!((Vertex) e.getTarget()).getAttributeValue(colorScheme.attribute).contentEquals(VariableNames.UnknownValue)) {
                float attValue = ((Vertex) node).getAttributeValueFloat(colorScheme.attribute) - ((Vertex) e.getTarget()).getAttributeValueFloat(colorScheme.attribute);
                double time = ((Vertex) node).getTime() - ((Vertex) e.getTarget()).getTime();
                if (time != 0) {
                    slope = attValue / time;
                } else if ((attValue != 0) && (time == 0)) {
                    slope = attValue;
                } else if (time == 0) {
                    slope = 0;
                }
            }
        }
        return (float) slope;
    }
    
    public static ArrayList<Float> getAttributeValuesFromVertices(DirectedGraph<Object, Edge> graph, String attribute) {
        Collection<Object> nodes = graph.getVertices();
            ArrayList<Float> values = new ArrayList<>();
            for (Object node : nodes) {
                if (!((Vertex) node).getAttributeValue(attribute).contentEquals(VariableNames.UnknownValue)) {
                    values.add(((Vertex) node).getAttributeValueFloat(attribute));
                }
            }
            return values;
    }
    
    /**
     * Method that returns a GraphVertex from the (Graph) v
     * @param v is the Graph
     * @return the GraphVertex
     */
    public static GraphVertex CreateVertexGraph(Object v) {
        Map<String, String> ids = new HashMap<>();
        Map<String, GraphAttribute> attributes = new HashMap<>(); 
        CreateVertexGraph(v, ids, attributes);
        return new GraphVertex(ids, attributes, (Graph) v);
    }
    
    /**
     * Method to update the attribute value that counts the number of vertices of the specified type
     * @param attributes is the list of attributes already computed
     * @param graphVertex is the current GraphVertex
     * @param type is the type of vertices we are counting (Agents, Activities, Entities)
     */
    private static void UpdateVertexTypeQuantityGraphVertex(Map<String, GraphAttribute> attributes, GraphVertex graphVertex, String type) {
        if(attributes.containsKey(type)) {
            int qnt = (int) Float.parseFloat(attributes.get(type).getValue());
            qnt = (int) (qnt + ((Vertex)graphVertex).getAttributeValueFloat(type));
            attributes.get(type).setValue(Integer.toString(qnt));
        } else {
            GraphAttribute att = new GraphAttribute(type, Integer.toString((int) ((Vertex)graphVertex).getAttributeValueFloat(type)));
            attributes.put(att.getName(), att);
        }
    }
    
    /**
     * Method to update the attribute value that counts the number of vertices of the specified type
     * @param attributes is the list of attributes already computed
     * @param v is the current vertex
     * @param type is the type of vertices we are counting (Agents, Activities, Entities)
     */
    private static void UpdateVertexTypeQuantity(Map<String, GraphAttribute> attributes, Vertex v, String type) {
        if(attributes.containsKey(type)) {
            int qnt = (int) Float.parseFloat(attributes.get(type).getValue());
            qnt++;
            attributes.get(type).setValue(Integer.toString(qnt));
        } else {
            GraphAttribute att = new GraphAttribute(type, Integer.toString(1));
            attributes.put(att.getName(), att);
        }
    }
    /**
     * Recursive method to generate the tooltip. 
     * It considers Graph vertices inside the collapsed vertex.
     * @param v is the current vertex for the tooltip
     * @param ids is all computed ids for the tooltip
     * @param attributes is the attribute list for the tooltip
     * @return 
     */
    public static void CreateVertexGraph(Object v, 
            Map<String, String> ids,
            Map<String, GraphAttribute> attributes){
        
        Collection vertices = ((Graph) v).getVertices();
        for (Object vertex : vertices) {
            if (!(vertex instanceof Graph))
            {
                ids.put(((Vertex) vertex).getID(), ((Vertex) vertex).getID());
                
                if(vertex instanceof GraphVertex) {
                    if(((Vertex)vertex).hasAttribute(VariableNames.CollapsedVertexAgentAttribute)){
                        UpdateVertexTypeQuantityGraphVertex(attributes, (GraphVertex)vertex, VariableNames.CollapsedVertexAgentAttribute);
                    }
                    if(((Vertex)vertex).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)){
                        UpdateVertexTypeQuantityGraphVertex(attributes, (GraphVertex)vertex, VariableNames.CollapsedVertexActivityAttribute);
                    }
                    if(((Vertex)vertex).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)){
                        UpdateVertexTypeQuantityGraphVertex(attributes, (GraphVertex)vertex, VariableNames.CollapsedVertexEntityAttribute);
                    }
                }else if (vertex instanceof AgentVertex) {
                    UpdateVertexTypeQuantity(attributes, (Vertex)vertex, VariableNames.CollapsedVertexAgentAttribute);
                } else if (vertex instanceof ActivityVertex) {
                    UpdateVertexTypeQuantity(attributes, (Vertex)vertex, VariableNames.CollapsedVertexActivityAttribute);
                } else if (vertex instanceof EntityVertex) {
                    UpdateVertexTypeQuantity(attributes, (Vertex)vertex, VariableNames.CollapsedVertexEntityAttribute);
                }
                
                for (GraphAttribute att : ((Vertex) vertex).getAttributes()) {
                    if (attributes.containsKey(att.getName())) {
                        if(!att.getName().equalsIgnoreCase(VariableNames.CollapsedVertexAgentAttribute) 
                                && !att.getName().equalsIgnoreCase(VariableNames.CollapsedVertexActivityAttribute) 
                                && !att.getName().equalsIgnoreCase(VariableNames.CollapsedVertexEntityAttribute)) {
                            GraphAttribute temporary = attributes.get(att.getName());
                            temporary.updateAttribute(att.getAverageValue());
                            attributes.put(att.getName(), temporary);
                        }
                    } else {
                        attributes.put(att.getName(), new GraphAttribute(att.getName(), att.getAverageValue()));
                    }
                }
            }
            else //(vertex instanceof Graph) 
            {
                CreateVertexGraph(vertex, ids, attributes);
            }
        }
    }
}
