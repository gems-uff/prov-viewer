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
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.SimilarityCollapse.AutomaticInference;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.provviewer.SimilarityCollapse.Dbscan;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class responsible for the PROLOG implementation
 *
 * @author Kohwalter
 */
public class GuiInference {

    /**
     * Method to apply the similarity collapse in the graph
     *
     * @param variables
     * @param updateError
     * @param verifyWithinCluster
     */
    public static void SimilarityCollapse(Variables variables, boolean updateError, boolean verifyWithinCluster) {
//        GuiButtons.Reset(variables);
        System.out.println("VE: " + updateError);
        System.out.println("IC: " + verifyWithinCluster);
        ArrayList<ConcurrentHashMap<String, Object>> list;
        list = ColorSchemeCollapse((String) StatusFilterBox.getSelectedItem(), variables, updateError, verifyWithinCluster);
//        MarkClusters(list, variables);
        variables.collapser.CollapseIrrelevant(variables, printCollapseGroups(list), (String) StatusFilterBox.getSelectedItem());
        System.out.println("Finished Collapsing");
//        }
    }

    /**
     * Apply the DBSCAN clustering algorithm
     *
     * @param variables
     */
    public static void DBSCANCollapse(Variables variables) {
//        GuiButtons.Reset(variables);
        ArrayList<ConcurrentHashMap<String, Object>> list = DBSCAN((String) StatusFilterBox.getSelectedItem(), variables.graph);
//        MarkClusters(list, variables);
        variables.collapser.CollapseIrrelevant(variables, printCollapseGroups(list), (String) StatusFilterBox.getSelectedItem());
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
                    collapseList += " / " + id1;
                }
                collapseList += " + ";
            }
        }
        return collapseList;
    }

    /**
     * Function to apply one of the three similarity clustering algorithms
     *
     * @param attribute is the attribute used in the distance metric
     * @param updateError is the parameter value to define which algorithm will
     * be used
     * @param verifyWithinCluster is the parameter value to define which
     * algorithm will be used
     * @return the clusters
     */
    public static ArrayList<ConcurrentHashMap<String, Object>> ColorSchemeCollapse(String attribute, Variables variables, boolean updateError, boolean verifyWithinCluster) {
        AutomaticInference infer;
        int simSize = 1;
        double simInc = 1;
        double simEpsilonModifier = 1;
        double eps = 1;
        GraphMatching combiner;

        // -----------------------------
        // Standard Deviation
        // -----------------------------
        double std = Utils.std(variables.layout.getGraph().getVertices(), attribute);

        // -----------------------------
        // Similarity configuration
        // -----------------------------
        if (Utils.tryParseDouble(GraphFrame.simEpsilon.getText())) {
            simEpsilonModifier = Double.parseDouble(GraphFrame.simEpsilon.getText());
        }
        
        if(GraphFrame.attributeDisplaySimConfig.isSelected()) {
            String defaultError = "0";
            double similarityThreshold = 0.5f;
            Map<String, AttributeErrorMargin> restrictionList = new HashMap<>();
            AttributeErrorMargin epsilon;
            eps = std * simEpsilonModifier; // The epsilon used by the algorithm
            if(eps != eps)
                eps = 1;
            System.out.println("Eps used: " + eps);
            epsilon = new AttributeErrorMargin(attribute, String.valueOf(eps));
            restrictionList.put(attribute, epsilon);
            combiner = new GraphMatching(restrictionList, similarityThreshold, defaultError, 0);
        }
        else {
            combiner = new GraphMatching(variables.similarityConfig.getRestrictionList(), variables.similarityConfig.getVocabulary(), variables.similarityConfig.getSimilarityThreshold(), variables.similarityConfig.getDefaultError(), variables.similarityConfig.getDefaultWeight());
        }
        

        if (updateError) {
            if (Utils.tryParseDouble(GraphFrame.simStdSize.getText())) {
                simSize = Integer.parseInt(GraphFrame.simStdSize.getText());
            }
            if (Utils.tryParseDouble(GraphFrame.simStdInc.getText())) {
                simInc = Double.parseDouble(GraphFrame.simStdInc.getText());
            }
            infer = new AutomaticInference(combiner, (DirectedGraph<Object, Edge>) variables.layout.getGraph(), simSize, simInc, simEpsilonModifier);
        } else {
            infer = new AutomaticInference(combiner, (DirectedGraph<Object, Edge>) variables.layout.getGraph());
        }

        // Set "ErrorTest" since it uses only one attribute for faster computation
        if(GraphFrame.attributeDisplaySimConfig.isSelected()) {
            infer.setSingleAttributeOptimization(attribute, eps);
        }
        ArrayList<ConcurrentHashMap<String, Object>> clusters = infer.applySimilarity(updateError, verifyWithinCluster, variables.considerOnlyNeighborsSimilarityCollapse);
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

        double eps = 1;
        if (Utils.tryParseDouble(GraphFrame.dbscanEpsilon.getText())) {
            eps = Double.parseDouble(GraphFrame.dbscanEpsilon.getText());
        }
        if (GraphFrame.isSTDeps.isSelected()) {
            double epsilon = Utils.std(graph.getVertices(), attribute);
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
                GraphAttribute att = new GraphAttribute("Cluster", i + "", variables.originalGraphPath);
                ((Vertex) v).addAttribute(att);
            }
            i++;
        }
    }
}
