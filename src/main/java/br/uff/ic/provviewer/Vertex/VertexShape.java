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

package br.uff.ic.provviewer.Vertex;

import br.uff.ic.utility.GraphUtils;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Class that defines each vertex type shape
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 */
public class VertexShape<V> extends EllipseVertexShapeTransformer<V> {

    int defaultSize = 15;
    public VertexShape() {
        setSizeTransformer(new VertexSize<V>(defaultSize));
    }

    public VertexShape(int vertexSize) {
        defaultSize = vertexSize;
        setSizeTransformer(new VertexSize<V>(vertexSize));
    }

    /**
     * Create the vertex shape using VertexShapeFactory<V> factory;
     *
     * @param v JUNG's V (Vertex) type
     * @return Shape
     */
    @Override
    public Shape transform(V v) {
        if (v instanceof Graph) {
            int graphSize = GraphUtils.getCollapsedVertexSize(v);
            Object vertex;
            vertex = GraphUtils.hasAgentVertex(v);      

            v = (V) vertex;
            setSizeTransformer(new VertexSize<V>(defaultSize + graphSize));
        }
        else
            setSizeTransformer(new VertexSize<V>(defaultSize));
        
        if (v instanceof EntityVertex) {
            return new Ellipse2D.Float(-7, -7, defaultSize, defaultSize);
        }
        if (v instanceof AgentVertex) {
            return factory.getRegularPolygon(v, 5);
        } else//activity vertex 
        {
            return factory.getRegularPolygon(v, 4);
        }
    }
}
