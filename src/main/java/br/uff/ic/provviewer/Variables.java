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
package br.uff.ic.provviewer;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import br.uff.ic.provviewer.GUI.GuiBackground;
import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Input.SimilarityConfig;
import br.uff.ic.provviewer.Layout.ProvCircleLayout;
import br.uff.ic.provviewer.Layout.ProvCircleLayout2;
import br.uff.ic.provviewer.Layout.Spatial_Layout;
import br.uff.ic.provviewer.Layout.Timeline_Layout;
import br.uff.ic.provviewer.Layout.Hierarchy_Layout_2;
import br.uff.ic.provviewer.Layout.ListLayout;
import br.uff.ic.provviewer.Layout.TimelineGraphs_Layout;
import br.uff.ic.provviewer.Layout.OneDimensional_Layout;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Layout.OneDimensional_Layout_Ordered;
import br.uff.ic.provviewer.Layout.TwoDimensional_Layout;
import br.uff.ic.utility.GraphCollapser;
import br.uff.ic.utility.GraphUtils;
import br.uff.ic.utility.StackElementUndoDeletion;
import br.uff.ic.utility.ThresholdValues;
import br.uff.ic.utility.Utils;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Class containing graph related variables (View, Layout, GraphCollapser,
 * Graph, CollapsedGraph)
 *
 * @author Kohwalter
 */
public class Variables extends Object {

    public String mergingWithGraphPath;
    public String originalGraphPath = "demo";

//    public static String demo = File.separator + "Graph" + File.separator + "Merge_Test.xml";
    public static String demo = File.separator + "Graph" + File.separator + "AngryBots_3_Merge.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Angry_Robots.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Graph_to_Merge_01.xml";
    public String configDemo = File.separator + "Config" + File.separator + "Angry_Robots_config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Angry_Robots_paperCIG.xml";

//    public static String demo = "Graph" + File.separator + "Car_Tutorial.xml";   
//    public String configDemo = "Config" + File.separator + "Car_Tutorial_config.xml";
//    
//    public static String demo = File.separator + "Graph" + File.separator + "MorphWing2.xml";
//    public String configDemo = "Config" + File.separator + "Morph_config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "rio_city_bus_example.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "bus_linha5.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "rio_de_janeiro_cidade_config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "input.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Test" + File.separator + "prov-dm-example1.prov-asn";
//    public static String demo = File.separator + "Graph" + File.separator + "test" + File.separator + "example-blog1.provn";
//    public static String demo = File.separator + "Graph" + File.separator + "workflow_trial_0.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "PROV_config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "2D_Provenance.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "2D_Tower_Defense_config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "map.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "graph_features_example.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "graph1.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "graph2.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Gradient.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "map_config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "program_prov_example.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "prog_example_config.xml";  
//    public static String demo = File.separator + "Graph" + File.separator + "Car_Tutorial.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "Noise_config.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "bash-count.xml"; 
//    public String configDemo = File.separator + "Config" + File.separator + "Reprozip_config.xml";
    public GuiBackground guiBackground = new GuiBackground();
    public VisualizationViewer<Object, Edge> view;
    public Layout<Object, Edge> layout;
    public GraphCollapser gCollapser;
    public DirectedGraph<Object, Edge> graph;
    public DirectedGraph<Object, Edge> collapsedGraph;
    public Config config = new Config();
    public boolean filterCredits = false;
    public boolean initConfig = false;
    public final Set exclusions = new HashSet();
    public boolean initLayout = true;
    public Filters filter = new Filters();
    public boolean prologIsInitialized = false;
    public File file;
    public DefaultModalGraphMouse mouse = new DefaultModalGraphMouse();
    public Collapser collapser = new Collapser();
    public boolean initialGraph = true;
    public SimilarityConfig mergeConfig;
    public SimilarityConfig similarityConfig;
    public boolean considerOnlyNeighborsSimilarityCollapse = true;

    public String selectedTimeScale = config.timeScale;
    public boolean doDerivate = false;
    public boolean removeDerivateOutliers = false;
    public boolean changedOutliersOption = false;
    public boolean isEdgeStrokeByValue = true;
    public boolean isEdgeColorByGraphs = false;

    public ImproveJUNGPerformance jungPerformance = new ImproveJUNGPerformance();
    public String layout_attribute_X = "Timestamp";
    public String layout_attribute_Y = "Timestamp";
    public int numberOfGraphs = 1;
    public Collection<String> graphNames;
    public boolean highlightVertexOutliers = true;
    public boolean vertexBorderByGraphs = false;
    public ThresholdValues outliersThresholds;
    public boolean allowVariableLayout = false;
    public int edgeAlpha = 25;

    public Stack<StackElementUndoDeletion> undoDeletion = new Stack();

    public boolean allowMergeUndo = false;

    /**
     * Method that updates the number of graphs that comprises the current graph
     * Should be called everytime after a graph is loaded: File Open / Merge
     * Graph
     */
    public void updateNumberOfGraphs() {
        graphNames = Utils.DetectAllPossibleValuesFromAttribute(graph.getVertices(), VariableNames.GraphFile);
        numberOfGraphs = graphNames.size();
        this.config.detectGraphVisualizationModes(this.graphNames);
        this.config.addGraphFileVertexFilter(this.graphNames);
    }

    /**
     * Return the max value between 2 values
     *
     * @param type (String) Edge type to be analyzed
     * @param value (Float) Current max value
     * @param edge (Edge) Second value is extracted from this edge, if the
     * influence type is the same
     * @return max value between value and edge.getValue
     */
    public float CompareMax(float value, Edge edge) {
        value = Math.max(value, edge.getValue());
        return value;
    }

    /**
     * Return the min value between 2 values
     *
     * @param type (String) Edge type to be analyzed
     * @param value (Float) Current max value
     * @param edge (Edge) Second value is extracted from this edge, if the
     * influence type is the same
     * @return max value between value and edge.getValue
     */
    public float CompareMin(float value, Edge edge) {
        value = Math.min(value, edge.getValue());
        return value;
    }

    /**
     * Return the added value between 2 values
     *
     * @param type (String) Edge type to be analyzed
     * @param value (Float[]) Current value
     * @param edge (Edge) Second value is extracted from this edge, if the
     * influence type is the same
     * @return added value between value and edge.getValue
     */
    public float Add(float value, Edge edge) {
        value += Math.abs(edge.getValue());
        return value;
    }

    /**
     * Return the average value
     *
     * @param value (Float[]) Current value and counter
     * @param average (Boolean) To compute the average or not
     * @return average value between value and edge.getValue
     */
    public float[] Average(float[] value, String average) {
        if (average.equalsIgnoreCase("MEAN")) {
            value[0] = value[0] / (value[1] - 1);
        }
        return value;
    }

    /**
     * Return the new value
     *
     * @param etype
     * @param type (String) Edge type to be analyzed
     * @param edge (Edge) Second value is extracted from this edge, if the
     * influence type is the same
     * @param max (Boolean) To compute the max between values or to add values
     * @return value, according to the operation, between value and
     * edge.getValue
     */
    public EdgeType ComputeValue(EdgeType etype, Edge edge) {
        etype.max = CompareMax(etype.max, edge);
        etype.min = CompareMin(etype.min, edge);
        etype.count++;
        etype.total = Add(etype.total, edge);
        return etype;
    }

    /**
     * Find max values for each edge type. Used for edge width.
     *
     */
    public void ComputeEdgeTypeValues() {
        Collection<Edge> edges = this.graph.getEdges();
        for (Edge edge : edges) {
            if (this.config.edgeTypes.containsKey(edge.getLabel())) {
                EdgeType et = this.config.edgeTypes.get(edge.getLabel());
                ComputeValue(et, edge);
            } else if (this.config.edgeTypes.containsKey(edge.getType())) {
                EdgeType et = this.config.edgeTypes.get(edge.getType());
                ComputeValue(et, edge);
            }
        }
        GraphFrame.edgeFilterList.setSelectedIndex(0);
    }

    public void newLayout(String layout) {
        DirectedGraph<Object, Edge> displayGraph;
        if (this.layout != null && this.allowVariableLayout) {
            displayGraph = (DirectedGraph<Object, Edge>) this.layout.getGraph();
        } else {
            displayGraph = this.graph;
        }
        if (layout.equalsIgnoreCase(VariableNames.layout_circle)) {
            this.layout = new CircleLayout<>(displayGraph);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_fr)) {
            this.layout = new FRLayout<>(displayGraph);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_fr2)) {
            this.layout = new FRLayout2<>(displayGraph);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_ISOM)) {
            this.layout = new ISOMLayout<>(displayGraph);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_kk)) {
            this.layout = new KKLayout<>(displayGraph);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_spring)) {
            this.layout = new SpringLayout<>(displayGraph);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_dag)) {
            this.layout = new DAGLayout<>(displayGraph);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_temporal)) {
            this.layout = new Temporal_Layout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_spatial)) {
            this.layout = new Spatial_Layout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_timeline)) {
            this.layout = new Timeline_Layout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_timeline_graphs)) {
            this.layout = new TimelineGraphs_Layout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_One_Dimensional)) {
            this.layout = new OneDimensional_Layout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_One_Dimensional_2)) {
            this.layout = new OneDimensional_Layout_Ordered<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_Two_Dimensional)) {
            this.layout = new TwoDimensional_Layout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_provcircle)) {
            this.layout = new ProvCircleLayout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_provcircle2)) {
            this.layout = new ProvCircleLayout2<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_list)) {
            this.layout = new ListLayout<>(displayGraph, this);
        } else if (layout.equalsIgnoreCase(VariableNames.layout_hierarchy)) {
            this.layout = new Hierarchy_Layout_2<>(displayGraph, this);
        } else {
            this.layout = new Timeline_Layout<>(displayGraph, this);
        }
    }

    public void initGraphCollapser() {
        gCollapser = new GraphCollapser(this.graph, this.config.considerEdgeLabelForMerge);
    }

    /**
     * Method to set the parameters for Highlighting the outliers during a
     * display mode The actual highlighting happnes at GuiFunctions Stroke
     * method
     *
     * @param attribute is the attribute that we are using to display the values
     */
    public void highlightOutliers(String attribute) {
        if (this.highlightVertexOutliers) {
            ArrayList<Float> values = GraphUtils.getAttributeValuesFromVertices(this.graph, attribute);
            if (!values.isEmpty() && values.size() > 5) {
                this.outliersThresholds = Utils.calculateOutliers(values, attribute);
            } else {
                this.outliersThresholds = new ThresholdValues("", 0, 0);
                this.highlightVertexOutliers = false;
            }
        }
    }
}
