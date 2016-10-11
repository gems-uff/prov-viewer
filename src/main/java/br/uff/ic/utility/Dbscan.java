package br.uff.ic.utility;


import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
PSEUDOCODE
-------------------------------------

DBSCAN(D, eps, MinPts)
   C = 0
   for each unvisited point P in dataset D
      mark P as visited
      N = getNeighbors (P, eps)
      if sizeof(N) < MinPts
         mark P as NOISE
      else
         C = next cluster
         expandCluster(P, N, C, eps, MinPts)
          
expandCluster(P, N, C, eps, MinPts)
   add P to cluster C
   for each point P' in N 
      if P' is not visited
         mark P' as visited
         N' = getNeighbors(P', eps)
         if sizeof(N') >= MinPts
            N = N joined with N'
      if P' is not yet member of any cluster
         add P' to cluster C
		 
---------------------------------------
*/

public class Dbscan {

    public double e;
    public int minpt;
    public ArrayList<Object> visitList = new ArrayList<>();
    public ArrayList<ConcurrentHashMap<String, Object>> resultList = new ArrayList<>();
//    public ArrayList<Object> neighbours;
    public DirectedGraph<Object, Edge> graph;
    String attribute;
    
    public Dbscan(DirectedGraph<Object, Edge> g, String att, double eps, int minp) {
        resultList.clear();
        visitList.clear();
        graph = g;
        attribute = att;
        e = eps;
        minpt = minp;
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
    
    private ArrayList<Object> getNeighbours(Object current) {
        ArrayList<Object> neighbor = new ArrayList<>();
        Collection<Object> points = graph.getNeighbors(current);
        for(Object point : points) {
            if(getDistance(current, point) <= e) {
                neighbor.add(point);
            }
        }
        return neighbor;
    }
    
    private double getDistance(Object p, Object q) {

        double dx = ((Vertex)p).getAttributeValueFloat(attribute) - ((Vertex)q).getAttributeValueFloat(attribute);

        double distance = Math.sqrt(dx * dx);

        return distance;
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
    
    public static String printCollapseGroups(ArrayList<List> collapseGroups) {
        String collapseList = "";
        for (List subGraph : collapseGroups) {
            if (subGraph.size() > 0) {
                for (Object v1 : subGraph.toArray()) {
                    String id1 = ((Vertex) v1).getID();
                    collapseList += "," + id1;
                }
                collapseList += " ";
            }
        }
        return collapseList;
    }
    
    public ArrayList<ConcurrentHashMap<String, Object>> applyDbscan() {
//        String result = "";
        for (Object p : graph.getVertices()) {
            if(!isVisited(p)) {
                visited(p);
                
                ArrayList<Object> n = getNeighbours(p);
//                if (n.size() >= minpt) {
                    ConcurrentHashMap<String, Object> c = new ConcurrentHashMap<>();
                    expandCluster(p, n, c);
                    resultList.add(c);
//                }
            }
        }
        return resultList;
//        return printCollapseGroups(resultList);
    }

    private void expandCluster(Object p, ArrayList<Object> n, ConcurrentHashMap<String, Object> c) {
        c.put(((Vertex) p).getID(), p);
        int ind=0;
        while(n.size()>ind){
            Object point = n.get(ind);
            if(!isVisited(point)) {
                visited(point);
//                c.add(point);
                c.put(((Vertex) point).getID(), point);
                ArrayList<Object> n2 = getNeighbours(point);
                if (n2.size() >= minpt) {
                    n = merge(n, n2);
                }
            }
            ind++;
//            System.out.println("N: " + n.size());
//            System.out.println("IND: " + ind);
        }
        
//        System.out.println("Expand Cluster");
//        printArray(n);
        
    }
    
    public void printClusters(String list) {
        List<String> clusters = new ArrayList<>();

        String[] elements = list.split(" ");
        clusters.addAll(Arrays.asList(elements));

        for (String cluster : clusters) {
            boolean computedCluster = false;
            System.out.println("Cluster: " + cluster);
        }
    }
    
    public String printClusters(ArrayList<ConcurrentHashMap<String, Object>> collapseGroups) {
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
}
