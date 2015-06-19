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
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Stroke.EdgeStroke;
import br.uff.ic.provviewer.Stroke.VertexStroke;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import br.uff.ic.provviewer.Vertex.VertexShape;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author Kohwalter
 */
public class GuiFunctions {
    
    public static void InitVariables(Variables variables)
    {
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
    
    public static void InitFilters(Variables variables)
    {
        variables.filter.filteredGraph = variables.graph;
        variables.filter.FilterInit();
        
        PreFilters.PreFilter();
        //Initialize selected filters from the GUI
        variables.collapser.Filters(variables, variables.filter);
    }
    
    /**
     * ================================================
     * Init Graph Component
     * ================================================
     */
    public static void initGraphComponent(Variables variables, DirectedGraph<Object, Edge> graph, JFrame graphFrame, JComboBox Layouts) {
        variables.initConfig = true;
        variables.graph = graph;
        variables.collapsedGraph = variables.graph;
        
        SetView(variables, Layouts, graphFrame); 
        GuiBackground.InitBackground(variables, Layouts);
        MouseInteraction(variables); 
        Tooltip(variables);
        VertexLabel(variables);
        Stroke(variables); 
        GraphPaint(variables);
        VertexShape(variables);
        InitFilters(variables);
    }

    public static void VertexShape(Variables variables) {
        variables.view.getRenderContext().setVertexShapeTransformer(new VertexShape());
    }
    
    public static void Stroke(final Variables variables)
    {
        /**
         * ================================================
         * Vertex Stroke
         * ================================================
         */
        Transformer<Object, Stroke> nodeStrokeTransformer =  new Transformer<Object, Stroke>() {
            @Override
            public Stroke transform(Object v) {
                return VertexStroke.VertexStroke(v, variables.view, variables.layout);
        }};
        variables.view.getRenderContext().setVertexStrokeTransformer(nodeStrokeTransformer);
        /**
         * ================================================
         * Edge Stroke
         * ================================================
         */
        variables.ComputeEdgeTypeValues(variables.graph);
        Transformer<Edge, Stroke> edgeStrokeTransformer =  new Transformer<Edge, Stroke>() {
            @Override
            public Stroke transform(Edge e) {
                return EdgeStroke.StrokeByType(e, variables);
            }
        };
        variables.view.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
    }
    
    public static void VertexLabel(Variables variables)
    {
        variables.view.getRenderContext().setVertexLabelTransformer(new Transformer<Object, String>() {

                @Override
                public String transform(Object v) {
                    if(v instanceof Graph) {
                        for(Object vertex : ((Graph)v).getVertices())
                        {
                            if(vertex instanceof AgentVertex) {
                                return "<html><font size=\"8\">" + ((Vertex)vertex).getLabel();
                            }
                        }    
                    }
                    if(v instanceof AgentVertex) {
                                return "<html><font size=\"8\">" + ((Vertex)v).getLabel();
                            }
                    else if((v instanceof EntityVertex) && Config.showEntityLabel && Config.showEntityDate) {
                        return "<html><font size=\"8\">" + String.valueOf((int)((Vertex)v).getDate()) + " : " + ((Vertex)v).getLabel();
                    }
                    else if((v instanceof EntityVertex) && Config.showEntityDate) {
                        return "<html><font size=\"8\">" + String.valueOf((int)((Vertex)v).getDate());
                    }
                    else if((v instanceof EntityVertex) && Config.showEntityLabel) {
                        return "<html><font size=\"8\">" + ((Vertex)v).getLabel();
                    }
                    return "";
                }
            });
    }
    
    public static void Tooltip(Variables variables)
    {
        /**
         * ================================================
         * Add a listener for ToolTips
         * ================================================
         */
        variables.view.setVertexToolTipTransformer(new ToStringLabeller() {
            @Override
            public String transform(Object v) {
                    if(v instanceof Graph) {
                            return ("<html>" + ((Graph)v).getVertices().toString() + "</html>");
                    }
                    return ("<html>" + v.toString() + "</html>");
            }});
         /**
         * ================================================
         * Edge Tooltip
         * ================================================
         */
        variables.view.setEdgeToolTipTransformer(new Transformer<Edge,String>(){
        @Override
            public String transform(Edge n) 
            {
                return n.getEdgeInfluence();
            }
        });
    }
    
    public static void MouseInteraction(Variables variables)
    {
        /**
         * ================================================
         * Adding interaction via mouse
         * Commands: t for translate, p for picking
         * ================================================
         */
        variables.view.setGraphMouse(variables.mouse);
        variables.view.addKeyListener(variables.mouse.getModeKeyListener());
    }
    
    /**
     * Pan the camera to the first vertex in the graph
     */
    public static void PanCameraToFirstVertex(Variables variables)
    {
        Vertex first = (Vertex) variables.graph.getVertices().iterator().next();    
        variables.view.getGraphLayout();
        Point2D q = variables.view.getGraphLayout().transform(first);
        Point2D lvc = 
            variables.view.getRenderContext().getMultiLayerTransformer().inverseTransform(variables.view.getCenter());
        final double dx = (lvc.getX() - q.getX());
        final double dy = (lvc.getY() - q.getY());
        variables.view.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
    }
    /**
     * Scale back the zoom in the camera
     */
    public static void ScaleView(Variables variables)
    {
        variables.view = new VisualizationViewer<Object, Edge>(variables.layout);
        final ScalingControl scaler = new CrossoverScalingControl();
        scaler.scale(variables.view, 1/2.1f, variables.view.getCenter());
    }
    
    public static void SetView(final Variables variables, JComboBox Layouts, JFrame graphFrame)
    {
        /**
         * ================================================
         * Choosing layout
         * ================================================
         */
        if(variables.initLayout)
        {
            variables.config.Initialize();
            variables.layout = new Temporal_Layout<Object, Edge>(variables.graph);
            variables.view = new VisualizationViewer<Object, Edge>(variables.layout);
            Layouts.setSelectedItem("SpatialLayout");
            variables.initLayout = false;
        }
        
        ScaleView(variables);
        PanCameraToFirstVertex(variables);
        
        variables.gCollapser = new GraphCollapser(variables.graph);
        
        final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction.getInstance();
        // ================================================
        //        final Set exclusions = new HashSet();
        //testing for edge collapse
        eif.setPredicate(new Predicate() {
            @Override
            public boolean evaluate(Object e) {

                    return variables.exclusions.contains(e);
            }});
        // ================================================
        variables.view.getRenderContext().setParallelEdgeIndexFunction(eif);
        
        variables.view.setBackground(Color.white);
        graphFrame.getContentPane().add(variables.view, BorderLayout.CENTER);
    }

    public static void GraphPaint(Variables variables)
    {
        /**
         * ================================================
         * Vertex Paint
         * ================================================
         */
        VertexPainter.VertexPainter("Default", variables.view, variables);
        /**
         * ================================================
         * Edge Paint
         * ================================================
         */
        Transformer edgePainter = new Transformer<Edge,Paint>() {
            @Override
            public Paint transform(Edge edge) {
                return edge.getColor();
            }
        };  
        variables.view.getRenderContext().setEdgeDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowFillPaintTransformer(edgePainter);
    }
}