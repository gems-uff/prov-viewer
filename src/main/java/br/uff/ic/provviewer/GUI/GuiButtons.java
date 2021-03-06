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

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Edge;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Layout.Temporal_Layout_entityAppart;
import br.uff.ic.provviewer.Layout.OneDimensional_Layout;
import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import br.uff.ic.utility.IO.PROVNWriter;
import br.uff.ic.utility.IO.XMLWriter;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.GraphVertex;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import org.apache.commons.collections15.Transformer;

/**
 * Class responsible for implementing each GUI button
 * @author Kohwalter
 */
public class GuiButtons {

    /**
     * Method to exit the application
     */
    public static void Exit() {
        System.exit(0);
    }
    
    public static void Delete(Variables variables){
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        GuiFunctions.DeleteVertices(variables, picked);
    }
    
    public static void UndoDeletion(Variables variables){
        GuiFunctions.UndoLastDeletion(variables);
    }

    /**
     * Method to expand a previously collapsed vertex
     * @param variables 
     */
    public static void Expand(Variables variables) {
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        variables.collapser.Expander(variables, picked);
    }

    /**
     * Method to collapse a set of selected vertices
     * @param variables 
     */
    public static void Collapse(Variables variables) {
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        variables.collapser.Collapse(variables, picked, true, "");
    }

    /**
     * Method to rollback to the original graph
     * @param variables 
     */
    public static void Reset(Variables variables) {
        variables.collapser.ResetGraph(variables);
        variables.config.resetVertexModeInitializations();
    }

    /**
     * Method to select the mouse modes (Picking or Transforming)
     * @param mouse
     * @param MouseModes 
     */
    public static void MouseModes(DefaultModalGraphMouse mouse, JComboBox MouseModes) {
        String mode = (String) MouseModes.getSelectedItem();
        if (mode.equalsIgnoreCase(VariableNames.MouseModePicking)) {
            mouse.setMode(ModalGraphMouse.Mode.PICKING);
        }
        if (mode.equalsIgnoreCase(VariableNames.MouseModeTransforming)) {
            mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        }
    }

    /**
     * Method to get the selected agent vertices for the collapse
     * @param variables 
     */
    public static void CollapseAgent(Variables variables) {
        PickedInfo<Object> picked_state;
        picked_state = variables.view.getPickedVertexState();
        Collection<Object> agents = new HashSet();
        //Get the selected node
        for (Object z : variables.layout.getGraph().getVertices()) {
            if(z instanceof AgentVertex)
                if (picked_state.isPicked(z))
                    agents.add(z);
        }
        CollapseSelectedAgents(variables, agents);
    }
    
    /**
     * Method that selects all agents for the collapse
     * @param variables 
     */
    public static void CollapseAllAgents(Variables variables) {
        Collection<Object> agents = new HashSet();
        //Get the selected node
        for (Object z : variables.layout.getGraph().getVertices()) {
            if(z instanceof AgentVertex)
                agents.add(z);
        }
        CollapseSelectedAgents(variables, agents);
    }
        
    /**
     * Method that do the collapse of the agent's vertices
     * @param variables
     * @param agents is the list of agents that we want to be collapsed
     */
    private static void CollapseSelectedAgents(Variables variables, Collection<Object> agents) {
        //Select the node and its neighbors to be collapsed
        for(Object node : agents) {
            if (variables.layout.getGraph().getNeighbors(node) != null) {
                Collection picked = new HashSet();
                for(Object v : variables.layout.getGraph().getNeighbors(node)) {
                    if(v instanceof GraphVertex) {
                        boolean hasAgent = false;
                        if(((GraphVertex)v).hasAttribute(VariableNames.CollapsedVertexAgentAttribute))
                            hasAgent = true;
                        if(!hasAgent)
                            picked.add(v);
                    }
                    else if(!(v instanceof AgentVertex))
                        picked.add(v);
                }
    //            Collection picked = new HashSet(variables.layout.getGraph().getNeighbors(node));
                picked.add(node);
                if (!(node instanceof AgentVertex)) {
                    picked.removeAll(picked);
                }
                variables.collapser.Collapse(variables, picked, true, "");
            }
        }
    }

    /**
     * Method to apply edge filters
     * @param variables 
     */
    public static void Filter(Variables variables) {
        variables.filter.Filters(variables);
    }

    /**
     * Method to define the overall edge arrow format
     * @param EdgeLineShapeSelection
     * @param variables 
     */
    public static void EdgeLineMode(JComboBox EdgeLineShapeSelection, Variables variables) {
        String mode = (String) EdgeLineShapeSelection.getSelectedItem();
        if (mode.equalsIgnoreCase(VariableNames.EdgeModeQuadCurve)) {
            variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Object, Edge>());
        }
        if (mode.equalsIgnoreCase(VariableNames.EdgeModeLine)) {
            variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Object, Edge>());
        }
        variables.view.repaint();
    }

    /**
     * Method to change the vertex coloring according to the selected attribute
     * @param variables 
     */
    public static void StatusFilter(Variables variables) {
        VertexPainter.VertexPainter((String) StatusFilterBox.getSelectedItem(), variables.view, variables);
        variables.view.repaint();
    }

    /**
     * method to display edge label
     * @param variables
     * @param ShowEdgeIDButton
     * @param ShowEdgeLabelButton 
     * @param ShowEdgeTypeButton 
     * @param ShowEdgeProbabilityButton 
     * @param ShowEdgePathProbabilityButton 
     */
    public static void EdgeTextDisplay(final Variables variables, boolean ShowEdgeIDButton, boolean ShowEdgeLabelButton, boolean ShowEdgeTypeButton, boolean ShowEdgeProbabilityButton, boolean ShowEdgePathProbabilityButton) {
        if (ShowEdgeIDButton) {
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    return VariableNames.FontConfiguration + i.getID();
                }
            });
        } else if (ShowEdgeLabelButton && ShowEdgeTypeButton) {
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    return VariableNames.FontConfiguration + i.getType() + " (" + i.getLabel() + ")";
                }
            });
        } else if (ShowEdgeLabelButton) {
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    return VariableNames.FontConfiguration + i.getLabel();
                }
            });
        } else if (ShowEdgeTypeButton) {
        variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

            @Override
            public String transform(Edge i) {
                return VariableNames.FontConfiguration + i.getType();
            }
        });
        } else if(ShowEdgeProbabilityButton) {
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    if(Utils.edgeHighlighted(i, variables)) {
                        return VariableNames.FontConfiguration + 
                            "Frequency: " + i.getFrequency(variables.numberOfGraphs)
                            + "<br>MarkovIn: " + i.getAttributeValue(VariableNames.MarkovIn)
                            + "<br>MarkovOut: " + i.getAttributeValue(VariableNames.MarkovOut);
                    } else
                        return "";
                }
            });
        } else if(ShowEdgePathProbabilityButton) {
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    Pair endpoints = variables.layout.getGraph().getEndpoints(i);
                    Object target = endpoints.getSecond();
                    int sources = variables.layout.getGraph().getInEdges(target).size();
                    if(sources == 1)
                        return VariableNames.FontConfiguration + "100%";
                    else
                        return VariableNames.FontConfiguration + i.getFrequency(variables.numberOfGraphs);
                }
            });
        } else {
            //Show nothing
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    return "";
                }
            });
        }
        variables.view.repaint();
    }

    /**
     * Method to change the graph layout
     * @param variables
     * @param Layouts 
     */
    public static void LayoutSelection(Variables variables, JComboBox Layouts) {
        String layout = (String) Layouts.getSelectedItem();
        variables.newLayout(layout);
        variables.guiBackground.InitBackground(variables, Layouts);
        variables.view.setGraphLayout(variables.layout);
        variables.view.repaint();
        GuiFunctions.PanCameraToFirstVertex(variables);
    }
    
    public static void setVariableLayouts(Variables variables, boolean b, JComboBox Layouts) {
       variables.allowVariableLayout = b;
       if(!variables.allowVariableLayout)
           LayoutSelection(variables, Layouts);
    }
    
    /**
     * Method to update the layouts that use the vertex's time as X position since the user can alter the timescale
     * @param variables
     * @param Layouts 
     */
    public static void UpdateTemporalLayout(Variables variables, JComboBox Layouts) {
        String layout = (String) Layouts.getSelectedItem();
        if (layout.equalsIgnoreCase(VariableNames.layout_temporal)) {
            variables.layout = new Temporal_Layout_entityAppart<>(variables.layout.getGraph(), variables);
            variables.guiBackground.InitBackground(variables, Layouts);
            variables.view.setGraphLayout(variables.layout);
            variables.view.repaint();
        }
        if (layout.equalsIgnoreCase(VariableNames.layout_One_Dimensional)) {
            variables.layout = new OneDimensional_Layout<>(variables.layout.getGraph(), variables);
            variables.guiBackground.InitBackground(variables, Layouts);
            variables.view.setGraphLayout(variables.layout);
            variables.view.repaint();
        }
        
    }

    public static void AutoDetectEdge(Variables variables, boolean selected) {
        if(selected)
            variables.config.DetectEdges(variables.graph.getEdges());
        variables.ComputeEdgeTypeValues();
    }
    
    public static void AutoDetectVertexModes(Variables variables, boolean selected) {
        if(selected)
            variables.config.DetectVertexModes(variables.graph.getVertices());
    }
    
//    public static void AutoDetectVertexLabels(Variables variables, boolean selected) {
//        if(selected)
//            variables.config.DetectVertexAttributeFilterValues(variables.graph.getVertices());
//    }

    public static void VertexLabel(Variables variables, boolean agentLabel, boolean activityLabel, boolean entityLabel, boolean timeLabel, boolean showID) {
        GuiFunctions.VertexLabel(variables, agentLabel, activityLabel, entityLabel, timeLabel, showID);
        variables.view.repaint();
    }

    /**
     * Exports the original graph to PROVN
     * If the Graph was merged, then it will lose the ability to expand the graph since it will be a normal vertex
     * Not really recomended nor fully tested since information can be lost if the graph changed inside PROV Viewer due to format restrictions
     * @param variables 
     */
    public static void ExportGraphPROVN(Variables variables) {
        PROVNWriter provnWriter = new PROVNWriter(variables.graph.getVertices(), variables.graph.getEdges());
//        XMLWriter xmlWriter = new XMLWriter(variables.graph.getVertices(), variables.graph.getEdges());
//        variables.filter.showHiddenEdges(variables, variables.view, variables.layout);
//        XMLWriter xmlWriter_collapsed = new XMLWriter(variables.layout.getGraph().getVertices(), variables.layout.getGraph().getEdges());
        try {
            provnWriter.saveToProvn("Export_Original_Graph_PROVN");
//            xmlWriter.saveToXML("XML_Export_Test");
//            xmlWriter_collapsed.saveToXML("XML_Collapsed_Export_Test");
        } catch (IOException ex) {
            Logger.getLogger(GuiButtons.class.getName()).log(Level.SEVERE, null, ex);
        }
//        variables.filter.filterHiddenEdges(variables.view, variables.layout);
    }
    
    /**
     * Export the original graph or the resulting merged graph in Prov Viewer's xml format
     * @param vertices collection of vertices
     * @param edges collection of edges
     * @param fileName the name the file will have
     */
    public static void ExportGraphXML(Collection<Object> vertices, Collection<Edge> edges, String fileName) {
        XMLWriter xmlWriter = new XMLWriter(vertices, edges);
        try {
            xmlWriter.saveToXML(fileName);
        } catch (IOException ex) {
            Logger.getLogger(GuiButtons.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Export the displayed graph in Prov Viewer's xml format
     * Will result in data loss since it exports after applying filters
     * Anything not being displayed will be lost (e.g., hidden due to filters)
     * @param variables 
     */
    public static void ExportDisplayedGraphXML(Variables variables) {
        variables.filter.showHiddenEdges(variables, variables.view, variables.layout);
        XMLWriter xmlWriter_collapsed = new XMLWriter(variables.layout.getGraph().getVertices(), variables.layout.getGraph().getEdges());
        try {
            xmlWriter_collapsed.saveToXML("Export_Displayed_Graph_XML");
        } catch (IOException ex) {
            Logger.getLogger(GuiButtons.class.getName()).log(Level.SEVERE, null, ex);
        }
        variables.filter.filterHiddenEdges(variables.view, variables.layout);
    }
    
    /**
     * Export the collapsed graph in Prov Viewer's xml format
     * No data loss since the export is done before applying filters in the graph
     * @param variables 
     */
    public static void ExportCollapsedGraphXML(Variables variables) {
        XMLWriter xmlWriter_collapsed = new XMLWriter(variables.collapsedGraph.getVertices(), variables.collapsedGraph.getEdges());
        try {
            xmlWriter_collapsed.saveToXML("Export_Collapsed_Graph_XML");
        } catch (IOException ex) {
            Logger.getLogger(GuiButtons.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void TemporalLayoutGranularity(String selectedTime, Variables variables, 
            boolean nanoseconds, 
            boolean microseconds, 
            boolean milliseconds, 
            boolean seconds, 
            boolean minutes, 
            boolean hours, 
            boolean days, 
            boolean weeks, 
            JComboBox Layouts, 
            JCheckBoxMenuItem temporalNanosecondsButton, 
            JCheckBoxMenuItem temporalMicrosecondsButton, 
            JCheckBoxMenuItem temporalMillisecondsButton, 
            JCheckBoxMenuItem temporalSecondsButton, 
            JCheckBoxMenuItem temporalMinutesButton, 
            JCheckBoxMenuItem temporalHoursButton, 
            JCheckBoxMenuItem temporalDaysButton, 
            JCheckBoxMenuItem temporalWeeksButton) {
        variables.selectedTimeScale = selectedTime;
        temporalNanosecondsButton.setSelected(nanoseconds);
        temporalMicrosecondsButton.setSelected(microseconds);
        temporalMillisecondsButton.setSelected(milliseconds);
        temporalSecondsButton.setSelected(seconds);
        temporalMinutesButton.setSelected(minutes);
        temporalHoursButton.setSelected(hours);
        temporalDaysButton.setSelected(days);
        temporalWeeksButton.setSelected(weeks);
        
        GuiButtons.UpdateTemporalLayout(variables, Layouts);
    }
    
    public static void derivateScheme(Variables variables, boolean doDerivate) {
        variables.doDerivate = doDerivate;
        StatusFilter(variables);
        
    }
    
    public static void removeOutliersDerivateScheme(Variables variables, boolean removeOutliers) {
        variables.removeDerivateOutliers = removeOutliers;
        variables.changedOutliersOption = true;
        StatusFilter(variables);
        
    }
    
    public static void selectVertexShape(Variables variables, String selectedMode, String attribute) {
        GuiFunctions.VertexShape(variables, selectedMode, attribute);
    }
    
    public static void ShowVertexRenameLabelWindow(JDialog setNewLabelDialogBox) {
        setNewLabelDialogBox.setVisible(true);
        setNewLabelDialogBox.toFront();
        setNewLabelDialogBox.setLocationRelativeTo(null);
    }
    public static void UpdateVertexLabel(String newLabel, Variables variables) {
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        GuiFunctions.RenameSelectedVertexLabel(newLabel, picked);
    }

    public static void SearchVertexByID(String ID, Variables variables) {
        System.out.println("Now Here");
        GuiFunctions.SearchVertexByID(ID, variables);
    }
    
    public static void ShowVertexSearchWindow(JDialog searchVertexIDDialogBox) {
        searchVertexIDDialogBox.setVisible(true);
        searchVertexIDDialogBox.toFront();
        searchVertexIDDialogBox.setLocationRelativeTo(null);
    }

    public static void ComputeMarkov(Variables variables) {
        GuiFunctions.ComputeMarkovChain(variables);
    }
    
    public static void EdgeColorConfiguration(Variables variables, 
            boolean type, 
            boolean value, 
            boolean graphs, 
            boolean markovin, 
            boolean markovout, 
            JCheckBoxMenuItem isEdgeColorByType, 
            JCheckBoxMenuItem isColorByEdgeValueButton, 
            JCheckBoxMenuItem EdgeColorGraphsButtom, 
            JCheckBoxMenuItem EdgeColorMarkovInButtom, 
            JCheckBoxMenuItem EdgeColorMarkovOutButtom) {
        variables.isEdgeColorByType = type;
        variables.isEdgeColorByValue = value;
        variables.isEdgeColorByGraphs = graphs;
        variables.isEdgeColorByMarkovIn = markovin;
        variables.isEdgeColorByMarkovOut = markovout;
        isEdgeColorByType.setSelected(type);
        isColorByEdgeValueButton.setSelected(value);
        EdgeColorGraphsButtom.setSelected(graphs);
        EdgeColorMarkovInButtom.setSelected(markovin);
        EdgeColorMarkovOutButtom.setSelected(markovout);
        variables.view.repaint();
    }
    public static void EdgeStrokeConfiguration(Variables variables, 
            boolean value, 
            boolean graphs, 
            boolean markovin, 
            boolean markovout, 
            boolean defaultStroke,
            JCheckBoxMenuItem EdgeStrokeByValueButton, 
            JCheckBoxMenuItem EdgeStrokeByGraphButton, 
            JCheckBoxMenuItem EdgeStrokeByMarkovinButton, 
            JCheckBoxMenuItem EdgeStrokeByMarkovoutButton,
            JCheckBoxMenuItem EdgeStrokeDefaultButton) {
        variables.isEdgeStrokeByValue = value;
        variables.isEdgeStrokeByGraph = graphs;
        variables.isEdgeStrokeByMarkovIn = markovin;
        variables.isEdgeStrokeByMarkovOut = markovout;
        variables.isEdgeStrokeDefault = defaultStroke;
        EdgeStrokeByValueButton.setSelected(value);
        EdgeStrokeByGraphButton.setSelected(graphs);
        EdgeStrokeByMarkovinButton.setSelected(markovin);
        EdgeStrokeByMarkovoutButton.setSelected(markovout);
        EdgeStrokeDefaultButton.setSelected(markovout);
        variables.view.repaint();
    }

}
