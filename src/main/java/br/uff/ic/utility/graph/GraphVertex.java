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
package br.uff.ic.utility.graph;

import br.uff.ic.utility.GraphAttribute;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Color;
import java.awt.Paint;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphVertex extends Vertex{
    public Graph clusterGraph;
    public GraphVertex(Map<String, String> id, Map<String, GraphAttribute> attributes, Graph v) {
        super(id.keySet().toString(), attributes.get("Label").getValue(), attributes.get("Timestamp").getAverageValue(), attributes);
        clusterGraph = v;
    }

    @Override
    public Paint getColor() {
        return Color.PINK;
    }

    @Override
    public String getNodeType() {
        return "Summarized Vertex";
    }
    
}
