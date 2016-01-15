/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Stroke.EdgeStroke;
import br.uff.ic.provviewer.Stroke.VertexStroke;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import br.uff.ic.provviewer.Vertex.VertexShape;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
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
 * Class responsible for main (GUI) graph functions
 * @author Kohwalter
 */
public class GuiFunctions {

    /**
     * Method to define the vertex shape
     * @param variables
     */
    public static void VertexShape(Variables variables) {
        variables.view.getRenderContext().setVertexShapeTransformer(new VertexShape());
    }

    /**
     * Method to define the vertex and edge borders/stroke
     * @param variables 
     */
    public static void Stroke(final Variables variables) {
        // Vertex Stroke
        Transformer<Object, Stroke> nodeStrokeTransformer = new Transformer<Object, Stroke>() {
            @Override
            public Stroke transform(Object v) {
                return VertexStroke.VertexStroke(v, variables.view, variables.layout, variables);
            }
        };
        variables.view.getRenderContext().setVertexStrokeTransformer(nodeStrokeTransformer);
        
        // Edge Stroke
        variables.ComputeEdgeTypeValues(variables, variables.graph);
        Transformer<Edge, Stroke> edgeStrokeTransformer = new Transformer<Edge, Stroke>() {
            @Override
            public Stroke transform(Edge e) {
                return EdgeStroke.StrokeByType(e, variables);
            }
        };
        variables.view.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
    }

    /**
     * Method to display labels for vertices
     * @param variables 
     */
    public static void VertexLabel(final Variables variables) {
        variables.view.getRenderContext().setVertexLabelTransformer(new Transformer<Object, String>() {

            @Override
            public String transform(Object v) {
                if (v instanceof Graph) {
                    for (Object vertex : ((Graph) v).getVertices()) {
                        if (vertex instanceof AgentVertex) {
                            return "<html><font size=\"8\">" + ((Vertex) vertex).getLabel();
                        }
                    }
                }
                if (v instanceof AgentVertex) {
                    return "<html><font size=\"8\">" + ((Vertex) v).getLabel();
                } else if ((v instanceof EntityVertex) && variables.config.showEntityLabel && variables.config.showEntityDate) {
                    return "<html><font size=\"8\">" + String.valueOf((int) ((Vertex) v).getTime()) + " : " + ((Vertex) v).getLabel();
                } else if ((v instanceof EntityVertex) && variables.config.showEntityDate) {
                    return "<html><font size=\"8\">" + String.valueOf((int) ((Vertex) v).getTime());
                } else if ((v instanceof EntityVertex) && variables.config.showEntityLabel) {
                    return "<html><font size=\"8\">" + ((Vertex) v).getLabel();
                }
                return "";
            }
        });
    }

    /**
     * Method to enable mouse interactions
     * @param variables 
     */
    public static void MouseInteraction(Variables variables) {
        // via mouse Commands: t for translate, p for picking
        variables.view.setGraphMouse(variables.mouse);
        variables.view.addKeyListener(variables.mouse.getModeKeyListener());
    }

    /**
     * Method to pan the camera to the first vertex in the graph
     * @param variables 
     */
    public static void PanCameraToFirstVertex(Variables variables) {
        Vertex first = (Vertex) variables.graph.getVertices().iterator().next();
        variables.view.getGraphLayout();
        Point2D q = variables.view.getGraphLayout().transform(first);
        Point2D lvc
                = variables.view.getRenderContext().getMultiLayerTransformer().inverseTransform(variables.view.getCenter());
        final double dx = (lvc.getX() - q.getX());
        final double dy = (lvc.getY() - q.getY());
        variables.view.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
    }

    /**
     * Method to scale back the camera zoom
     * @param variables 
     */
    public static void ScaleView(Variables variables) {
        variables.view = new VisualizationViewer<Object, Edge>(variables.layout);
        final ScalingControl scaler = new CrossoverScalingControl();
        scaler.scale(variables.view, 1 / 2.1f, variables.view.getCenter());
    }

    /**
     * Method to initialize the View
     * @param variables
     * @param Layouts is the GUI layout chooser
     * @param graphFrame is the tool's main frame
     */
    public static void SetView(final Variables variables, JComboBox Layouts, JFrame graphFrame) {
        // Choosing layout
        if (variables.initLayout) {
            variables.config.Initialize(variables);
            variables.layout = new Temporal_Layout<Object, Edge>(variables.graph, variables);
            variables.view = new VisualizationViewer<Object, Edge>(variables.layout);
            Layouts.setSelectedItem(variables.config.defaultLayout);
            variables.initLayout = false;
        }

        ScaleView(variables);
        PanCameraToFirstVertex(variables);

        variables.gCollapser = new GraphCollapser(variables.graph);

        final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction.getInstance();
        eif.setPredicate(new Predicate() {
            @Override
            public boolean evaluate(Object e) {

                return variables.exclusions.contains(e);
            }
        });
        variables.view.getRenderContext().setParallelEdgeIndexFunction(eif);

        variables.view.setBackground(Color.white);
        graphFrame.getContentPane().add(variables.view, BorderLayout.CENTER);
    }

    /**
     * Method to paint vertices and edges according to their values
     * @param variables 
     */
    public static void GraphPaint(final Variables variables) {

        // Vertex Paint
        VertexPainter.VertexPainter("Default", variables.view, variables);
        
         // Edge Paint
        Transformer edgePainter = new Transformer<Edge, Paint>() {
            @Override
            public Paint transform(Edge edge) {
                return edge.getColor(variables);
            }
        };
        variables.view.getRenderContext().setEdgeDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowFillPaintTransformer(edgePainter);
    }
}
