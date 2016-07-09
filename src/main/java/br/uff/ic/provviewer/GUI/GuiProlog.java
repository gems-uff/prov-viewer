/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.xmlToProlog.XMLConverter;
import br.uff.ic.utility.IO.BasePath;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Vertex;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JToggleButton;

/**
 * Class responsible for the PROLOG implementation
 * @author Kohwalter
 */
public class GuiProlog {
    
    /**
     * Method to enable the prolog functions
     * @param prolog is the prolog implementation for the automatic inference
     * @param prologIsInitialized is the boolean to check if the prolog can be activated
     * @param InitPrologButton is the boolean to check if the prolog can be activated
     */
    public static void InitializeProlog(PrologInference prolog, boolean prologIsInitialized, JToggleButton InitPrologButton)
    {
        if(InitPrologButton.isSelected() && !prologIsInitialized)
        {
            prologIsInitialized = true;
            prolog.Init();
        }
    }
    
    /**
     * Method to generate the prolog facts using the XML-Inference
     * @param initialGraph
     * @param file
     * @param demo 
     */
    public static void GeneratePrologFacts(boolean initialGraph, File file, String demo)
    {
        if(initialGraph)
        {
            file = new File(BasePath.getBasePathForClass(GuiProlog.class) + demo);
            initialGraph = false;
        }
        ConvertXML(file);
        System.out.println("Finished Converting to Prolog");
    }
    
    /**
     * Method to convert the XML to prolog facts
     * @param file the XML file for conversion
     */
    private static void ConvertXML(File file)
    {
        XMLConverter xmlConv = new XMLConverter();
        xmlConv.ConvertXMLtoProlog(file);
    }
    
    /**
     * Method to apply the similarity collapse in the graph
     * @param InitPrologButton is the boolean to check if the prolog is active
     * @param prolog is the prolog implementation
     * @param variables
     * @param edgeType is the type of edge used for the collapse
     */
    public static void SimilarityCollapse(boolean InitPrologButton, PrologInference prolog, Variables variables, String edgeType)
    {
        if(InitPrologButton)
        {
            System.out.println("Starting Prolog Inference: " + edgeType);
            GuiButtons.Reset(variables);
            String list;
            list = prolog.QueryCollapse((String)StatusFilterBox.getSelectedItem(), edgeType);
            System.out.println("Collapsing...");
            variables.collapser.CollapseIrrelevant(variables, list);
            System.out.println("Finished Collapsing");
        }
        else {
            GuiButtons.Reset(variables);
            String list = QueryCollapse((String)StatusFilterBox.getSelectedItem(), variables);
            variables.collapser.CollapseIrrelevant(variables, list);
            System.out.println("Finished Collapsing");
        }
    }
    
    public static String QueryCollapse(String attribute, Variables variables) {
        Map<String, String> processedVertices = new HashMap<>();
        ArrayList <ConcurrentHashMap<String, Object>> collapseGroups = new ArrayList<>();

        // -----------------------------
        // Standard Deviation
        // -----------------------------
        double std = std(variables, attribute);
//        System.out.println("STD = " + std);
        
        // -----------------------------
        // Similarity configuration
        // -----------------------------
        double similarityThreshold = 0.5;
        String defaultError = "0";
        Map<String, AttributeErrorMargin> restrictionList = new HashMap<>();
        AttributeErrorMargin epsilon;
        epsilon = new AttributeErrorMargin(attribute, "" + std);
        restrictionList.put(attribute, epsilon);
        GraphMatching combiner = new GraphMatching(restrictionList, similarityThreshold, defaultError, 0);
        
        // -----------------------------
        // Create 1st order clusters
        // -----------------------------
        // Run through all vertices in the graph
        createCollapseClusters(variables, processedVertices, collapseGroups, combiner);
        
        // -----------------------------
        // Break 1st order clusters
        // -----------------------------
        breakCollapseClusters(collapseGroups, combiner);
        
        // -----------------------------
        // Return Collapse Clusters
        // -----------------------------
//        System.out.println(": L = " + collapseList);
        return printCollapseGroups(collapseGroups);
    }
    
    /**
     * Neighborhood clustering heuristic
     * Cluster similar vertices into the same cluster as long as the difference between all elements in the cluster is within acceptable parameters
     * @param v1
     * @param variables
     * @param combiner
     * @param cg
     * @param processedVertices 
     */
    private static void Neighborhood(Object v1, Variables variables, GraphMatching combiner, ConcurrentHashMap<String, Object> cg, Map<String, String> processedVertices) {
        for (Object v2 : variables.graph.getNeighbors(v1)) {
            String id2 = ((Vertex) v2).getID();
            if(!processedVertices.containsKey(id2)) {
                if(combiner.isSimilar((Vertex)v1, (Vertex)v2)) {
                    if(cg.isEmpty())
                        cg.put(((Vertex) v1).getID(), v1);
                    boolean isSimilar = true;
                    for(Object v3 : cg.values()) {
                        if(!combiner.isSimilar((Vertex)v2, (Vertex)v3)) {
                            isSimilar = false;
                        }
                    }
                    if(isSimilar) {
                        processedVertices.put(id2, id2);
                        cg.put(id2, v2);
                        Neighborhood(v2, variables, combiner, cg, processedVertices);
                    }
                }
            }
        }
    }
    
    /**
     * Computes the standard deviation for the attribute
     * @param variables
     * @param attribute
     * @return 
     */
    private static double std(Variables variables, String attribute) {
        ArrayList <Float> values = new ArrayList<>();
        for (Object v1 : variables.graph.getVertices()) {
            float val = ((Vertex) v1).getAttributeValueFloat(attribute);
            if(!(val != val)) {
                values.add(val);
                
            }               
        }
        float[] floatArray = new float[values.size()];
        int i = 0;

        for (Float f : values) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return Utils.stdev(floatArray) * 1;
    }
    
    /**
     * Method to generate the correct format to pass to the Collapser class
     * @param collapseGroups
     * @return 
     */
    private static String printCollapseGroups(ArrayList <ConcurrentHashMap<String, Object>> collapseGroups) {
        String collapseList = "";
//        ArrayList <String> values;
        for(ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
//            values = new ArrayList<>();
            if(subGraph.size() > 1) {
                for(Object v1 : subGraph.values()) {
                    String id1 = ((Vertex) v1).getID();
                    collapseList += "," + id1;
//                    values.add(id1);
                }
//                Collections.sort(values);
//                System.out.println(values.toString());
                collapseList += " ";
            }
        }
        return collapseList;
    }
    
    /**
     * Generate clusters based on the Neighborhood heuristic
     * @param variables
     * @param processedVertices
     * @param collapseGroups
     * @param combiner 
     */
    private static void createCollapseClusters(Variables variables, Map<String, String> processedVertices, ArrayList <ConcurrentHashMap<String, Object>> collapseGroups, GraphMatching combiner) {
        ConcurrentHashMap<String, Object> cg;
        for (Object v1 : variables.graph.getVertices()) {
            String id1 = ((Vertex) v1).getID();
            if(!processedVertices.containsKey(id1)) {
                cg = new ConcurrentHashMap<>();
                processedVertices.put(id1, id1);
                Neighborhood(v1, variables, combiner, cg, processedVertices);
                collapseGroups.add(cg);
            }
        }
    }
    
    // Not used so far.
    private static void breakCollapseClusters(ArrayList <ConcurrentHashMap<String, Object>> collapseGroups, GraphMatching combiner) {
        for(ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
            boolean notSimilar = false;
            // Detect if there is any similarity problem within the same group
            for(Object v1 : subGraph.values()) {
                
                for(Object v2 : subGraph.values()) {
                    if(!combiner.isSimilar((Vertex)v1, (Vertex)v2)) {
                        System.out.println("Not Similar: " + ((Vertex) v1).getID() + " " + ((Vertex) v2).getID());
                        notSimilar = true;
                    }
                }
            }
            // There is a similarity problem, then we need to resolve it
            if(notSimilar) {
                // Do something to fix it
            }
        }
    }
}
