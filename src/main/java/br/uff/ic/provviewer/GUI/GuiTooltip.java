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

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GraphUtils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.GraphVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections15.Transformer;

/**
 * Class responsible for managing the tooltip system from Prov Viewer
 * @author Kohwalter
 */
public class GuiTooltip {
//    static int agents = 0;
//    static int activities = 0;
//    static int entities = 0;
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
                if (v instanceof Graph) {
                    return "<html>" + GraphTooltip(v) + "</html>";
                }
                ((Vertex) v).setTimeScalePrint(variables.config.timeScale, variables.selectedTimeScale);
                return "<html>" + v.toString() + "</html>";
            }
        });
        
        // Edges Tooltips
        variables.view.setEdgeToolTipTransformer(new Transformer<Edge, String>() {
            @Override
            public String transform(Edge n) {
                return "<html><font size=\"4\">" + n.getEdgeTooltip() + "</html>";
            }
        });
    }

    /**
     * Tooltip generator for collapsed vertices, also known as Graph vertices or Composite vertices
     * @param v represents the targeted vertex for the tooltip 
     * @return a string that is the tooltip
     */
    public static String GraphTooltip(Object v) {
        return GraphUtils.CreateVertexGraph(v).toString();
//        return PrintVertexGraph(v, ids, labels, times, attributes);
    }
    
    /**
     * Method to print the Graph-Vertex
     * This is no longer used
     * @deprecated 
     * @param v
     * @param ids
     * @param labels
     * @param times
     * @param attributes
     * @return 
     */
    public static String PrintVertexGraph(Object v, 
            Map<String, String> ids,
            Map<String, String> labels,
            Map<String, String> times,
            Map<String, GraphAttribute> attributes){
        String nodeTypes = "";
        int agents = 0;
        int activities = 0;
        int entities = 0;
        
        if (agents > 0) {
            nodeTypes = "Agents: " + agents + "<br>";
        }
        if (activities > 0) {
            nodeTypes += "Activities: " + activities + "<br>";
        }
        if (entities > 0) {
            nodeTypes += "Entities: " + entities + "<br>";
        }

        return "<b>Summarized Vertex" + "</b>" + "<br>" + nodeTypes
                + "<br>IDs: " + ids.values().toString() + "<br>"
                + "<b>Labels: " + labels.values().toString() + "</b>"
                + " <br>" + "Original Timestamps: " + times.values().toString()
                + " <br>" + PrintAttributes(attributes);
    }

    
    
    /**
     * Prints the tooltip attributes
     * @param attributes is the list of all attributes
     * @return a string that represents all attributes and their respective values
     */
    public static String PrintAttributes(Map<String, GraphAttribute> attributes) {
        String attributeList = "";
        if (!attributes.values().isEmpty()) {
            for (GraphAttribute att : attributes.values()) {
                attributeList += att.printAttribute();
            }
        }
        return attributeList;
    }
}
