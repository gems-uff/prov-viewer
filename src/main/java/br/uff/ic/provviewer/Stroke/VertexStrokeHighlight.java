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

package br.uff.ic.provviewer.Stroke;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.BasicStroke;
import java.awt.Stroke;
import org.apache.commons.collections15.Transformer;

/**
 * Class to highlight selected vertex's neighbors
 *
 * @author Kohwalter
 * @deprecated This function is also done at VertexStroke
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class VertexStrokeHighlight<V, E> implements
        Transformer<V, Stroke> {

    /**
     * Variable
     */
    protected boolean highlight = false;
    /**
     * Variable
     */
    protected Stroke heavy = new BasicStroke(5);
    /**
     * Variable
     */
    protected Stroke medium = new BasicStroke(3);
    /**
     * Variable
     */
    protected Stroke light = new BasicStroke(1);
    /**
     * Variable
     */
    protected PickedInfo<V> pi;
    /**
     * Variable
     */
    protected Graph<V, E> graph;

    /**
     * Constructor
     *
     * @param graph
     * @param pi
     */
    public VertexStrokeHighlight(Graph<V, E> graph, PickedInfo<V> pi) {
        this.graph = graph;
        this.pi = pi;
    }

    /**
     * Set highlight
     *
     * @param highlight
     */
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    /**
     * Method to mark the vertex neighbors by changing their border width
     *
     * @param v Vertex
     * @return Stroke
     */
    @Override
    public Stroke transform(V v) {
        if (highlight) {
            if (pi.isPicked(v)) {
                return heavy;
            } else {
                for (V w : graph.getNeighbors(v)) {
//                    for (Iterator iter = graph.getNeighbors(v)v.getNeighbors().iterator(); iter.hasNext(); )
//                    {
//                        Vertex w = (Vertex)iter.next();
                    if (pi.isPicked(w)) {
                        return medium;
                    }
                }
                return light;
            }
        } else {
            return light;
        }
    }
}
