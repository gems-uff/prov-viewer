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

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GraphUtils;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.GraphVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Collection;

/**
 * Class that defines each vertex type shape
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 */
public class VertexShape<V> extends EllipseVertexShapeTransformer<V> {

    int defaultSize = 15;
    String attribute = "Timestamp";
    String selectedShape = "Prov";
    int max;
    Variables variables;

    public VertexShape(int vertexSize, Variables v) {
        defaultSize = vertexSize;
        setSizeTransformer(new VertexSize<V>(vertexSize));
        variables = v;
    }
    
    public VertexShape(int vertexSize, String selectedMode, String att, Collection<Object> vertices, Variables v) {
        defaultSize = vertexSize;
        selectedShape = selectedMode;
        setSizeTransformer(new VertexSize<V>(vertexSize));
        attribute = att;
        max = (int) Utils.findMaximumAttributeValue(vertices, attribute);
        variables = v;
    }

    /**
     * Create the vertex shape using VertexShapeFactory factory;
     *
     * @param v JUNG's V (Vertex) type
     * @return Shape
     */
    @Override
    public Shape transform(V v) {
        switch (selectedShape) {
            case "Prov":
                return defaultShape(v);
            case "Graphs":
                return multipleGraphShape(v);
            case "Attribute":
                return attributeValueShape(v);
            default:
                return defaultShape(v);
        }
    }

    /**
     * Method that defines the default vertex shape
     *
     * @param v is the vertex
     * @return the vertex shape
     */
    private Shape defaultShape(V v) {
        if (v instanceof GraphVertex) {
            vertexGraphSizeTransformer(v);
        } else {
            setSizeTransformer(new VertexSize<V>(defaultSize));
        }

        return provShape(v, defaultSize);
    }

    /**
     * Method that defines the prov shapes for the vertices
     *
     * @param v is the vertex
     * @param size is the size that we want for the vertex
     * @return the vertex shape
     */
    private Shape provShape(V v, int size) {
        if (v instanceof AgentVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexAgentAttribute)) {
            return factory.getRegularPolygon(v, 5);
        } else if (v instanceof ActivityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) {{//activity vertex
            return factory.getRegularPolygon(v, 4);
        }
        } else if (v instanceof EntityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) {
            return new Ellipse2D.Float(-7, -7, size, size);
        } else // Unknown
            return factory.getRegularPolygon(v, 3);
    }

    /**
     * Default Method for setting the vertex-graph size transformer
     *
     * @param v is the graph vertex
     */
    private void vertexGraphSizeTransformer(V v) {
        int graphSize = GraphUtils.getCollapsedVertexSize(v);
        float gsize = defaultSize * (1 + 2 * (graphSize - 1) / variables.numberOfGraphs);
        setSizeTransformer(new VertexSize<V>(defaultSize + graphSize));
    }

    /**
     * Method to define the vertex size based on the number of graphs it belongs
     *
     * @param v is the vertex
     * @return the vertex shape
     */
    private Shape multipleGraphShape(V v) {

        int numberOfGraphs;
        int vertexSize;
        String[] graphs = ((Vertex) v).getAttributeValues(VariableNames.GraphFile);
        numberOfGraphs = graphs.length;
        vertexSize = (int) (defaultSize * numberOfGraphs * 0.5);
        setSizeTransformer(new VertexSize<V>(vertexSize));
        return provShape(v, vertexSize);
    }
    
    private Shape attributeValueShape(V v) {

        double value;
        int vertexSize;
        value = ((Vertex)v).getAttributeValueFloat(attribute);
        vertexSize = (int) (defaultSize * 2 * value / max);
        setSizeTransformer(new VertexSize<V>(vertexSize));
        return provShape(v, vertexSize);
    }
}
