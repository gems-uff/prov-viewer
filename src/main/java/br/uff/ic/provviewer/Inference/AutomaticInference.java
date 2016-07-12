/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Inference;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Vertex;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kohwalter
 */
public class AutomaticInference {

    /**
     * Method to generate the correct format to pass to the Collapser class
     *
     * @param collapseGroups
     * @return
     */
    public static String printCollapseGroups(ArrayList<ConcurrentHashMap<String, Object>> collapseGroups) {
        String collapseList = "";
        for (ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
            if (subGraph.size() > 1) {
                for (Object v1 : subGraph.values()) {
                    String id1 = ((Vertex) v1).getID();
                    collapseList += "," + id1;
                }
                collapseList += " ";
            }
        }
        return collapseList;
    }

    // Not used so far.
    public static void breakCollapseClusters(ArrayList<ConcurrentHashMap<String, Object>> collapseGroups, GraphMatching combiner) {
        for (ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
            boolean notSimilar = false;
            for (Object v1 : subGraph.values()) {
                for (Object v2 : subGraph.values()) {
                    if (!combiner.isSimilar((Vertex) v1, (Vertex) v2)) {
                        System.out.println("Not Similar: " + ((Vertex) v1).getID() + " " + ((Vertex) v2).getID());
                        notSimilar = true;
                    }
                }
            }
            if (notSimilar) {
            }
        }
    }

    /**
     * Computes the standard deviation for the attribute
     *
     * @param variables
     * @param attribute
     * @return
     */
    public static double std(Variables variables, String attribute) {
        ArrayList<Float> values = new ArrayList<>();
        for (Object v1 : variables.graph.getVertices()) {
            float val = ((Vertex) v1).getAttributeValueFloat(attribute);
            if (!(val != val)) {
                values.add(val);
            }
        }
        float[] floatArray = new float[values.size()];
        int i = 0;
        for (Float f : values) {
            floatArray[i++] = (f != null ? f : Float.NaN);
        }
        return Utils.stdev(floatArray) * 1;
    }

    /**
     * Neighborhood clustering heuristic Cluster similar vertices into the same
     * cluster as long as the difference between all elements in the cluster is
     * within acceptable parameters
     *
     * @param v1
     * @param variables
     * @param combiner
     * @param cg
     * @param processedVertices
     */
    private static void Neighborhood(Object v1, Variables variables, GraphMatching combiner, ConcurrentHashMap<String, Object> cg, Map<String, String> processedVertices) {
        for (Object v2 : variables.graph.getNeighbors(v1)) {
            String id2 = ((Vertex) v2).getID();
            if (!processedVertices.containsKey(id2)) {
                if (combiner.isSimilar((Vertex) v1, (Vertex) v2)) {
                    if (cg.isEmpty()) {
                        cg.put(((Vertex) v1).getID(), v1);
                    }
                    boolean isSimilar = true;
                    for (Object v3 : cg.values()) {
                        if (!combiner.isSimilar((Vertex) v2, (Vertex) v3)) {
                            isSimilar = false;
                        }
                    }
                    if (isSimilar) {
                        processedVertices.put(id2, id2);
                        cg.put(id2, v2);
                        Neighborhood(v2, variables, combiner, cg, processedVertices);
                    }
                }
            }
        }
    }

    /**
     * Generate clusters based on the Neighborhood heuristic
     *
     * @param variables
     * @param processedVertices
     * @param collapseGroups
     * @param combiner
     */
    public static void createCollapseClusters(Variables variables, Map<String, String> processedVertices, ArrayList<ConcurrentHashMap<String, Object>> collapseGroups, GraphMatching combiner) {
        ConcurrentHashMap<String, Object> cg;
        for (Object v1 : variables.graph.getVertices()) {
            String id1 = ((Vertex) v1).getID();
            if (!processedVertices.containsKey(id1)) {
                cg = new ConcurrentHashMap<>();
                processedVertices.put(id1, id1);
                Neighborhood(v1, variables, combiner, cg, processedVertices);
                collapseGroups.add(cg);
            }
        }
    }

}
