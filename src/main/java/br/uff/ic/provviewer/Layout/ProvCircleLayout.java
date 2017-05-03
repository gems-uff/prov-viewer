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
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author Kohwalter
 */
public class ProvCircleLayout<V, E> extends ProvViewerLayout<V, E> {

    private double radius;
    private List<V> vertex_ordered_list;

    Map<V, CircleVertexData> circleVertexDataMap
            = LazyMap.decorate(new HashMap<V, CircleVertexData>(),
                    new Factory<CircleVertexData>() {
                public CircleVertexData create() {
                    return new CircleVertexData();
                }
            });

    /**
     * Creates an instance for the specified graph.
     */
    public ProvCircleLayout(Graph<V, E> g, Variables variables) {
        super(g, variables);
    }

    /**
     * Returns the radius of the circle.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the circle. Must be called before {@code initialize()}
     * is called.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Sets the order of the vertices in the layout according to the ordering
     * specified by {@code comparator}.
     */
    public void setVertexOrder(Comparator<V> comparator) {
        if (vertex_ordered_list == null) {
            vertex_ordered_list = new ArrayList<V>(getGraph().getVertices());
        }
        Collections.sort(vertex_ordered_list, comparator);
    }

    /**
     * Sets the order of the vertices in the layout according to the ordering of
     * {@code vertex_list}.
     */
    public void setVertexOrder(List<V> vertex_list) {
        if (!vertex_list.containsAll(getGraph().getVertices())) {
            throw new IllegalArgumentException("Supplied list must include "
                    + "all vertices of the graph");
        }
        this.vertex_ordered_list = vertex_list;
    }

    public void reset() {
        initialize();
    }

    public void initialize() {
        Dimension d = getSize();
        
        // Sort vertices by their timestamp
        Comparator comparator = new Comparator<Object>() {
                @Override
                public int compare(Object c1, Object c2) {
                    if(!(c1 instanceof Graph) && !(c2 instanceof Graph))
                        return Float.compare(((Vertex)c1).getTime(),(((Vertex)c2).getTime()));
                    else
                        return 0;
                }
            };

        if (d != null) {
            if (vertex_ordered_list == null) {
//                setVertexOrder(new ArrayList<V>(getGraph().getVertices()));
                setVertexOrder(comparator);
            }

            double height = d.getHeight();
            double width = d.getWidth();

            if (radius <= 0) {
                radius = 0.45 * (height < width ? height : width);
            }

            int i = 0;
            for (V v : vertex_ordered_list) {
                Point2D coord = transform(v);

                double angle = (2 * Math.PI * i) / vertex_ordered_list.size();
                if(v instanceof EntityVertex) {
                    coord.setLocation(Math.cos(angle) * radius * 1.25 + width / 2,
                        Math.sin(angle) * radius * 1.25 + height / 2);
                }
                else if(v instanceof AgentVertex) {
                    coord.setLocation(Math.cos(angle) * radius * 0.75 + width / 2,
                        Math.sin(angle) * radius * 0.75 + height / 2);
                }
                else {
                    coord.setLocation(Math.cos(angle) * radius + width / 2,
                        Math.sin(angle) * radius + height / 2);
                }
                CircleVertexData data = getCircleData(v);
                data.setAngle(angle);
                i++;
            }
        }
    }

    protected CircleVertexData getCircleData(V v) {
        return circleVertexDataMap.get(v);
    }

    @Override
    public void step() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean done() {
        return true;
    }

    protected static class CircleVertexData {

        private double angle;

        protected double getAngle() {
            return angle;
        }

        protected void setAngle(double angle) {
            this.angle = angle;
        }

        @Override
        public String toString() {
            return "CircleVertexData: angle=" + angle;
        }
    }
}
