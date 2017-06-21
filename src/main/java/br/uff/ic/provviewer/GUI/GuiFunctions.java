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

import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.provviewer.GraphFrame;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Stroke.EdgeStroke;
import br.uff.ic.provviewer.Stroke.VertexStroke;
import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import br.uff.ic.provviewer.Vertex.VertexShape;
import br.uff.ic.utility.EdgeSourceTarget;
import br.uff.ic.utility.GraphUtils;
import br.uff.ic.utility.StackElementUndoDeletion;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

/**
 * Class responsible for main (GUI) graph functions
 *
 * @author Kohwalter
 */
public class GuiFunctions {

    /**
     * Method to define the vertex shape using the default shapes
     *
     * @param variables
     */
    public static void VertexShape(Variables variables) {
        variables.view.getRenderContext().setVertexShapeTransformer(new VertexShape(variables.config.vertexSize, variables));
    }

    /**
     * Method to define the Vertex Shape
     * @param variables
     * @param selectedMode
     * @param attribute 
     */
    public static void VertexShape(Variables variables, String selectedMode, String attribute) {
        variables.view.getRenderContext().setVertexShapeTransformer(new VertexShape(variables.config.vertexSize, selectedMode, attribute, variables.graph.getVertices(), variables));
        variables.view.repaint();
    }

    /**
     * Method to define the vertex and edge borders/stroke
     *
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

        // Change Stroke color
        Transformer<Object, Paint> drawPaint = new Transformer<Object, Paint>() {
            @Override
            public Paint transform(Object v) {
                if (v instanceof Vertex) {
                    if (variables.highlightVertexOutliers) {
                        float value = ((Vertex) v).getAttributeValueFloat(variables.outliersThresholds.attributeName);
                        if (value < variables.outliersThresholds.lowerThreshold || value > variables.outliersThresholds.upperThreshold) {
                            return new Color(255, 0, 0);
                        }
                    }
                    if (variables.vertexBorderByGraphs) {
                        String graphs = ((Vertex) v).getAttributeValue(VariableNames.GraphFile);
                        int i = 0;
                        if (((Vertex) v).getAttributeValues(VariableNames.GraphFile).length == variables.numberOfGraphs) {
                            return Color.LIGHT_GRAY;
                        }
                        for (Object g : variables.graphNames.toArray()) {
                            if (graphs.contains((String) g)) {
                                break;
                            }
                            i++;
                        }
                        return Utils.getColor(i);
                    }
                }
//                if(v instanceof Vertex) {
//                    float value = ((Vertex) v).getAttributeValueFloat("Cluster");
//                    if( value != value)
//                        return new Color(0, 0, 0);
//                    else
//                        return Utils.getColor((int) ((Vertex) v).getAttributeValueFloat("Cluster"));
//                }
                return Color.BLACK;
            }
        };
        variables.view.getRenderContext().setVertexDrawPaintTransformer(drawPaint);

        // Edge Stroke
        variables.ComputeEdgeTypeValues();
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
     *
     * @param variables
     * @param agentLabel interface check-box state agent label
     * @param activityLabel interface check-box state activity label
     * @param entityLabel interface check-box state for entity label
     * @param timeLabel interface check-box state for time label
     * @param showID interface check-box state for ID label
     */
    public static void VertexLabel(final Variables variables, final boolean agentLabel, final boolean activityLabel, final boolean entityLabel, final boolean timeLabel, final boolean showID) {
        variables.view.getRenderContext().setVertexLabelTransformer(new Transformer<Object, String>() {

            @Override
            public String transform(Object v) {
                String font = VariableNames.FontConfiguration;
                // Agent
                if ((v instanceof AgentVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexAgentAttribute)) && agentLabel) {
                    return font + ((Vertex) v).getLabel();
                } // Entity
                // Label + Time
                else if ((v instanceof EntityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) && entityLabel && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime()) + " : " + ((Vertex) v).getLabel();
                } // Time
                else if ((v instanceof EntityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime());
                } // Label
                else if ((v instanceof EntityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) && entityLabel) {
                    return font + ((Vertex) v).getLabel();
                } // Activity
                // Label + Time
                else if ((v instanceof ActivityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) && activityLabel && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime()) + " : " + ((Vertex) v).getLabel();
                } // Time
                else if ((v instanceof ActivityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime());
                } // Label
                else if ((v instanceof ActivityVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) && activityLabel) {
                    return font + ((Vertex) v).getLabel();
                } else if (showID) {
                    return font + ((Vertex) v).getID();
                }

                return "";
            }
        });
    }

    /**
     * Method to enable mouse interactions
     *
     * @param variables
     */
    public static void MouseInteraction(Variables variables) {
        // via mouse Commands: t for translate, p for picking
        variables.view.setGraphMouse(variables.mouse);
        variables.view.addKeyListener(variables.mouse.getModeKeyListener());
    }

    /**
     * Method to pan the camera to the first vertex in the graph
     *
     * @param variables
     */
    public static void PanCameraToFirstVertex(Variables variables) {
        Vertex first = (Vertex) variables.layout.getGraph().getVertices().iterator().next();
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
     *
     * @param variables
     */
    public static void ScaleView(Variables variables) {
//        variables.view = new VisualizationViewer<>(variables.layout);
        final ScalingControl scaler = new CrossoverScalingControl();
        scaler.scale(variables.view, 1 / 2.1f, variables.view.getCenter());
    }

    /**
     * Method to initialize the View
     *
     * @param variables
     * @param Layouts is the GUI layout chooser
     * @param graphFrame is the tool's main frame
     */
    public static void SetView(final Variables variables, JComboBox Layouts, JFrame graphFrame) {
        // Choosing layout
        if (variables.initLayout) {
            variables.config.Initialize(variables);
            variables.newLayout(variables.config.defaultLayout);
//            variables.layout = new Timeline_Layout<>(variables.graph, variables);
            variables.view = new VisualizationViewer<>(variables.layout);
//            Layouts.setSelectedItem(variables.config.defaultLayout);
            variables.initLayout = false;
        }

        ScaleView(variables);
        PanCameraToFirstVertex(variables);

        variables.initGraphCollapser();

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
     *
     * @param variables
     */
    public static void GraphPaint(final Variables variables) {

        // Vertex Paint
        VertexPainter.VertexPainter(VariableNames.DefaultVertexPainter, variables.view, variables);
        StatusFilterBox.setSelectedItem(VariableNames.DefaultVertexPainter);
        // Edge Paint
        Transformer edgePainter = new Transformer<Edge, Paint>() {
            @Override
            public Paint transform(Edge edge) {
                if (GraphFrame.useEdgeTypeColor.isSelected()) {
                    for (EdgeType e : variables.config.edgetype) {
                        if (e.type.equalsIgnoreCase(edge.getType())) {
                            return e.edgeColor;
                        }
                    }
                }
                
                // Highlight PATH
                PickedState<Edge> picked_edge_state = variables.view.getPickedEdgeState();
                if (!picked_edge_state.getPicked().isEmpty()) {
                    for (Edge e : picked_edge_state.getPicked()) {
                        if(edge.getID().equalsIgnoreCase(e.getID()))
                            return edge.getColor(variables);
                    }
                    return new Color(edge.getColor(variables).getRed(), edge.getColor(variables).getGreen(), edge.getColor(variables).getBlue(), (int) (variables.edgeAlpha * 0.25));
                }
                
                // Highlight incident edges
                PickedState<Object> picked_state = variables.view.getPickedVertexState();
                if (!picked_state.getPicked().isEmpty()) {
                    for (Object v : picked_state.getPicked()) {
                        Pair endpoints = variables.layout.getGraph().getEndpoints(edge);
                        if (endpoints.getFirst().equals(v)) {
                            return edge.getColor(variables);
                        } else if (endpoints.getSecond().equals(v)) {
                            return edge.getColor(variables);
                        }
                    }
                    return new Color(edge.getColor(variables).getRed(), edge.getColor(variables).getGreen(), edge.getColor(variables).getBlue(), variables.edgeAlpha);
                }
                return edge.getColor(variables);
            }
        };
        variables.view.getRenderContext().setEdgeDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowDrawPaintTransformer(edgePainter);
        variables.view.getRenderContext().setArrowFillPaintTransformer(edgePainter);
    }
    
    /**
     * Method to delete the selected vertices
     * @param variables
     * @param picked is the list of vertices to be deleted
     */
    public static void DeleteVertices(Variables variables, Collection picked){
        StackElementUndoDeletion element = new StackElementUndoDeletion();
        DirectedGraph<Object, Edge> graph = new DirectedSparseMultigraph<>();
        // Clone the current displayed graph to the "graph" variable
        variables.filter.AddFilters(variables);
        // Add all vertices, even lonely ones since it will not be capture if there is no edge connecting it in the next step just as a precation
        for(Object v : variables.layout.getGraph().getVertices())
            graph.addVertex(v);
        
        for(Edge edge : variables.layout.getGraph().getEdges()) {
            Pair endpoints = variables.layout.getGraph().getEndpoints(edge);
            Object v1 = endpoints.getFirst();
            Object v2 = endpoints.getSecond();
            graph.addEdge(edge, v1, v2);
        }
        for (Object v : picked) {
            // Add each vertex to the stack element
            element.vertices.add(v);
            // Add all edges from that vertex in the stack element
            for(Edge e : graph.getIncidentEdges(v)) {
                Pair endpoints = graph.getEndpoints(e);
                Object v1 = endpoints.getFirst();
                Object v2 = endpoints.getSecond();
                EdgeSourceTarget est = new EdgeSourceTarget();
                est.edge = e;
                est.source = v1;
                est.target = v2;
                element.edges.add(est);
                
                // Need to insert new edges that connects A to C if the middle vertex was removed
            }
            graph.removeVertex(v);
        }
        // Add to the stack
        variables.undoDeletion.push(element);
        variables.layout.setGraph(graph);
        variables.view.getPickedVertexState().clear();
        
        variables.filter.filterHiddenEdges(variables.view, variables.layout);
//        variables.view.repaint();
    }
    
    /**
     * Method to undo the last deletion
     * @param variables 
     */
    public static void UndoLastDeletion(Variables variables) {
        if(!variables.undoDeletion.isEmpty()) {
            DirectedGraph<Object, Edge> graph = (DirectedGraph<Object, Edge>) variables.layout.getGraph();
            StackElementUndoDeletion element = variables.undoDeletion.pop();
            for(EdgeSourceTarget e : element.edges)
                graph.addEdge(e.edge, e.source, e.target);
            if(!element.insertedEdges.isEmpty()) {
                for(EdgeSourceTarget e : element.insertedEdges)
                    graph.removeEdge(e.edge);
            }
            variables.layout.setGraph(graph);
            variables.filter.filterHiddenEdges(variables.view, variables.layout);
//            variables.view.repaint();
        }
    }
    
    /**
     * Method to change the label of the selected vertices
     * @param newLabel is the new label
     * @param picked is the list of selected vertices that will have the new label
     */
    public static void RenameSelectedVertexLabel (String newLabel, Collection picked) {
        for(Object v : picked)
            ((Vertex)v).setLabel(newLabel);
    }
    
    /**
     * Method to create "Chronological" Edges linking the activities from each agent to create a sequence based on their timestamps
     * @param variables 
     */
    public static void AddChronologicalEdgesLinkingActivities(Variables variables) {
        Collection<Object> agents = new HashSet();
        Collection<Edge> edges = new HashSet();
        int id = 0;
        //Get the selected node
        for (Object z : variables.layout.getGraph().getVertices()) {
            if(z instanceof AgentVertex)
                agents.add(z);
        }
        
        for(Object node : agents) {
            if (variables.graph.getNeighbors(node) != null) {
                List<Object> neighbors = new ArrayList(variables.graph.getNeighbors(node));
                
                Collections.sort(neighbors, Utils.getVertexAttributeComparator(VariableNames.time));
                Object previousActivity = null;
                for(Object v : neighbors) {
                    if(v instanceof ActivityVertex) { 
                        if(previousActivity != null) {
                            Edge newEdge = new Edge("CE_" + id, VariableNames.ChronologicalEdge, previousActivity, v);
                            id++;
                            edges.add(newEdge);
                        }
                        previousActivity = v;
                    }
                }
            }
        }
        for(Edge e : edges) {
            variables.graph.addEdge(e, e.getSource(), e.getTarget());
        }
        variables.layout.setGraph(variables.graph);
        variables.view.repaint();
    }

    /**
     * Method to find a path between two selected vertices
     * @param variables 
     */
    public static void FindPath(Variables variables) {
        Vertex source;
        Vertex target;
        PickedState<Object> picked_state = variables.view.getPickedVertexState();
        if (!picked_state.getPicked().isEmpty() && picked_state.getPicked().size() > 1) {
            source = (Vertex) picked_state.getPicked().toArray()[0];
            target = (Vertex) picked_state.getPicked().toArray()[1];
            System.out.println("Source: " + source.getID());
            System.out.println("target: " + target.getID());
            Map<String, Edge> path = GraphUtils.BFS(source, target, variables.layout.getGraph());
            Collection<Edge> cleanedPath = CleanPath(path, source.getID(), target.getID());
            if(path != null) {
                float probability = 1;
                PickedState<Edge> picked_edge_state = variables.view.getPickedEdgeState();
                for(Edge e : cleanedPath) {
                    float x = GraphUtils.getSubPathProbability(variables.layout.getGraph(), e, variables.numberOfGraphs);
//                    System.out.print(e.getID() + "(" + x + ")" + " - > ");
                    probability = probability * x;
                    picked_edge_state.pick(e, true);
                }
                System.out.println();
                System.out.println("probability: " + probability);
                 
            }
        }
    }
    
    /**
     * Method to clean the path returned from BFS, removing the paths that did not reach the destination
     * This algorithm backtracks from the Target to the Source
     * @param path is the path returned by BFS
     * @param source is the ID of the source vertex
     * @param target is the ID of the target vertex
     * @return the shortest path
     */
    public static Collection<Edge> CleanPath(Map<String, Edge> path, String source, String target) {
        Collection<Edge> cleanedPath = new ArrayList<>();
        String destination = target;
        while(destination != source) {
            cleanedPath.add(path.get(destination));
            destination = ((Vertex)path.get(destination).getTarget()).getID();
        }
        return cleanedPath;
    }

}
