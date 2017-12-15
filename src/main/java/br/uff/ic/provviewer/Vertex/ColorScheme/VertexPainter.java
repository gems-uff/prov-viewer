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

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Paint;
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
        Transformer vertexPainter = new Transformer<Object, Paint>() {
            @Override
            public Paint transform(Object v) {
                int j = 0;
                ColorScheme vm = variables.config.vertexModes.get(mode);
                return vm.Execute(v, variables);
            }
        };
        view.getRenderContext().setVertexFillPaintTransformer(vertexPainter);
    }
}
