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

import br.uff.ic.provviewer.Collapser;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import br.uff.ic.provviewer.Filter.PreFilters;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import java.util.Collection;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;

/**
 * Class to initialize the interface operations
 *
 * @author Kohwalter
 */
public class GuiInitialization {

    /**
     * Method to initialize the tool components
     *
     * @param variables
     * @param graph
     * @param graphFrame
     * @param Layouts
     * @param agentLabel interface check-box state agent label
     * @param activityLabel interface check-box state activity label
     * @param entityLabel interface check-box state for entity label
     * @param timeLabel interface check-box state for time label
     */
    public static void initGraphComponent(Variables variables, DirectedGraph<Object, Edge> graph,
            JFrame graphFrame, JComboBox Layouts, boolean agentLabel, boolean activityLabel, boolean entityLabel, boolean timeLabel, boolean showID) {
        variables.initConfig = true;
        variables.graph = graph;
        variables.collapsedGraph = variables.graph;
        GuiFunctions.SetView(variables, Layouts, graphFrame);
        variables.guiBackground.InitBackground(variables, Layouts);
        GuiFunctions.MouseInteraction(variables);
        GuiTooltip.Tooltip(variables);
        GuiFunctions.VertexLabel(variables, agentLabel, activityLabel, entityLabel, timeLabel, showID);
        GuiFunctions.Stroke(variables);
        GuiFunctions.GraphPaint(variables);
        GuiFunctions.VertexShape(variables);
        InitFilters(variables);
        NormalizeTime(variables);
        
        ToolTipManager.sharedInstance().setInitialDelay(10);
        ToolTipManager.sharedInstance().setDismissDelay(50000);
        GraphFrame.vertexFilterList.setSelectedIndex(0);
    }

    /**
     * Method to initialize the tool's variables
     *
     * @param variables
     */
    public static void InitVariables(Variables variables) {
        variables.mouse = new DefaultModalGraphMouse();
        variables.filterCredits = false;
        variables = new Variables();
        variables.collapser = new Collapser();
        variables.filter = new Filters();
        variables.prologIsInitialized = false;
        variables.initLayout = true;
        variables.initConfig = false;
    }

    /**
     * Method to initialize the tool's filters
     *
     * @param variables
     */
    public static void InitFilters(Variables variables) {
        variables.filter.filteredGraph = variables.graph;
//        variables.filter.FilterInit();
        PreFilters.PreFilter();
        variables.filter.Filters(variables);
    }

    /**
     * Method to normalize vertex's timestamps to start from 0
     * @param variables 
     */
    public static void NormalizeTime(Variables variables) {
        Collection<Object> vertices = variables.graph.getVertices();
        double minTime = Double.POSITIVE_INFINITY;
        for (Object v : vertices) {
            if (((Vertex) v).getTime() != -1) {
                minTime = Math.min(minTime, ((Vertex) v).getTime());
            }
        }

        // Normalize time
        for (Object v : vertices) {
            if (((Vertex) v).getTime() >= 0) {
                ((Vertex) v).setNormalizedTime((((Vertex) v).getTime() - minTime));
            } else {
                ((Vertex) v).setNormalizedTime(-1);
            }
        }
    }

}
