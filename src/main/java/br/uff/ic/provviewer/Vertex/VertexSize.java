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

import br.uff.ic.utility.graph.GraphVertex;
import org.apache.commons.collections15.Transformer;

/**
 * Class to define vertex size
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 */
public class VertexSize<V> implements Transformer<V, Integer> {

    int size;

    /**
     * Method for defining the vertex size
     *
     * @param size define the size of the vertex
     */
    public VertexSize(Integer size) {
        this.size = size;
    }

    /**
     *
     * @param v the vertex being analyzed
     * @return the vertex size. If the vertex is a graph (collapsed vertices)
     * then it will be twice bigger.
     */
    @Override
    public Integer transform(V v) {
        if (v instanceof GraphVertex) {
            return (size * 2);
        }
        return size;
    }
}
