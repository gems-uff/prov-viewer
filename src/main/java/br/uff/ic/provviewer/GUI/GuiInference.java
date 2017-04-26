/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
     * @param file the XML file for conversion
     */
    private static void ConvertXML(File file) {
        XMLConverter xmlConv = new XMLConverter();
        xmlConv.ConvertXMLtoProlog(file);
    }

    /**
     * Method to apply the similarity collapse in the graph
     *
     * @param InitPrologButton is the boolean to check if the prolog is active
     * @param prolog is the prolog implementation
     * @param variables
     * @param edgeType is the type of edge used for the collapse
     */
    public static void SimilarityCollapse(boolean InitPrologButton, PrologInference prolog, Variables variables, String edgeType, boolean updateError, boolean verifyWithinCluster) {
        if (InitPrologButton) {
            System.out.println("Starting Prolog Inference: " + edgeType);
            GuiButtons.Reset(variables);
            String list;
            list = prolog.QueryCollapse((String) StatusFilterBox.getSelectedItem(), edgeType);
            System.out.println("Collapsing...");
            variables.collapser.CollapseIrrelevant(variables, list);
            System.out.println("Finished Collapsing");
        } else {
            GuiButtons.Reset(variables);
            System.out.println("updateError: " + updateError);
            System.out.println("verifyWithinCluster: " + verifyWithinCluster);
            ArrayList<ConcurrentHashMap<String, Object>> list = ColorSchemeCollapse((String) StatusFilterBox.getSelectedItem(), variables.graph, updateError, verifyWithinCluster);
            MarkClusters(list, variables);
            variables.collapser.CollapseIrrelevant(variables, printCollapseGroups(list));
            System.out.println("Finished Collapsing");
        }
    }
    
    public static void DBSCANCollapse(Variables variables) {
        GuiButtons.Reset(variables);
        ArrayList<ConcurrentHashMap<String, Object>> list = DBSCAN((String) StatusFilterBox.getSelectedItem(), variables.graph);
        MarkClusters(list, variables);
        variables.collapser.CollapseIrrelevant(variables, printCollapseGroups(list));
        System.out.println("Finished Collapsing");
        
    }
    
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

    public static ArrayList<ConcurrentHashMap<String, Object>> ColorSchemeCollapse(String attribute, DirectedGraph<Object, Edge> graph, boolean updateError, boolean verifyWithinCluster) {
        // -----------------------------
        // Standard Deviation
        // -----------------------------
        float std = Utils.std(graph.getVertices(), attribute);
//        System.out.println("STD = " + std);

        // -----------------------------
        // Similarity configuration
        // -----------------------------
        float similarityThreshold = 0.5f;
        String defaultError = "0";
        Map<String, AttributeErrorMargin> restrictionList = new HashMap<>();
        AttributeErrorMargin epsilon;
        epsilon = new AttributeErrorMargin(attribute, String.valueOf(std));
        restrictionList.put(attribute, epsilon);
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold, defaultError, 0);

        AutomaticInference infer;
        int TF_size;
        int TF_increase;
        int TF_qnt;
        int TT_size;
        int TT_increase;
        int TT_qnt;
        
        TT_size = 5;
        TT_increase = 10;
        TT_qnt = 4;
        
        TF_size = 5;
        TF_increase = 13;
        TF_qnt = 5;
        
        if(updateError) {
            if(verifyWithinCluster) { // True True
                infer = new AutomaticInference(combiner, graph, TT_size, TT_increase, TT_qnt);
            }
            else { // True False
                infer = new AutomaticInference(combiner, graph, TF_size, TF_increase, TF_qnt);
            }
        }
        else {
            if(verifyWithinCluster) { // False True
                infer = new AutomaticInference(combiner, graph, 1, 1, 1);
            }
            else { // False False
                infer = new AutomaticInference(combiner, graph, 1, 1, 1);
            }
        }
//        infer = new AutomaticInference(combiner, graph, 7, 4, 4);
        ArrayList<ConcurrentHashMap<String, Object>> clusters =  infer.applySimilarity(updateError, verifyWithinCluster);
        return clusters;
    }
    
    public static ArrayList<ConcurrentHashMap<String, Object>> DBSCAN (String attribute, DirectedGraph<Object, Edge> graph) {
//        float epsilon = 50;
        Dbscan dbscan;
        
        
        float eps = 1;
        if (Utils.tryParseFloat(GraphFrame.dbscanEpsilon.getText())) {
            eps = Float.parseFloat(GraphFrame.dbscanEpsilon.getText());
        }
        System.out.println("Eps = " + eps);
        if(GraphFrame.isSTDeps.isSelected()) {
            float epsilon = Utils.std(graph.getVertices(), attribute);
            dbscan = new Dbscan(graph, attribute, epsilon * eps, 1);
            System.out.println("STD");
        }
        else {
            dbscan = new Dbscan(graph, attribute, eps, 1);
            System.out.println("Normal");
        }
        ArrayList<ConcurrentHashMap<String, Object>> clusters = dbscan.applyDbscan();
        return clusters; 
    }
    
    private static void MarkClusters(ArrayList<ConcurrentHashMap<String, Object>> clusters, Variables variables) {
        int i = 0;
        for(ConcurrentHashMap<String, Object> c : clusters) {
            for(Object v : c.values()) {
                GraphAttribute att = new GraphAttribute("Cluster", i + "");
                ((Vertex)v).addAttribute(att);
            }
            i++;
        }
    }
}
