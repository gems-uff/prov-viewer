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
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Input.SimilarityConfig;
import br.uff.ic.provviewer.Layout.Spatial_Layout;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Layout.Timeline_Layout;
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
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing graph related variables (View, Layout, GraphCollapser,
 * Graph, CollapsedGraph)
 *
 * @author Kohwalter
 */
public class Variables extends Object {



//    public static String demo = File.separator + "Graph" + File.separator + "Merge_Test.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Angry_Robots_paperCIG.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Angry_Robots.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Graph_to_Merge_01.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "Angry_Robots_config.xml";
    
    public static String demo = File.separator + "Graph" + File.separator + "Car_Tutorial.xml";   
    public String configDemo = File.separator + "Config" + File.separator + "Car_Tutorial_config.xml";
    
//    public static String demo = File.separator + "Graph" + File.separator + "rio_city_bus_example.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "bus_linha5.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "rio_de_janeiro_cidade_config.xml";
    
//    public static String demo = File.separator + "Graph" + File.separator + "input.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "config.xml";
    
//    public static String demo = File.separator + "Graph" + File.separator + "Test" + File.separator + "prov-dm-example1.prov-asn";
//    public static String demo = File.separator + "Graph" + File.separator + "test" + File.separator + "example-blog1.provn";
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
    
//    public static String demo = File.separator + "Graph" + File.separator + "ache.xml"; 
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
    public PrologInference testProlog = new PrologInference();
    public boolean prologIsInitialized = false;
    public File file;
    public DefaultModalGraphMouse mouse = new DefaultModalGraphMouse();
    public Collapser collapser = new Collapser();
    public boolean initialGraph = true;
    public SimilarityConfig mergeConfig;
    public SimilarityConfig similarityConfig;
    
    public String selectedTimeScale = config.timeScale;
    public boolean doDerivate = false;
    public boolean removeDerivateOutliers = false;
    public boolean changedOutliersOption = false;
    
    /**
     * Return the max value between 2 values
     *
     * @param type (String) Edge type to be analyzed
     * @param value (Float) Current max value
     * @param edge (Edge) Second value is extracted from this edge, if the
     * influence type is the same
     * @return max value between value and edge.getValue
     */
    public float CompareMax(String type, float value, Edge edge) {
        if ((edge.getLabel().contains(type))) {
            value = Math.max(value, edge.getValue());
        }
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
    public float CompareMin(String type, float value, Edge edge) {
        if ((edge.getLabel().contains(type))) {
            value = Math.min(value, edge.getValue());
        }
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
    public float Add(String type, float value, Edge edge) {
        if (edge.getLabel().contains(type)) {
            value += Math.abs(edge.getValue());
        }
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
    public EdgeType ComputeValue(EdgeType etype, String type, Edge edge, String max) {
        etype.max = CompareMax(type, etype.max, edge);
        etype.min = CompareMin(type, etype.min, edge);
        etype.count++;
        etype.total = Add(type, etype.total, edge);
        return etype;
    }

    /**
     * Find max values for each edge type. Used for edge width.
     *
     */
    public void ComputeEdgeTypeValues() {
        Collection<Edge> edges = this.graph.getEdges();
        for (Edge edge : edges) {
            for (int i = 0; i < this.config.edgetype.size(); i++) {
                GraphFrame.edgeFilterList.setSelectedIndex(i);
                this.config.edgetype.set(i, ComputeValue(this.config.edgetype.get(i), GraphFrame.edgeFilterList.getSelectedValue().toString(), edge, this.config.edgetype.get(i).stroke));
            }
        }
        GraphFrame.edgeFilterList.setSelectedIndex(0);
    }
    
    public void newLayout(String layout) {
        if (layout.equalsIgnoreCase("CircleLayout")) {
            this.layout = new CircleLayout<>(this.graph);
        }
        else if (layout.equalsIgnoreCase("FRLayout")) {
            this.layout = new FRLayout<>(this.graph);
        }
        else if (layout.equalsIgnoreCase("FRLayout2")) {
            this.layout = new FRLayout2<>(this.graph);
        }
        else if (layout.equalsIgnoreCase("ISOMLayout")) {
            this.layout = new ISOMLayout<>(this.graph);
        }
        else if (layout.equalsIgnoreCase("KKLayout")) {
            this.layout = new KKLayout<>(this.graph);
        }
        else if (layout.equalsIgnoreCase("SpringLayout")) {
            this.layout = new SpringLayout<>(this.graph);
        }
        else if (layout.equalsIgnoreCase("DagLayout")) {
            this.layout = new DAGLayout<>(this.graph);
        }
        else if (layout.equalsIgnoreCase("TemporalLayout")) {
            this.layout = new Temporal_Layout<>(this.graph, this);
        }
        else if (layout.equalsIgnoreCase("SpatialLayout")) {
            this.layout = new Spatial_Layout<>(this.graph, this);
        }
        else if (layout.equalsIgnoreCase("TimelineLayout")) {
            this.layout = new Timeline_Layout<>(this.graph, this);
        }
        else {
            this.layout = new Timeline_Layout<>(this.graph, this);
        }
    }
}
