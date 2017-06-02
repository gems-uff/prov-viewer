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
package br.uff.ic.provviewer.Inference;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kohwalter
 */
public class AutomaticInference {
    
    float STD_QUANTITY = 3;
    int MINIMUM_SIZE = 10;
    float smallClusterError = 3;
    boolean isUpdating = false;
    boolean isRestrictingVariation = false;
    GraphMatching combiner;
    
    public ArrayList<Object> visitList = new ArrayList<>();
    public DirectedGraph<Object, Edge> graph;
    public ArrayList<ConcurrentHashMap<String, Object>> resultList = new ArrayList<>();
    
    // For the experiment only
    String attribute;
    double e;
    boolean testing = false;
    
    /**
     * Constructor used when VE is set as True
     * @param combiner
     * @param g is the graph
     * @param minSize is the minimum cluster size
     * @param thresholdIncrease is the epsilon modifier before reaching the minimum cluster size
     * @param std is the epsilon used by the algorithms
     */
    public AutomaticInference(GraphMatching combiner, DirectedGraph<Object, Edge> g, int minSize, float thresholdIncrease, float std) {
        MINIMUM_SIZE = minSize;
        smallClusterError = thresholdIncrease;
        STD_QUANTITY = std;
        graph = g;
        resultList.clear();
        visitList.clear();
        this.combiner = combiner;
    }
    
    /**
     * Constructor used for when VE is set as False
     * @param combiner
     * @param g is the graph
     */
    public AutomaticInference(GraphMatching combiner, DirectedGraph<Object, Edge> g) {
        MINIMUM_SIZE = 1;
        smallClusterError = 1;
        STD_QUANTITY = 1;
        graph = g;
        resultList.clear();
        visitList.clear();
        this.combiner = combiner;
    }
    
    /**
     * Function used to optimize calculations when using only one attribute for the similarity
     * @param att is the single attribute used for the similarity
     * @param epsilon is the STD of the attribute * the epsilon modifier
     */
    public void setSingleAttributeOptimization(String att, double epsilon) {
        attribute = att;
        e = epsilon;
        testing = true;
    }
    
    public void setTestingOn() {
        testing = true;
    }
    
    public void setTestingOff() {
        testing = false;
    }
    
    /**
     * Method to generate the correct format to pass to the Collapser class
     *
     * @param collapseGroups
     * @return
     */
    public String printCollapseGroups(ArrayList<ConcurrentHashMap<String, Object>> collapseGroups) {
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
//    private void getNeighborhood(Object v1, Map<String, Object[]> graph, GraphMatching combiner, ConcurrentHashMap<String, Object> cg, Map<String, String> processedVertices) {
//        for (Object v2 : graph.get(((Vertex) v1).getID())) {
//            String id2 = ((Vertex) v2).getID();
//            if (!processedVertices.containsKey(id2)) {
//                if(isUpdating)
//                    updateError(combiner, cg);
//                if (combiner.isSimilar((Vertex) v1, (Vertex) v2)) {
//                    boolean isSimilar = true;
//                    if(isRestrictingVariation) {
//                        
//                        for (Object v3 : cg.values()) {
//                            if (!combiner.isSimilar((Vertex) v2, (Vertex) v3)) {
//                                isSimilar = false;
//                            }
//                        }
//                    }
//                    if (isSimilar) {
//                        processedVertices.put(id2, id2);
//                        cg.put(id2, v2);
//                        getNeighborhood(v2, graph, combiner, cg, processedVertices);
//                    }
//                }
//            }
//        }
//    }
     
    private void updateError(ConcurrentHashMap<String, Object> cg) {
        if (cg.size() > 2) {
//            System.out.println("Updating error");
            Map<String, AttributeErrorMargin> error = combiner.getRestrictionList();
            for (String e : error.keySet()) {
//                System.out.println("Old error: " + error.get(e).getValue());
                float std = Utils.std(cg.values(), e) * STD_QUANTITY;
                if (cg.size() < MINIMUM_SIZE) {
                    std *= smallClusterError;
                }
                AttributeErrorMargin newError = new AttributeErrorMargin(e, Float.toString(std));
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
     * @param updateError
     * @param verifyWithinCluster
     * @return the list of clusters
     */
//    public ArrayList<ConcurrentHashMap<String, Object>> cluster(DirectedGraph<Object, Edge> graph, GraphMatching combiner, boolean updateError, boolean verifyWithinCluster) {
//        isUpdating = updateError;
//        isRestrictingVariation = verifyWithinCluster;
//        ArrayList<ConcurrentHashMap<String, Object>> clusters = new ArrayList<>();
//        ConcurrentHashMap<String, Object> cluster;
//        Map<String, String> visited = new HashMap<>();
////        Map<String, AttributeErrorMargin> error = new HashMap<>(combiner.getRestrictionList());
//        Map<String, Object[]> neighbors = new HashMap<>();
//        for(Object v : graph.getVertices()) {
//            Object[] neigh = graph.getNeighbors(v).toArray();
//            neighbors.put(((Vertex) v).getID(), neigh);
//        }
//        for (Object v1 : graph.getVertices()) {
//            String id1 = ((Vertex) v1).getID();
//            if (!visited.containsKey(id1)) {
//                cluster = new ConcurrentHashMap<>();
//                visited.put(id1, id1);
//                cluster.put(id1, v1);
//                getNeighborhood(v1, neighbors, combiner, cluster, visited);
////                combiner.setRestrictionList(error);
//                clusters.add(cluster);
//            }
//        }
////        breakCollapseClusters(clusters, combiner);
////        System.out.println(printCollapseGroups(clusters));
////        return printCollapseGroups(clusters);
//        return clusters;
//    }
    
    public ArrayList<ConcurrentHashMap<String, Object>> applySimilarity(boolean updateError, boolean verifyWithinCluster) {
        return applySimilarity(updateError, verifyWithinCluster, false);
    }
    public ArrayList<ConcurrentHashMap<String, Object>> applySimilarity(boolean updateError, boolean verifyWithinCluster, boolean isConsideringOnlyDirectNeighbors) {
        isUpdating = updateError;
        isRestrictingVariation = verifyWithinCluster;
        
        for (Object p : graph.getVertices()) {
            if(!isVisited(p)) {
                visited(p);
                ConcurrentHashMap<String, Object> c = new ConcurrentHashMap<>();
                    c.put(((Vertex) p).getID(), p);
                ArrayList<Object> n;
                if(isConsideringOnlyDirectNeighbors)
                    n = getNeighbours(p, c);
                else
                    n = getNeighboursEntireGraph(p, c);
                expandCluster(p, n, c, c);
                resultList.add(c);
            }
        }
        return resultList;
    }
    
    private void expandCluster(Object p, ArrayList<Object> n, ConcurrentHashMap<String, Object> c, ConcurrentHashMap<String, Object> cg) {
//        c.put(((Vertex) p).getID(), p);
        int ind = 0;
        while(n.size() > ind){
            Object point = n.get(ind);
            if(!isVisited(point)) {
                visited(point);
//                c.add(point);
                    c.put(((Vertex) point).getID(), point);
                ArrayList<Object> n2 = getNeighbours(point, cg);
                n = merge(n, n2);
            }
            ind++;
        }
    }
    
    private ArrayList<Object> merge(ArrayList<Object> a, ArrayList<Object> b) {

        Iterator<Object> it5 = b.iterator();
        while (it5.hasNext()) {
            Object t = it5.next();
            if (!a.contains(t)) {
                a.add(t);
            }
        }
        return a;
    }
    
    /**
     * Method to do normal clustering, dicarding vertex linkage
     * @param current is the current vertex
     * @param cg
     * @return 
     */
    private ArrayList<Object> getNeighboursEntireGraph(Object current, ConcurrentHashMap<String, Object> cg) {
        ArrayList<Object> neighbor = new ArrayList<>();
        Collection<Object> points = graph.getVertices();
        for(Object point : points) {
            // TODO: Revert to original getDistance
            if(!point.equals(current)) {
                if(testing) {
                    if(getDistanceSingleAttribute(current, point, cg)) {
                        neighbor.add(point);
                    }
                }
                else {
                    if(getDistance(current, point, cg)) {
                        neighbor.add(point);
                    }
                }
            }
            
        }
        return neighbor;
    }
    
    /**
     * Method to do the similarity collapse considering only the vertex neighbors
     * @param current
     * @param cg
     * @return 
     */
    private ArrayList<Object> getNeighbours(Object current, ConcurrentHashMap<String, Object> cg) {
        ArrayList<Object> neighbor = new ArrayList<>();
        Collection<Object> points = graph.getNeighbors(current);
        for(Object point : points) {
            // TODO: Revert to original getDistance
            if(testing) {
                if(getDistanceSingleAttribute(current, point, cg)) {
                    neighbor.add(point);
                }
            }
            else {
                if(getDistance(current, point, cg)) {
                    neighbor.add(point);
                }
            }
            
        }
        return neighbor;
    }
    
    private boolean getDistance(Object p, Object q, ConcurrentHashMap<String, Object> cg) {
        if (isUpdating) {
            updateError(cg);
        }

        if (combiner.isSimilar((Vertex) p, (Vertex) q)) {
            boolean isSimilar = true;
            if (isRestrictingVariation) {

                for (Object v3 : cg.values()) {
                    if (!combiner.isSimilar((Vertex) q, (Vertex) v3)) {
                        isSimilar = false;
                        break;
                    }
                }
            }
            return isSimilar;
        }
        return false;
    }
    
    private boolean getDistanceSingleAttribute(Object p, Object q, ConcurrentHashMap<String, Object> cg) {
        if (isUpdating) {
            updateErrorSingleAttribute(cg);
        }

        if (isSimilarSingleAttribute((Vertex) p, (Vertex) q) <= e) {
            boolean isSimilar = true;
            if (isRestrictingVariation) {

                for (Object v3 : cg.values()) {
                    if ((isSimilarSingleAttribute((Vertex) q, (Vertex) v3) * 1.0) > e) {
                        isSimilar = false;
                        break;
                    }
                }
            }
            return isSimilar;
        }
        return false;
    }
    
    private double isSimilarSingleAttribute(Object p, Object q) {
        double distance = Double.POSITIVE_INFINITY;
        if(!((Vertex)p).getNodeType().equalsIgnoreCase(((Vertex)q).getNodeType()))
            return distance;
        if(Utils.tryParseFloat(((Vertex)p).getAttributeValue(attribute))) {
            double dx = ((Vertex)p).getAttributeValueFloat(attribute) - ((Vertex)q).getAttributeValueFloat(attribute);
            distance = Math.sqrt(dx * dx);
        } else {
            String v1 = ((Vertex)p).getAttributeValue(attribute);
            String v2 = ((Vertex)q).getAttributeValue(attribute);
            if(v1.equalsIgnoreCase(v2) && !v1.equalsIgnoreCase(VariableNames.UnknownValue))
                distance = 0;
        }
        return distance;
    }
    
    private void updateErrorSingleAttribute(ConcurrentHashMap<String, Object> cg) {
        if (cg.size() > 2) {
//            System.out.println("Updating error");
            float std = Utils.std(cg.values(), attribute) * STD_QUANTITY;
            if (cg.size() < MINIMUM_SIZE) {
                std *= smallClusterError;
            }
            e = std;
        }
    }
    
    private boolean isVisited(Object c) {
        if (visitList.contains(c)) {
            return true;
        } else {
            return false;
        }
    }
    private void visited(Object d) {
        visitList.add(d);
    }
}
