/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import static br.uff.ic.provviewer.GUI.GuiInference.ColorSchemeCollapse;
import br.uff.ic.utility.Dbscan;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kohwalter
 */
public class ClusteringEvaluator {

//    int NUMBER_OF_ORACLE_GRAPHS = 10;
//    int NUMBER_OF_NOISE_GRAPHS = 10;
//    int MAX_NOISE_GRAPH_SIZE = 10;
//    double averageFmeasure = 0;
    double noiseProbability = 1.0F;
//    double averageRecall = 0;
//    double noiseFactor = 3.0F;
//    double averagePrecision = 0;
    
    ArrayList<Double> p_similarity = new ArrayList<>();
    ArrayList<Double> r_similarity = new ArrayList<>();
    ArrayList<Double> f_similarity = new ArrayList<>();
    
//    ArrayList<Double> p_similarity_TF = new ArrayList<>();
//    ArrayList<Double> r_similarity_TF = new ArrayList<>();
//    ArrayList<Double> f_similarity_TF = new ArrayList<>();
//    
//    ArrayList<Double> p_similarity_TT = new ArrayList<>();
//    ArrayList<Double> r_similarity_TT = new ArrayList<>();
//    ArrayList<Double> f_similarity_TT = new ArrayList<>();
    
    
    ArrayList<Double> p_dbscan = new ArrayList<>();
    ArrayList<Double> r_dbscan = new ArrayList<>();
    ArrayList<Double> f_dbscan = new ArrayList<>();
    
    File file = new File("clusterEvaluation.txt");
    FileWriter fw;
    BufferedWriter bw;

    public void comparePRF(DirectedGraph<Object, Edge> oracle, String list, ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f) throws IOException {
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
//            bw.write("Cluster: " + cluster);
//            bw.newLine();
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

//        bw.write("=========================");
//        bw.newLine();
//        bw.write("Intersection: " + intersection);
//        bw.newLine();
//        bw.write("Retrieved Documents: " + retrievedDocuments);
//        bw.newLine();
//        bw.write("Relevant Documents: " + relevantDocuments);
//        bw.newLine();
//        bw.write("");
//        bw.newLine();
//        bw.write("Precision: " + precision);
//        bw.newLine();
//        bw.write("Recall: " + recall);
//        bw.newLine();
//        bw.write("F-Measure: " + fmeasure);
//        bw.newLine();
    }

    public void collapse(OracleGraph oracleGraph, int NUMBER_OF_ORACLE_GRAPHS, int NUMBER_OF_NOISE_GRAPHS, double INITIAL_NOISE_GRAPH_SIZE, double NOISE_INCREASE_NUMBER) throws IOException {
        int i = 0;
        int j = 0;
        int w = 1;
        int total_similarity = 0;
        int total_dbscan = 0;
        
        // if file doesnt exists, then create it
        if (!file.exists()) {
                file.createNewFile();
        }

        fw = new FileWriter(file.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        
        double noiseFactor = INITIAL_NOISE_GRAPH_SIZE;        
        for (j = 1; j <= NUMBER_OF_ORACLE_GRAPHS; j++) {
            bw.write("==============================================================");
            bw.newLine();
            bw.write("ORACLE NUMBER #" + j);
            bw.newLine();
//            System.out.println("ORACLE NUMBER #" + j);
            DirectedGraph<Object, Edge> oracle = oracleGraph.generateLinearGraph();
            bw.write("Oracle size: " + oracle.getVertexCount());
            bw.newLine();
            bw.write("NoiseGraph size: " + oracle.getVertexCount() * noiseFactor);
                bw.newLine();
//            bw.write("===============================");
            for (i = 0; i < NUMBER_OF_NOISE_GRAPHS; i++) {
//                bw.write("==============================================================");
//                bw.newLine();
//                bw.write("TEST NUMBER #" + i);
//                bw.newLine();
                NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute);
                DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + j + i);
//                bw.write("NoiseGraph size: " + noiseGraph.getVertexCount());
//                bw.newLine();

                SimilarityCollapse(oracleGraph, oracle, noiseGraph, true, false, p_similarity, r_similarity, f_similarity);
//                SimilarityCollapse(oracleGraph, oracle, noiseGraph, true, true, p_dbscan, r_dbscan, f_dbscan);
                dbscan(oracleGraph, oracle, noiseGraph);
             
            }
            printResults();
            noiseFactor += NOISE_INCREASE_NUMBER; 
            
            if(( j % 5 == 0)) {
                System.out.println("===============================");
                System.out.println("Bundle #" + w);
                System.out.println("Similarity: " + similarity);
                System.out.println("dbscan: " + dbscan);
                total_similarity += similarity;
                total_dbscan += dbscan;
                similarity = 0;
                dbscan = 0;
                w++;
            }
        }
//        printResults();
        bw.close();
        System.out.println("===============================");
        System.out.println("Final Result");
        System.out.println("Similarity: " + total_similarity);
        System.out.println("dbscan: " + total_dbscan);
    }
    
    private void SimilarityCollapse(OracleGraph oracleGraph, 
            DirectedGraph<Object, Edge> oracle, 
            DirectedGraph<Object, Edge> noiseGraph, 
            boolean updateError, 
            boolean verifyWithinCluster,
            ArrayList<Double> p,
            ArrayList<Double> r,
            ArrayList<Double> f) throws IOException {
//        bw.write("=========================");
//        bw.newLine();
//        bw.write("Similarity Collapse");
//        bw.newLine();
        String similarity = ColorSchemeCollapse(oracleGraph.attribute, noiseGraph, updateError, verifyWithinCluster);
        comparePRF(oracle, similarity, p, r, f);
    }
    
    private void dbscan(OracleGraph oracleGraph, DirectedGraph<Object, Edge> oracle, DirectedGraph<Object, Edge> noiseGraph) throws IOException {
//        bw.write("=========================");
//        bw.newLine();
//        bw.write("DBSCAN");
//        bw.newLine();
        double eps = Utils.std(noiseGraph.getVertices(), oracleGraph.attribute) * 1;
        Dbscan instance = new Dbscan(noiseGraph, oracleGraph.attribute, eps, 1);
        String dbscan = instance.applyDbscan(); 
        comparePRF(oracle, dbscan, p_dbscan, r_dbscan, f_dbscan);
//        System.out.println("=========================");
    }
    
    private void printResults() throws IOException {
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse");
        bw.newLine();
        printPrf(p_similarity, r_similarity, f_similarity);
//        bw.write("=========================");
//        bw.newLine();
//        bw.write("Similarity Collapse: Dynamic Error + No Internal Verification");
//        bw.newLine();
//        printPrf(p_similarity_TF, r_similarity_TF, f_similarity_TF);
//        bw.write("=========================");
//        bw.newLine();
//        bw.write("Similarity Collapse: Dynamic Error + Internal Verification");
//        bw.newLine();
//        printPrf(p_similarity_TT, r_similarity_TT, f_similarity_TT);
        bw.write("=========================");
        bw.newLine();
        bw.write("DBSCAN");
        bw.newLine();
        printPrf(p_dbscan, r_dbscan, f_dbscan);
        
        checkWinner();
        clearLists(p_similarity, r_similarity, f_similarity);
//        clearLists(p_similarity_TF, r_similarity_TF, f_similarity_TF);
//        clearLists(p_similarity_TT, r_similarity_TT, f_similarity_TT);
        clearLists(p_dbscan, r_dbscan, f_dbscan);
    }
    
    private void printPrf(ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f) throws IOException {
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
        
        bw.write(precision);
        bw.newLine();
        bw.write(recall);
        bw.newLine();
        bw.write(fmeasure);
        bw.newLine();
        
//        System.out.println(precision);
//        System.out.println(recall);
//        System.out.println(fmeasure);
        
    }
    
    private void clearLists(ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f) {
        p.clear();
        r.clear();
        f.clear();
    }
    
    int similarity = 0;
    int tf = 0;
    int tt = 0;
    int dbscan = 0;
    private void checkWinner() {
        similarity += isWinner(f_similarity, f_dbscan, f_dbscan);
//        tf += isWinner(f_similarity_TF, f_similarity, f_similarity_TT, f_dbscan);
//        tt += isWinner(f_similarity_TT, f_similarity_TF, f_similarity, f_dbscan);
        dbscan += isWinner(f_dbscan, f_similarity, f_similarity);
    }
    
    private int isWinner(ArrayList<Double> first, ArrayList<Double> second, ArrayList<Double> third) {
        int win = 0;
        if (Utils.mean(Utils.listToDoubleArray(first)) > Utils.mean(Utils.listToDoubleArray(second))) {
            if (Utils.mean(Utils.listToDoubleArray(first)) > Utils.mean(Utils.listToDoubleArray(third))) {
                    win = 1;
            }
        }
        return win;
    }
    
}
