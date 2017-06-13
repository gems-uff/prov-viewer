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
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.util.Map;
import org.apache.commons.collections15.Transformer;

/**
 * Class responsible for managing the tooltip system from Prov Viewer
 * @author Kohwalter
 */
public class GuiTooltip {
    Map<String, GraphAttribute> attributes;

    /**
     * Method to generate vertex tooltips in the interface
     * Called when initializing the application's components (GuiInitialization)
     * @param variables is the global variables from Prov Viewer
     */
    public static void Tooltip(final Variables variables) {
        // Vertex Tooltips
        variables.view.setVertexToolTipTransformer(new ToStringLabeller() {
            @Override
            public String transform(Object v) {
                ((Vertex) v).setTimeScalePrint(variables.config.timeScale, variables.selectedTimeScale);
                return "<html>" + v.toString() + "</html>";
            }
        });
        
        // Edges Tooltips
        variables.view.setEdgeToolTipTransformer(new Transformer<Edge, String>() {
            @Override
            public String transform(Edge n) {
                return VariableNames.EdgeTooltipFontConfiguration + n.getEdgeTooltip(variables.numberOfGraphs) + "</html>";
            }
        });
    }
}
