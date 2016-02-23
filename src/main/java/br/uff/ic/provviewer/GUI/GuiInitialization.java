/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.Collapser;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import br.uff.ic.provviewer.Filter.PreFilters;
import br.uff.ic.provviewer.Inference.PrologInference;
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
            JFrame graphFrame, JComboBox Layouts, boolean agentLabel, boolean activityLabel, boolean entityLabel, boolean timeLabel) {
        variables.initConfig = true;
        variables.graph = graph;
        variables.collapsedGraph = variables.graph;
        GuiFunctions.SetView(variables, Layouts, graphFrame);
        variables.guiBackground.InitBackground(variables, Layouts);
        GuiFunctions.MouseInteraction(variables);
        GuiTooltip.Tooltip(variables);
        GuiFunctions.VertexLabel(variables, agentLabel, activityLabel, entityLabel, timeLabel);
        GuiFunctions.Stroke(variables);
        GuiFunctions.GraphPaint(variables);
        GuiFunctions.VertexShape(variables);
        InitFilters(variables);
        NormalizeTime(variables);
        
        ToolTipManager.sharedInstance().setInitialDelay(10);
        ToolTipManager.sharedInstance().setDismissDelay(50000);
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
        variables.testProlog = new PrologInference();
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
            if (((Vertex) v).getTime() != -1) {
                ((Vertex) v).setNormalizedTime((((Vertex) v).getTime() - minTime) + 1);
            } else {
                ((Vertex) v).setNormalizedTime(-1);
            }
        }
    }

}
