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
package br.uff.ic.provviewer.Vertex.ColorScheme;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.Paint;
import java.util.Iterator;
import org.apache.commons.collections15.Transformer;

/**
 * Class to paint vertex according to its type (subclass). It also paints
 * collapsed vertices (instance of graphs)
 *
 * @author Kohwalter
 */
public class VertexPainter {

    /**
     *
     * @param mode
     * @param view
     * @param variables
     */
    public static void VertexPainter(final String mode, VisualizationViewer<Object, Edge> view, final Variables variables) {
        variables.highlightVertexOutliers = true;
        variables.highlightOutliers(mode);
        if (mode.equals(VariableNames.MarkovLayout)) {
            computeMarkovInChainFromOrigin(variables);
        }
        Transformer vertexPainter = new Transformer<Object, Paint>() {
            @Override
            public Paint transform(Object v) {
//                ColorScheme vm = variables.config.vertexModes.get(mode);
                return variables.config.vertexModes.get(mode).Execute(v, variables);
            }
        };
        view.getRenderContext().setVertexFillPaintTransformer(vertexPainter);
    }

    public static void computeMarkovInChainFromOrigin(Variables variables) {
        variables.layout.getGraph().getVertices().stream().map((v) -> {
            ((Vertex) v).attributes.remove(VariableNames.MarkovInLayout);
            return v;
        }).forEachOrdered((v) -> {
            ((Vertex) v).attributes.remove(VariableNames.MarkovOutLayout);
        });
//        for(Object v : variables.layout.getGraph().getVertices()) {
//            ((Vertex)v).attributes.remove(VariableNames.MarkovInLayout);
//            ((Vertex)v).attributes.remove(VariableNames.MarkovOutLayout);
//        }

        PickedState<Object> picked_state = variables.view.getPickedVertexState();
        if (picked_state.getPicked().size() > 0) {
            Vertex source = (Vertex) picked_state.getPicked().toArray()[0];
            markovInChain(source, 1, 0, 10, variables);
            markovOutChain(source, 1, 0, 10, variables);
        }
    }

    public static void markovInChain(Vertex source, float markov, int currentDepth, int maxDepth, Variables variables) {
        if (source.hasAttribute(VariableNames.MarkovInLayout)) {
            if (Float.valueOf(source.getAttributeValue(VariableNames.MarkovInLayout)) < markov) {
                source.addAttribute(new GraphAttribute(VariableNames.MarkovInLayout, String.valueOf(markov), "ProvViewer"));
            }
        } else {
            source.addAttribute(new GraphAttribute(VariableNames.MarkovInLayout, String.valueOf(markov), "ProvViewer"));
        }
        if (currentDepth < maxDepth) {
            Iterator<Edge> i = variables.layout.getGraph().getInEdges(source).iterator();
            while (i.hasNext()) {
                Edge edge = (Edge) i.next();
                Vertex newSource = (Vertex) edge.getSource();
                float markovIn = Float.valueOf(edge.getAttributeValue(VariableNames.MarkovIn));
                markovInChain(newSource, markovIn * markov, currentDepth + 1, maxDepth, variables);
            }
        }
    }

    public static void markovOutChain(Vertex source, float markov, int currentDepth, int maxDepth, Variables variables) {
        if (source.hasAttribute(VariableNames.MarkovOutLayout)) {
            if (Float.valueOf(source.getAttributeValue(VariableNames.MarkovOutLayout)) < markov) {
                source.addAttribute(new GraphAttribute(VariableNames.MarkovOutLayout, String.valueOf(markov), "ProvViewer"));
            }
        } else {
            source.addAttribute(new GraphAttribute(VariableNames.MarkovOutLayout, String.valueOf(markov), "ProvViewer"));
        }
        if (currentDepth < maxDepth) {
            Iterator<Edge> i = variables.layout.getGraph().getOutEdges(source).iterator();
            while (i.hasNext()) {
                Edge edge = (Edge) i.next();
                if (!(edge.getTarget() instanceof AgentVertex)) {
                    Vertex newSource = (Vertex) edge.getTarget();
                    float markovOut = Float.valueOf(edge.getAttributeValue(VariableNames.MarkovOut));
                    markovOutChain(newSource, markovOut * markov, currentDepth + 1, maxDepth, variables);
                }
            }
        }
    }

}
