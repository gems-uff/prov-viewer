/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import static br.uff.ic.provviewer.GUI.GuiInference.ColorSchemeCollapse;
import br.uff.ic.provviewer.Inference.AutomaticInference;
import br.uff.ic.utility.Dbscan;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public class ClusteringEvaluator {

    int NUMBER_OF_ORACLE_GRAPHS = 10;
    int NUMBER_OF_NOISE_GRAPHS = 10;
    int MAX_NOISE_GRAPH_SIZE = 10;
//    double averageFmeasure = 0;
    double noiseProbability = 1.0F;
//    double averageRecall = 0;
    double noiseFactor = 3.0F;
//    double averagePrecision = 0;
    
    ArrayList<Double> p_similarity = new ArrayList<>();
    ArrayList<Double> r_similarity = new ArrayList<>();
    ArrayList<Double> f_similarity = new ArrayList<>();
    ArrayList<Double> p_dbscan = new ArrayList<>();
    ArrayList<Double> r_dbscan = new ArrayList<>();
    ArrayList<Double> f_dbscan = new ArrayList<>();

    public void comparePRF(DirectedGraph<Object, Edge> oracle, String list, ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f) {
        List<String> clusters = new ArrayList<>();
        double relevantDocuments = oracle.getVertexCount();
        double retrievedDocuments;
        double intersection = 0;
        double precision;
        double recall;
        double fmeasure;
        String[] elements = list.split(" ");
        clusters.addAll(Arrays.asList(elements));
        retrievedDocuments = clusters.size();
        for (String cluster : clusters) {
            boolean computedCluster = false;
            System.out.println("Cluster: " + cluster);
            for (Object v : oracle.getVertices()) {
                String id = ((Vertex) v).getID();
                if (cluster.contains(id) && !computedCluster) {
                    computedCluster = true;
                    intersection++;
                }
            }
        }
        precision = intersection / retrievedDocuments;
        
        recall = intersection / relevantDocuments;
        fmeasure = 2 * (precision * recall) / (precision + recall);
        
        p.add(precision);
        r.add(recall);
        f.add(fmeasure);

        System.out.println("=========================");
        System.out.println("Intersection: " + intersection);
        System.out.println("Retrieved Documents: " + retrievedDocuments);
        System.out.println("Relevant Documents: " + relevantDocuments);
        System.out.println("");
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F-Measure: " + fmeasure);
        System.out.println("=========================");
    }

    public void collapse(OracleGraph oracleGraph) {
        int i = 0;
        int j = 0;
        for (j = 0; j < NUMBER_OF_ORACLE_GRAPHS; j++) {
            System.out.println("===============================");
            System.out.println("ORACLE NUMBER #" + j);
            DirectedGraph<Object, Edge> oracle = oracleGraph.generateLinearGraph();
            System.out.println("===============================");
            for (i = 0; i < NUMBER_OF_NOISE_GRAPHS; i++) {
                System.out.println("===============================");
                System.out.println("TEST NUMBER #" + i);
                NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute);
                noiseFactor = (Math.random() * MAX_NOISE_GRAPH_SIZE);
                DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + j + i);
                System.out.println("Oracle size: " + oracle.getVertexCount());
                System.out.println("NoiseGraph size: " + noiseGraph.getVertexCount());
                SimilarityCollapse(oracleGraph, oracle, noiseGraph);
                dbscan(oracleGraph, oracle, noiseGraph);
                
            }
        }
        printResults();
    }
    
    private void SimilarityCollapse(OracleGraph oracleGraph, DirectedGraph<Object, Edge> oracle, DirectedGraph<Object, Edge> noiseGraph) {
        System.out.println("=========================");
        System.out.println("Similarity Collapse");
        String similarity = ColorSchemeCollapse(oracleGraph.attribute, noiseGraph);
        comparePRF(oracle, similarity, p_similarity, r_similarity, f_similarity);
    }
    
    private void dbscan(OracleGraph oracleGraph, DirectedGraph<Object, Edge> oracle, DirectedGraph<Object, Edge> noiseGraph) {
        System.out.println("=========================");
        System.out.println("DBSCAN");
        double eps = AutomaticInference.std(noiseGraph, oracleGraph.attribute);
        Dbscan instance = new Dbscan(noiseGraph, oracleGraph.attribute, eps, 1);
        String dbscan = instance.applyDbscan(); 
        comparePRF(oracle, dbscan, p_dbscan, r_dbscan, f_dbscan);
        System.out.println("=========================");
    }
    
    private void printResults() {
        String precision = "";
        String recall = "";
        String fmeasure = "";
        System.out.println("=========================");
        System.out.println("Similarity Collapse");
        printPrf(p_similarity, r_similarity, f_similarity);
        System.out.println("=========================");
        System.out.println("DBSCAN");
        printPrf(p_dbscan, r_dbscan, f_dbscan);
    }
    
    private void printPrf(ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f) {
        String precision = "";
        String recall = "";
        String fmeasure = "";
        precision = "Precision> Mean: " + Utils.mean(Utils.listToDoubleArray(p)) + 
                " / STD:" + Utils.stdev(Utils.listToDoubleArray(p)) + 
                " / Min: " + Utils.minimumValue(Utils.listToDoubleArray(p));
        
        recall = "Recall> Mean: " + Utils.mean(Utils.listToDoubleArray(r)) + 
                " / STD:" + Utils.stdev(Utils.listToDoubleArray(r)) + 
                " / Min: " + Utils.minimumValue(Utils.listToDoubleArray(r));
        
        fmeasure = "F-Measure> Mean: " + Utils.mean(Utils.listToDoubleArray(f)) + 
                " / STD:" + Utils.stdev(Utils.listToDoubleArray(f)) + 
                " / Min: " + Utils.minimumValue(Utils.listToDoubleArray(f));
        
        System.out.println(precision);
        System.out.println(recall);
        System.out.println(fmeasure);
    }
    
}
