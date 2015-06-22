/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.Collapser;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import br.uff.ic.provviewer.Filter.PreFilters;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author Kohwalter
 */
public class GuiInitialization {

    /**
     *  Init Graph Component
     * 
     * @param variables
     * @param graph
     * @param graphFrame
     * @param Layouts
     */
    public static void initGraphComponent(Variables variables, DirectedGraph<Object, Edge> graph, JFrame graphFrame, JComboBox Layouts) {
        variables.initConfig = true;
        variables.graph = graph;
        variables.collapsedGraph = variables.graph;
        GuiFunctions.SetView(variables, Layouts, graphFrame);
        GuiBackground.InitBackground(variables, Layouts);
        GuiFunctions.MouseInteraction(variables);
        GuiTooltip.Tooltip(variables);
        GuiFunctions.VertexLabel(variables);
        GuiFunctions.Stroke(variables);
        GuiFunctions.GraphPaint(variables);
        GuiFunctions.VertexShape(variables);
        InitFilters(variables);
    }

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

    public static void InitFilters(Variables variables) {
        variables.filter.filteredGraph = variables.graph;
        variables.filter.FilterInit();
        PreFilters.PreFilter();
        variables.collapser.Filters(variables, variables.filter);
    }
    
}
