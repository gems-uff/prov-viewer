/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Inference;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.Variables;
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
     * @param graph
     * @param attribute
     * @return
     */
    public static double std(DirectedGraph<Object, Edge> graph, String attribute) {
        ArrayList<Float> values = new ArrayList<>();
        for (Object v1 : graph.getVertices()) {
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
     * @param graph
     * @param combiner
     * @param cg
     * @param processedVertices
     */
    private static void getNeighborhood(Object v1, DirectedGraph<Object, Edge> graph, GraphMatching combiner, ConcurrentHashMap<String, Object> cg, Map<String, String> processedVertices) {
        for (Object v2 : graph.getNeighbors(v1)) {
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
                        getNeighborhood(v2, graph, combiner, cg, processedVertices);
                    }
                }
            }
        }
    }
    
    /**
     * Generate clusters based on the Neighborhood heuristic
     *
     * @param graph
     * @param combiner
     * @return the list of clusters
     */
    public static String cluster(DirectedGraph<Object, Edge> graph, GraphMatching combiner) {
        ArrayList<ConcurrentHashMap<String, Object>> clusters = new ArrayList<>();
        ConcurrentHashMap<String, Object> cluster;
        Map<String, String> visited = new HashMap<>();
        for (Object v1 : graph.getVertices()) {
            String id1 = ((Vertex) v1).getID();
            if (!visited.containsKey(id1)) {
                cluster = new ConcurrentHashMap<>();
                visited.put(id1, id1);
                getNeighborhood(v1, graph, combiner, cluster, visited);
                clusters.add(cluster);
            }
        }
        breakCollapseClusters(clusters, combiner);
        return printCollapseGroups(clusters);
    }

    
    /* DBSCAN implementation  */
    
    /** Distance Metric **/
    private static ArrayList<Object> getNeighbors(Object current,
            Variables variables,
            GraphMatching combiner) {

        ArrayList<Object> neighbors = new ArrayList<>();

        for (Object v2 : variables.graph.getNeighbors(current)) {
            String id2 = ((Vertex) v2).getID();
//            if (!processedVertices.containsKey(id2)) {
            if (combiner.isSimilar((Vertex) current, (Vertex) v2)) {
                boolean isSimilar = true;
                for (Object v3 : neighbors.toArray()) {
                    if (!combiner.isSimilar((Vertex) v2, (Vertex) v3)) {
                        isSimilar = false;
                    }
                }
                if (isSimilar) {
//                        processedVertices.put(id2, id2);
                    neighbors.add(v2);
                }
            }
//            }
        }
        return neighbors;
    }
    
    /**
     * Expands the cluster to include density-reachable items.
     *
     **/
    private static ArrayList<Object> expandCluster(ArrayList<Object> cluster,
            Object vertex,
            ArrayList<Object> neighbors,
            Variables variables,
            GraphMatching combiner,
            Map<String, String> visited) {

        String id1 = ((Vertex) vertex).getID();
        cluster.add(vertex);
        visited.put(id1, id1);
        ArrayList<Object> seeds = new ArrayList<>(neighbors);
        int index = 0;
        while (index < seeds.size()) {
            final Object current = seeds.get(index);
            String idCurrent = ((Vertex) current).getID();
            // only check non-visited points
            if (!visited.containsKey(idCurrent)) {
                final ArrayList<Object> currentNeighbors = getNeighbors(current, variables, combiner);
                int minPts = 1;
                if (currentNeighbors.size() >= minPts) {
                    seeds = merge(seeds, currentNeighbors);
                }
                visited.put(idCurrent, idCurrent);
                cluster.add(current);
            }

            index++;
        }
        return cluster;

    }

    /**
     * Merges two lists together.
     *
     * @param one first list
     * @param two second list
     * @return merged lists
     */
    private static ArrayList<Object> merge(final ArrayList<Object> one, final ArrayList<Object> two) {
        final ArrayList<Object> oneSet = new ArrayList<>(one);
        for (Object item : two) {
            if (!oneSet.contains(item)) {
                one.add(item);
            }
        }
        return one;
    }

    /**
     * Performs DBSCAN cluster analysis.
     * @param variables
     * @param combiner
     * @return the list of clusters
     */
    public static String dbscan(Variables variables, GraphMatching combiner) {

        final ArrayList<Object> clusters = new ArrayList<>();
        Map<String, String> visited = new HashMap<>();

        for (Object current : variables.graph.getVertices()) {
            String id1 = ((Vertex) current).getID();
            if (!visited.containsKey(id1)) {
//                clusters = new ConcurrentHashMap<>();
                ArrayList<Object> neighbors = getNeighbors(current, variables, combiner);
                int minPts = 1;
                if (neighbors.size() >= minPts) {
                    // DBSCAN does not care about center points
                    final ArrayList<Object> cluster = new ArrayList<>();
                    clusters.add(expandCluster(cluster, current, neighbors, variables, combiner, visited));
                } else {
                    visited.put(id1, id1);
                }
            }
        }

        return printClusters(clusters);
    }

    /**
     * Method to generate the correct format to pass to the Collapser class
     *
     * @param clusters
     * @return
     */
    public static String printClusters(ArrayList<Object> clusters) {
        String collapseList = "";
        for (Object subGraph : clusters) {
            if (((ArrayList<Object>) subGraph).size() > 1) {
                for (Object v1 : ((ArrayList<Object>) subGraph).toArray()) {
                    String id1 = ((Vertex) v1).getID();
                    collapseList += "," + id1;
                }
                collapseList += " ";
            }
        }
        return collapseList;
    }
}
