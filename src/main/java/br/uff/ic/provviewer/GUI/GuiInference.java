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

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.xmlToProlog.XMLConverter;
import br.uff.ic.utility.IO.BasePath;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Inference.AutomaticInference;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Dbscan;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JToggleButton;

/**
 * Class responsible for the PROLOG implementation
 *
 * @author Kohwalter
 */
public class GuiInference {

    /**
     * Method to enable the prolog functions
     *
     * @deprecated Prolog is no longer used
     * @param prolog is the prolog implementation for the automatic inference
     * @param prologIsInitialized is the boolean to check if the prolog can be
     * activated
     * @param InitPrologButton is the boolean to check if the prolog can be
     * activated
     */
    public static void InitializeProlog(PrologInference prolog, boolean prologIsInitialized, JToggleButton InitPrologButton) {
        if (InitPrologButton.isSelected() && !prologIsInitialized) {
            prologIsInitialized = true;
            prolog.Init();
        }
    }

    /**
     * Method to generate the prolog facts using the XML-Inference
     *
     * @deprecated Prolog is no longer used
     * @param initialGraph
     * @param file
     * @param demo
     */
    public static void GeneratePrologFacts(boolean initialGraph, File file, String demo) {
        if (initialGraph) {
            file = new File(BasePath.getBasePathForClass(GuiInference.class) + demo);
            initialGraph = false;
        }
        ConvertXML(file);
        System.out.println("Finished Converting to Prolog");
    }

    /**
     * Method to convert the XML to prolog facts
     *
     * @deprecated Prolog is no longer used
     * @param file the XML file for conversion
     */
    private static void ConvertXML(File file) {
        XMLConverter xmlConv = new XMLConverter();
        xmlConv.ConvertXMLtoProlog(file);
    }

    /**
     * Method to apply the similarity collapse in the graph
     *
     * @param variables
     * @param updateError
     * @param verifyWithinCluster
     */
    public static void SimilarityCollapse(Variables variables, boolean updateError, boolean verifyWithinCluster) {
        GuiButtons.Reset(variables);
        System.out.println("VE: " + updateError);
        System.out.println("IC: " + verifyWithinCluster);
        ArrayList<ConcurrentHashMap<String, Object>> list;
        list = ColorSchemeCollapse((String) StatusFilterBox.getSelectedItem(), variables, updateError, verifyWithinCluster);
        MarkClusters(list, variables);
        variables.collapser.CollapseIrrelevant(variables, printCollapseGroups(list));
        System.out.println("Finished Collapsing");
//        }
    }

    /**
     * Apply the DBSCAN clustering algorithm
     *
     * @param variables
     */
    public static void DBSCANCollapse(Variables variables) {
        GuiButtons.Reset(variables);
        ArrayList<ConcurrentHashMap<String, Object>> list = DBSCAN((String) StatusFilterBox.getSelectedItem(), variables.graph);
        MarkClusters(list, variables);
        variables.collapser.CollapseIrrelevant(variables, printCollapseGroups(list));
        System.out.println("Finished Collapsing");

    }

    /**
     * Function that return the list of vertex ID from each cluster separating
     * the IDs by a comma and the clusters by a blank space
     *
     * @param collapseGroups is the array that contains the clusters
     * @return the IDs from each vertex in each cluster
     */
    public static String printCollapseGroups(ArrayList<ConcurrentHashMap<String, Object>> collapseGroups) {
        String collapseList = "";
        for (ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
            if (subGraph.size() > 0) {
                for (Object v1 : subGraph.values()) {
                    String id1 = ((Vertex) v1).getID();
                    collapseList += "," + id1;
                }
                collapseList += " ";
            }
        }
        return collapseList;
    }

    /**
     * Function to apply one of the three similarity clustering algorithms
     *
     * @param attribute is the attribute used in the distance metric
     * @param graph is the graph that we want to apply the clustering
     * @param updateError is the parameter value to define which algorithm will
     * be used
     * @param verifyWithinCluster is the parameter value to define which
     * algorithm will be used
     * @return the clusters
     */
    public static ArrayList<ConcurrentHashMap<String, Object>> ColorSchemeCollapse(String attribute, Variables variables, boolean updateError, boolean verifyWithinCluster) {
        AutomaticInference infer;
        int simSize = 1;
        float simInc = 1;
        float simEpsilonModifier = 1;
        double eps = 1;
        GraphMatching combiner;

        // -----------------------------
        // Standard Deviation
        // -----------------------------
        float std = Utils.std(variables.graph.getVertices(), attribute);

        // -----------------------------
        // Similarity configuration
        // -----------------------------
        if (Utils.tryParseFloat(GraphFrame.simEpsilon.getText())) {
            simEpsilonModifier = Float.parseFloat(GraphFrame.simEpsilon.getText());
        }
        
        if(GraphFrame.attributeDisplaySimConfig.isSelected()) {
            String defaultError = "0";
            float similarityThreshold = 0.5f;
            Map<String, AttributeErrorMargin> restrictionList = new HashMap<>();
            AttributeErrorMargin epsilon;
            eps = std * simEpsilonModifier; // The epsilon used by the algorithm
            System.out.println("Eps used: " + eps);
            epsilon = new AttributeErrorMargin(attribute, String.valueOf(eps));
            restrictionList.put(attribute, epsilon);
            combiner = new GraphMatching(restrictionList, similarityThreshold, defaultError, 0);
        }
        else {
            combiner = new GraphMatching(variables.similarityConfig.getRestrictionList(), variables.similarityConfig.getVocabulary(), variables.similarityConfig.getSimilarityThreshold(), variables.similarityConfig.getDefaultError(), variables.similarityConfig.getDefaultWeight());
        }
        

        if (updateError) {
            if (Utils.tryParseFloat(GraphFrame.simStdSize.getText())) {
                simSize = Integer.parseInt(GraphFrame.simStdSize.getText());
            }
            if (Utils.tryParseFloat(GraphFrame.simStdInc.getText())) {
                simInc = Float.parseFloat(GraphFrame.simStdInc.getText());
            }
            infer = new AutomaticInference(combiner, variables.graph, simSize, simInc, simEpsilonModifier);
//            if(verifyWithinCluster) { // True True
//                infer = new AutomaticInference(combiner, graph, TT_size, TT_increase, TT_qnt);
//            }
//            else { // True False
//                infer = new AutomaticInference(combiner, graph, TF_size, TF_increase, TF_qnt);
//            }
        } else {
            infer = new AutomaticInference(combiner, variables.graph);
        }

        // Set "ErrorTest" since it uses only one attribute for faster computation
        if(GraphFrame.attributeDisplaySimConfig.isSelected()) {
            infer.setSingleAttributeOptimization(attribute, eps);
        }
        ArrayList<ConcurrentHashMap<String, Object>> clusters = infer.applySimilarity(updateError, verifyWithinCluster);
        return clusters;
    }

    /**
     * Execute DBSCAN clustering algorithm
     *
     * @param attribute is the attribute to be considered for the distance
     * metric
     * @param graph is the graph that we want to apply the clustering
     * @return the clusters for collapse
     */
    public static ArrayList<ConcurrentHashMap<String, Object>> DBSCAN(String attribute, DirectedGraph<Object, Edge> graph) {
        Dbscan dbscan;

        float eps = 1;
        if (Utils.tryParseFloat(GraphFrame.dbscanEpsilon.getText())) {
            eps = Float.parseFloat(GraphFrame.dbscanEpsilon.getText());
        }
        if (GraphFrame.isSTDeps.isSelected()) {
            float epsilon = Utils.std(graph.getVertices(), attribute);
            dbscan = new Dbscan(graph, attribute, epsilon * eps, 1);
        } else {
            dbscan = new Dbscan(graph, attribute, eps, 1);
        }
        ArrayList<ConcurrentHashMap<String, Object>> clusters = dbscan.applyDbscan();
        return clusters;
    }

    /**
     * Function to embedded the cluster number in the vertices. Used for
     * debugging
     *
     * @param clusters
     * @param variables
     */
    private static void MarkClusters(ArrayList<ConcurrentHashMap<String, Object>> clusters, Variables variables) {
        int i = 0;
        for (ConcurrentHashMap<String, Object> c : clusters) {
            for (Object v : c.values()) {
                GraphAttribute att = new GraphAttribute("Cluster", i + "");
                ((Vertex) v).addAttribute(att);
            }
            i++;
        }
    }
}
