/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Inference;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kohwalter
 */
public class AutomaticInference {
    
    static int STD_QUANTITY = 3;
    static int MINIMUM_SIZE = 10;
    static int smallClusterError = 3;
    static boolean isUpdating = false;
    static boolean isRestrictingVariation = false;
    
    
    public AutomaticInference(int minSize, int thresholdIncrease, int std) {
        MINIMUM_SIZE = minSize;
        smallClusterError = thresholdIncrease;
        STD_QUANTITY = std;
    }
    
    /**
     * Method to generate the correct format to pass to the Collapser class
     *
     * @param collapseGroups
     * @return
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

    // Not used so far.
//    public static void breakCollapseClusters(ArrayList<ConcurrentHashMap<String, Object>> collapseGroups, GraphMatching combiner) {
//        for (ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
//            boolean notSimilar = false;
//            for (Object v1 : subGraph.values()) {
//                for (Object v2 : subGraph.values()) {
//                    if (!combiner.isSimilar((Vertex) v1, (Vertex) v2)) {
//                        System.out.println("Not Similar: " + ((Vertex) v1).getID() + " " + ((Vertex) v2).getID());
//                        notSimilar = true;
//                    }
//                }
//            }
//            if (notSimilar) {
//            }
//        }
//    }

    

    /**
     * Neighborhood clustering heuristic Cluster similar vertices into the same
     * cluster as long as the difference between all elements in the cluster is
     * within acceptable parameters
     *
     * @param v1
     * @param graph
     * @param combiner
     * @param cg
     * @param processedVertices
     */
    private static void getNeighborhood(Object v1, DirectedGraph<Object, Edge> graph, GraphMatching combiner, ConcurrentHashMap<String, Object> cg, Map<String, String> processedVertices) {
        for (Object v2 : graph.getNeighbors(v1)) {
            String id2 = ((Vertex) v2).getID();
            if (!processedVertices.containsKey(id2)) {
                if(isUpdating)
                    updateError(combiner, cg);
                if (combiner.isSimilar((Vertex) v1, (Vertex) v2)) {
                    boolean isSimilar = true;
                    if(isRestrictingVariation) {
                        
                        for (Object v3 : cg.values()) {
                            if (!combiner.isSimilar((Vertex) v2, (Vertex) v3)) {
                                isSimilar = false;
                            }
                        }
                    }
                    if (isSimilar) {
                        processedVertices.put(id2, id2);
                        cg.put(id2, v2);
                        getNeighborhood(v2, graph, combiner, cg, processedVertices);
                    }
                }
            }
        }
    }
    
    private static void updateError(GraphMatching combiner, ConcurrentHashMap<String, Object> cg) {
        if (cg.size() > 2) {
//            System.out.println("Updating error");
            Map<String, AttributeErrorMargin> error = combiner.getRestrictionList();
            for (String e : error.keySet()) {
//                System.out.println("Old error: " + error.get(e).getValue());
                double std = Utils.std(cg.values(), e) * STD_QUANTITY;
                if (cg.size() < MINIMUM_SIZE) {
                    std *= smallClusterError;
                }
                AttributeErrorMargin newError = new AttributeErrorMargin(e, "" + std);
                error.put(e, newError);
//                System.out.println("New error: " + error.get(e).getValue());
            }
            combiner.setRestrictionList(error);
        }
    }
    
    /**
     * Generate clusters based on the Neighborhood heuristic
     *
     * @param graph
     * @param combiner
     * @return the list of clusters
     */
    public static String cluster(DirectedGraph<Object, Edge> graph, GraphMatching combiner, boolean updateError, boolean verifyWithinCluster) {
        isUpdating = updateError;
        isRestrictingVariation = verifyWithinCluster;
        ArrayList<ConcurrentHashMap<String, Object>> clusters = new ArrayList<>();
        ConcurrentHashMap<String, Object> cluster;
        Map<String, String> visited = new HashMap<>();
        Map<String, AttributeErrorMargin> error = new HashMap<String,AttributeErrorMargin>(combiner.getRestrictionList());
        for (Object v1 : graph.getVertices()) {
            String id1 = ((Vertex) v1).getID();
            if (!visited.containsKey(id1)) {
                cluster = new ConcurrentHashMap<>();
                visited.put(id1, id1);
                cluster.put(id1, v1);
                getNeighborhood(v1, graph, combiner, cluster, visited);
                combiner.setRestrictionList(error);
                clusters.add(cluster);
            }
        }
//        breakCollapseClusters(clusters, combiner);
//        System.out.println(printCollapseGroups(clusters));
        return printCollapseGroups(clusters);
    }
}
