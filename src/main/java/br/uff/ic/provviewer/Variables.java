/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Input.Config;
import edu.uci.ics.jung.algorithms.layout.Layout;
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


//    public static String demo = File.separator + "Graph" + File.separator + "Car_Tutorial.xml";
    public static String demo = File.separator + "Graph" + File.separator + "Merge_Test.xml";
//    public static String demo = File.separator + "Graph" + File.separator + "Test" + File.separator + "prov-dm-example1.prov-asn";
//    public static String demo = File.separator + "Graph" + File.separator + "test" + File.separator + "example-blog1.provn";
    //    public static String demo = File.separator + "Graph" + File.separator + "Car_Tutorial3.xml";
    //    public static String demo = File.separator + "Graph" + File.separator + "Angry_Robots.xml";
    //    public static String demo = File.separator + "Graph" + File.separator + "2D_Provenance.xml";
    //    public static String demo = File.separator + "Graph" + File.separator + "input.xml";
    //    public static String demo = File.separator + "Graph" + File.separator + "bus.xml";
    //    public static String demo = File.separator + "Graph" + File.separator + "map.xml";
    
//    public String configDemo = File.separator + "Config" + File.separator + "Car_Tutorial_config.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "PROV_config.xml";
    public String configDemo = File.separator + "Config" + File.separator + "Angry_Robots_config.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "2D_Provenance_config.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "config.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "map_config.xml";
//    public String configDemo = File.separator + "Config" + File.separator + "bus_config.xml";

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
     * @param type (String) Edge type to be analyzed
     * @param value (Float[]) Current value and counter
     * @param edge (Edge) Second value is extracted from this edge, if the
     * influence type is the same
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
     * @param type (String) Edge type to be analyzed
     * @param value (Float[]) Current value and counter
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
     * @param graph Graph
     */
    public void ComputeEdgeTypeValues(Variables variables, DirectedGraph<Object, Edge> graph) {
        Collection<Edge> edges = graph.getEdges();
        for (Edge edge : edges) {
            for (int i = 0; i < variables.config.edgetype.size(); i++) {
                GraphFrame.FilterList.setSelectedIndex(i);
                variables.config.edgetype.set(i, ComputeValue(variables.config.edgetype.get(i), GraphFrame.FilterList.getSelectedValue().toString(), edge, variables.config.edgetype.get(i).stroke));
            }
        }
        GraphFrame.FilterList.setSelectedIndex(0);
    }
}
