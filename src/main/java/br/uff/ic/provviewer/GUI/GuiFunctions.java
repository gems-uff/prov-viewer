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

import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Path.GraphPath;
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
import br.uff.ic.utility.AttValueColor;
import br.uff.ic.utility.EdgeSourceTarget;
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
import java.util.HashMap;
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
     *
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
                        double value = ((Vertex) v).getAttributeValueDouble(variables.outliersThresholds.attributeName);
                        if (value < variables.outliersThresholds.lowerThreshold || value > variables.outliersThresholds.upperThreshold) {
                            return new Color(255, 0, 0);
                        }
                    }
                    else if (variables.vertexBorderByGraphs) {
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
                    else if (variables.vertexBorderByLabel) {
                        List<AttValueColor> avc = variables.config.activityVC;
                        for (int i = 0; i < avc.size(); i++) {
                            if (((Vertex)v).getLabel().equalsIgnoreCase(avc.get(i).value)) {
                                return avc.get(i).color;
                            }
                        }
                        return Color.BLACK;
                    }
                    else {
                        return Color.BLACK;
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
                if ((v instanceof AgentVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexAgentAttribute)) && agentLabel) {
                    return font + ((Vertex) v).getLabel();
                } // Entity
                // Label + Time
                else if ((v instanceof EntityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) && entityLabel && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime()) + " : " + ((Vertex) v).getLabel();
                } // Time
                else if ((v instanceof EntityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime());
                } // Label
                else if ((v instanceof EntityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexEntityAttribute)) && entityLabel) {
                    return font + ((Vertex) v).getLabel();
                } // Activity
                // Label + Time
                else if ((v instanceof ActivityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) && activityLabel && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime()) + " : " + ((Vertex) v).getLabel();
                } // Time
                else if ((v instanceof ActivityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) && timeLabel) {
                    return font + String.valueOf((int) ((Vertex) v).getTime());
                } // Label
                else if ((v instanceof ActivityVertex || ((Vertex) v).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) && activityLabel) {
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
//                if (GraphFrame.isEdgeColorByType.isSelected()) {
//                    if (variables.config.edgeTypes.containsKey(edge.getLabel())) {
//                        return variables.config.edgeTypes.get(edge.getLabel()).edgeColor;
//                    }
//                }

                // Highlight PATH
                PickedState<Edge> picked_edge_state = variables.view.getPickedEdgeState();
                if (!picked_edge_state.getPicked().isEmpty()) {
                    for (Edge e : picked_edge_state.getPicked()) {
                        if (edge.getID().equalsIgnoreCase(e.getID())) {
                            return edge.getColor(variables);
                        }
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
     *
     * @param variables
     * @param picked is the list of vertices to be deleted
     */
    public static void DeleteVertices(Variables variables, Collection picked) {
        StackElementUndoDeletion element = new StackElementUndoDeletion();
        DirectedGraph<Object, Edge> graph = new DirectedSparseMultigraph<>();
        // Clone the current displayed graph to the "graph" variable
        variables.filter.AddFilters(variables);
        // Add all vertices, even lonely ones since it will not be capture if there is no edge connecting it in the next step just as a precation
        for (Object v : variables.layout.getGraph().getVertices()) {
            graph.addVertex(v);
        }

        for (Edge edge : variables.layout.getGraph().getEdges()) {
            Pair endpoints = variables.layout.getGraph().getEndpoints(edge);
            Object v1 = endpoints.getFirst();
            Object v2 = endpoints.getSecond();
            graph.addEdge(edge, v1, v2);
        }
        for (Object v : picked) {
            // Add each vertex to the stack element
            element.vertices.add(v);
            // Add all edges from that vertex in the stack element
            for (Edge e : graph.getIncidentEdges(v)) {
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
        variables.graph = graph;
        variables.collapsedGraph = graph;
        variables.view.getPickedVertexState().clear();

        variables.filter.filterHiddenEdges(variables.view, variables.layout);
//        variables.view.repaint();
    }

    /**
     * Method to undo the last deletion
     *
     * @param variables
     */
    public static void UndoLastDeletion(Variables variables) {
        if (!variables.undoDeletion.isEmpty()) {
            DirectedGraph<Object, Edge> graph = (DirectedGraph<Object, Edge>) variables.layout.getGraph();
            StackElementUndoDeletion element = variables.undoDeletion.pop();
            for (EdgeSourceTarget e : element.edges) {
                graph.addEdge(e.edge, e.source, e.target);
            }
            if (!element.insertedEdges.isEmpty()) {
                for (EdgeSourceTarget e : element.insertedEdges) {
                    graph.removeEdge(e.edge);
                }
            }
            variables.layout.setGraph(graph);
            variables.graph = graph;
            variables.collapsedGraph = graph;
            variables.filter.filterHiddenEdges(variables.view, variables.layout);
//            variables.view.repaint();
        }
    }

    /**
     * Method to change the label of the selected vertices
     *
     * @param newLabel is the new label
     * @param picked is the list of selected vertices that will have the new
     * label
     */
    public static void RenameSelectedVertexLabel(String newLabel, Collection picked) {
        for (Object v : picked) {
            ((Vertex) v).setLabel(newLabel);
        }
    }

    /**
     * Method to create "Chronological" Edges linking the activities from each
     * agent to create a sequence based on their timestamps
     *
     * @param variables
     */
    public static void AddChronologicalEdgesLinkingActivities(Variables variables) {
        Collection<Object> agents = new HashSet();
        Collection<Edge> edges = new HashSet();
        int id = 0;
        //Get the selected node
        for (Object z : variables.layout.getGraph().getVertices()) {
            if (z instanceof AgentVertex) {
                agents.add(z);
            }
        }

        for (Object node : agents) {
            if (variables.graph.getNeighbors(node) != null) {
                List<Object> neighbors = new ArrayList(variables.graph.getNeighbors(node));

                Collections.sort(neighbors, Utils.getVertexAttributeComparator(VariableNames.time));
                Object previousActivity = null;
                for (Object v : neighbors) {
                    if (v instanceof ActivityVertex) {
                        if (previousActivity != null) {
                            Edge newEdge = new Edge("CE_" + id, VariableNames.ChronologicalEdge, previousActivity, v);
                            id++;
                            edges.add(newEdge);
                        }
                        previousActivity = v;
                    }
                }
            }
        }
        for (Edge e : edges) {
            variables.graph.addEdge(e, e.getSource(), e.getTarget());
        }
        variables.layout.setGraph(variables.graph);
        variables.view.repaint();
    }

    public static void SearchVertexByID(String ID, Variables variables) {
        Vertex found = null;
        for (Object v : variables.layout.getGraph().getVertices()) {
            if (((Vertex) v).getID().equalsIgnoreCase(ID)) {
                found = (Vertex) v;
            }
        }
        variables.view.getGraphLayout();
        Point2D q = variables.view.getGraphLayout().transform(found);
        Point2D lvc
                = variables.view.getRenderContext().getMultiLayerTransformer().inverseTransform(variables.view.getCenter());
        final double dx = (lvc.getX() - q.getX());
        final double dy = (lvc.getY() - q.getY());
        variables.view.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
    }

    /**
     * Function that returns the support, confidence, and lift of the selected
     * pattern based on trials that worked
     *
     * @param variables
     * @param correctTrials is the list of known trials that worked
     * @return a map with "Support", "Confidence", and "Lift" of the selected
     * pattern
     */
    public static Map<String, String> FindFrequencyOfNodes(Variables variables, List<String> correctTrials) {

        // Testing purposes
        List<String> correctTrials_test = new ArrayList<>();
        correctTrials_test.add("workflow_trial_7.xml");
        correctTrials_test.add("workflow_trial_11.xml");
        correctTrials_test.add("workflow_trial_16.xml");
        correctTrials_test.add("workflow_trial_17.xml");
        correctTrials_test.add("workflow_trial_18.xml");
        correctTrials_test.add("workflow_trial_22.xml");
        correctTrials_test.add("workflow_trial_24.xml");
        correctTrials_test.add("workflow_trial_25.xml");
        correctTrials_test.add("workflow_trial_28.xml");
        correctTrials_test.add("workflow_trial_32.xml");
        // End testing purposes
        double support_notok;
        double support_ok;
        double confidence_ok;
        double confidence_notok;
        double lift_notok;
        double lift_ok;
        int notok = 0;
        int ok = 0;
        String selected_vertices = "Selected vertices:";
        PickedState<Object> picked_state = variables.view.getPickedVertexState();
        int numberOfNodes = picked_state.getSelectedObjects().length;
        Map<String, Integer> graphFiles = new HashMap<>();
        Collection<String> commonFiles = new ArrayList<>();

        for (Object v : picked_state.getSelectedObjects()) { // For each vertex from the pattern selected by the user
            String[] files = ((Vertex) v).getAttributeValues(VariableNames.GraphFile);
            for (String f : files) { // For each graphFile from the current vertex
                if (graphFiles.containsKey(f)) {
                    int i = graphFiles.get(f);
                    i++;
                    graphFiles.put(f, i);
                } else {
                    graphFiles.put(f, 1);
                }
            }
            selected_vertices += "\n" + ((Vertex) v).getID();
        }

        for (String f : graphFiles.keySet()) {
            int qnt = graphFiles.get(f);
            if (qnt == numberOfNodes) {
                if (!correctTrials_test.contains(f)) {
//                    System.out.println("Common graphFile to all:" + f);
                    notok++;
                }
                commonFiles.add(f);
//                else if(correctTrials_test.contains(f)) {
////                    System.out.println("Common graphFile to all:" + f);
//                    ok++;
//                    commonFiles.add(f);
//                }
            }
        }

        int numberofCorrect = 0;
        for (String f : commonFiles) {
            for (String cf : correctTrials_test) {
                if (f.equalsIgnoreCase(cf)) {
                    numberofCorrect++;
                    break;
                }
            }
        }
        support_notok = (double) notok / (double) variables.numberOfGraphs;
        support_ok = (double) numberofCorrect / (double) variables.numberOfGraphs;
        confidence_ok = (double) numberofCorrect / (double) commonFiles.size();
        confidence_notok = (double) notok / (double) commonFiles.size();
        lift_notok = confidence_notok / ((double) (variables.numberOfGraphs - correctTrials_test.size()) / (double) variables.numberOfGraphs);
        lift_ok = confidence_ok / ((double) correctTrials_test.size() / (double) variables.numberOfGraphs);

        // Lets make the numbers be in the % notation (0% to 100%)
        confidence_notok = confidence_notok * 100.0f;
        support_notok = support_notok * 100.0f;
        confidence_ok = confidence_ok * 100.0f;
        support_ok = support_ok * 100.0f;

        // Probability of these nodes appearing together
        String s_support_notok = "Support: " + support_notok + " %";
        // Probability of these nodes leading to an undesirable outbome
        String s_confidence_notok = "Confidence: " + confidence_notok + " %";
        String liftResult_notok = "Lift: " + lift_notok;
        String s_support_ok = "Support: " + support_ok + " %";
        String s_confidence_ok = "Confidence: " + confidence_ok + " %";
        String liftResult_ok = "Lift: " + lift_ok;

        System.out.println("NOT OK:");
        System.out.println(s_support_notok);
        System.out.println(s_confidence_notok);
        System.out.println(liftResult_notok);
        System.out.println("OK:");
        System.out.println(s_support_ok);
        System.out.println(s_confidence_ok);
        System.out.println(liftResult_ok);

        Map<String, String> result = new HashMap<>();
        result.put("Support_NOTOK", s_support_notok);
        result.put("Confidence_NOTOK", s_confidence_notok);
        result.put("Lift_NOTOK", liftResult_notok);
        result.put("Support_OK", s_support_ok);
        result.put("Confidence_OK", s_confidence_ok);
        result.put("Lift_OK", liftResult_ok);
        result.put("Selected", selected_vertices);

//        System.out.println("Given that these nodes appear together, the probability of the selected nodes leading to a desirable outbome is: " + frequencyCorrect);
        return result;
    }

    static void ComputeMarkovChain(Variables variables) {
        variables.markov.computeMarkovChain(variables);
    }

    public static String ComputePath(Variables variables) {
        return GraphPath.ComputePath(variables);
    }
}
