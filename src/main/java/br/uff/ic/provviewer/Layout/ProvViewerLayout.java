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
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Kohwalter
 * @param <V>
 * @param <E>
 */
public abstract class ProvViewerLayout<V, E> extends AbstractLayout<V, E> implements IterativeContext {
    public Variables variables;
    public List<V> vertex_ordered_list;
    public List<V> entity_ordered_list;
    public Graph<V, E> layout_graph;
 
    public ProvViewerLayout(Graph<V, E> g, Variables variables) {
        super(g);
        this.variables = variables;
        layout_graph = (DirectedGraph<V, E>) variables.graph;
    }
    
    public void setVertexOrder(Comparator<V> comparator) {
        if (vertex_ordered_list == null) {
            vertex_ordered_list = new ArrayList<>();
            entity_ordered_list = new ArrayList<>();
            for (V v : graph.getVertices()) {
                if(v instanceof EntityVertex) {
                    entity_ordered_list.add(v);
                }
                else
                    vertex_ordered_list.add(v);
            }
        }
        Collections.sort(vertex_ordered_list, comparator);
        Collections.sort(entity_ordered_list, comparator);
    }
    
    public void setVertexOrder(Comparator<V> comparator, Comparator<V> comparatorEntity) {
        if (vertex_ordered_list == null) {
            vertex_ordered_list = new ArrayList<>();
            entity_ordered_list = new ArrayList<>();
            for (V v : graph.getVertices()) {
                if(v instanceof EntityVertex 
                        || (((Vertex)v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute) 
                        && !((Vertex)v).hasAttribute(VariableNames.CollapsedVertexAgentAttribute) 
                        && !((Vertex)v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute))) {
                    entity_ordered_list.add(v);
                }
                else
                    vertex_ordered_list.add(v);
            }
        }
        Collections.sort(vertex_ordered_list, comparator);
        Collections.sort(entity_ordered_list, comparatorEntity);
    }
    
    public void setVertexOrder() {
        if (vertex_ordered_list == null) {
            vertex_ordered_list = new ArrayList<>();
            entity_ordered_list = new ArrayList<>();
            for (V v : graph.getVertices()) {
                if(v instanceof EntityVertex) {
                    entity_ordered_list.add(v);
                }
                else
                    vertex_ordered_list.add(v);
            }
        }
    }
    
    /**
     * Sets the order of the vertices in the layout according to the ordering of
     * {@code vertex_list}.
     * @param vertex_list
     */
    public void setVertexOrder(List<V> vertex_list) {
        if (!vertex_list.containsAll(getGraph().getVertices())) {
            throw new IllegalArgumentException("Supplied list must include "
                    + "all vertices of the graph");
        }
        this.vertex_ordered_list = vertex_list;
    }
}
