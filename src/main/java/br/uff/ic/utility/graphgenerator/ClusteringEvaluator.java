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

    double noiseProbability = 1.0F;
    
    ArrayList<Double> p_similarity = new ArrayList<>();
    ArrayList<Double> r_similarity = new ArrayList<>();
    ArrayList<Double> f_similarity = new ArrayList<>();
      
    ArrayList<Double> p_dbscan = new ArrayList<>();
    ArrayList<Double> r_dbscan = new ArrayList<>();
    ArrayList<Double> f_dbscan = new ArrayList<>();
    
//    File file = new File("clusterEvaluation.txt");
//    FileWriter fw;
//    BufferedWriter bw;

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

    private DirectedGraph<Object, Edge> createOracleGraph(OracleGraph oracleGraph, String typeGraph) {
        if(typeGraph.equalsIgnoreCase("DAG"))
            return oracleGraph.generateDagGraph();
        else if(typeGraph.equalsIgnoreCase("TREE"))
            return oracleGraph.generateTreeGraph();
        else
            return oracleGraph.generateLinearGraph();
    }
    public void collapse(OracleGraph oracleGraph, 
            int NUMBER_OF_ORACLE_GRAPHS, 
            int NUMBER_OF_NOISE_GRAPHS, 
            double INITIAL_NOISE_GRAPH_SIZE, 
            double NOISE_INCREASE_NUMBER, 
            File file, 
            String typeGraph,
            double epsMod) throws IOException {
        int i = 0;
        int j = 0;
        int w = 1;
        int total_similarity = 0;
        int total_dbscan = 0;
        BufferedWriter bw;
        
        // if file doesnt exists, then create it
        if (!file.exists()) {
                file.createNewFile();
        }

        bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        
//        double epsMod = trainDBSCAN(oracleGraph, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, typeGraph);
        
        System.out.println("DBSCAN EPS MOD: " + epsMod);
        bw.write("DBSCAN EPS: " + epsMod);
        bw.newLine();
        double noiseFactor = INITIAL_NOISE_GRAPH_SIZE;        
        for (j = 1; j <= NUMBER_OF_ORACLE_GRAPHS; j++) {
            bw.write("==============================================================");
            bw.newLine();
            bw.write("ORACLE NUMBER #" + j);
            bw.newLine();
            DirectedGraph<Object, Edge> oracle;
            
            oracle = createOracleGraph(oracleGraph, typeGraph);
            
            bw.write("Oracle size: " + oracle.getVertexCount());
            bw.newLine();
            bw.write("NoiseGraph size: " + oracle.getVertexCount() * noiseFactor);
            bw.newLine();
            for (i = 0; i < NUMBER_OF_NOISE_GRAPHS; i++) {
//                bw.write("==============================================================");
//                bw.newLine();
//                bw.write("TEST NUMBER #" + i);
//                bw.newLine();
                NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute);
                DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + j + i);

                SimilarityCollapse(oracleGraph, oracle, noiseGraph, true, false, p_similarity, r_similarity, f_similarity);
                dbscan(oracleGraph, oracle, noiseGraph, epsMod, p_dbscan, r_dbscan, f_dbscan);
             
            }
            printResults(bw);
            bw.newLine();
            bw.write("Similarity wins: " + similarity);
            bw.newLine();
            bw.write("dbscan wins: " + dbscan);
            bw.newLine();
            total_similarity += similarity;
            total_dbscan += dbscan;
            similarity = 0;
            dbscan = 0;
            noiseFactor += NOISE_INCREASE_NUMBER; 
            
//            if(( j % 5 == 0)) {
//                System.out.println("===============================");
//                System.out.println("Bundle #" + w);
//                System.out.println("Similarity: " + similarity);
//                System.out.println("dbscan: " + dbscan);
//                w++;
//            }
        }
//        printResults();
        
        System.out.println("===============================");
        System.out.println("Final Result");
        System.out.println("Similarity: " + total_similarity);
        System.out.println("dbscan: " + total_dbscan);
        bw.write("===============================");
        bw.newLine();
        bw.write("Final Result");
        bw.newLine();
        bw.write("Similarity: " + total_similarity);
        bw.newLine();
        bw.write("dbscan: " + total_dbscan);
        bw.newLine();
        
        bw.close();
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
    
    public double trainDBSCAN(OracleGraph oracleGraph, 
            int NUMBER_OF_ORACLE_GRAPHS, 
            double INITIAL_NOISE_GRAPH_SIZE, 
            double NOISE_INCREASE_NUMBER,
            String typeGraph) throws IOException {
        
        double current_eps;
        double best_eps = 184.04; // 2.26
        double noiseFactor;
        ArrayList<Double> p = new ArrayList<>();
        ArrayList<Double> r = new ArrayList<>();
        ArrayList<Double> f = new ArrayList<>();
        ArrayList<Double> p2 = new ArrayList<>();
        ArrayList<Double> r2 = new ArrayList<>();
        ArrayList<Double> f2 = new ArrayList<>();
        int current = 0;
        int best = 0;
        System.out.println("Training");
        for (int w = 0; w < 1000; w++) {
            System.out.println("Training run: " + w);
            noiseFactor = INITIAL_NOISE_GRAPH_SIZE;
            current_eps = Math.random() * 400;
            for(int i = 0; i < NUMBER_OF_ORACLE_GRAPHS * 0.25; i++) {
                // Make oracle graph
                DirectedGraph<Object, Edge> oracle = createOracleGraph(oracleGraph, typeGraph);
                
                for (int j = 0; j < NUMBER_OF_ORACLE_GRAPHS * 0.25; j++) {
                    // Make noise graphs
                    NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute);
                    DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + j + i);
                    dbscan(oracleGraph, oracle, noiseGraph, best_eps, p, r, f);
                    dbscan(oracleGraph, oracle, noiseGraph, current_eps, p2, r2, f2);
                }
                noiseFactor += NOISE_INCREASE_NUMBER * 4;
                
                current += isWinner(f2, f, f);
                best += isWinner(f, f2, f2);
                clearLists(p, r, f);
                clearLists(p2, r2, f2);
            }
//            System.out.println("Current eps mod: " + current_eps);
//            System.out.println("Best eps mod: " + best_eps);
//            System.out.println("Current wins: " + current);
//            System.out.println("Best wins: " + best);
            if(current > best) {
                best_eps = current_eps;
//                System.out.println("New Best eps mod: " + best_eps);
            }
            current = 0;
            best = 0;
        }
        System.out.println("Best eps mod: " + best_eps);
        return best_eps;
    }
    
    private void dbscan(OracleGraph oracleGraph, 
            DirectedGraph<Object, Edge> oracle, 
            DirectedGraph<Object, Edge> noiseGraph, 
            double epsMod, 
            ArrayList<Double> p,
            ArrayList<Double> r,
            ArrayList<Double> f) throws IOException {
//        bw.write("=========================");
//        bw.newLine();
//        bw.write("DBSCAN");
//        bw.newLine();
//        double eps = Utils.std(noiseGraph.getVertices(), oracleGraph.attribute) * epsMod;
        double eps = epsMod;
        Dbscan instance = new Dbscan(noiseGraph, oracleGraph.attribute, eps, 1);
        String dbscan = instance.applyDbscan(); 
        comparePRF(oracle, dbscan, p, r, f);
    }
    
    private void printResults(BufferedWriter bw) throws IOException {
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse");
        bw.newLine();
        printPrf(p_similarity, r_similarity, f_similarity, bw);
        bw.write("=========================");
        bw.newLine();
        bw.write("DBSCAN");
        bw.newLine();
        printPrf(p_dbscan, r_dbscan, f_dbscan, bw);
        
        checkWinner();
        clearLists(p_similarity, r_similarity, f_similarity);
        clearLists(p_dbscan, r_dbscan, f_dbscan);
    }
    
    private void printPrf(ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f, BufferedWriter bw) throws IOException {
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
//        if (Utils.mean(Utils.listToDoubleArray(first)) > Utils.mean(Utils.listToDoubleArray(second))) {
//            if (Utils.mean(Utils.listToDoubleArray(first)) > Utils.mean(Utils.listToDoubleArray(third))) {
//                    win = 1;
//            }
//        }
        for (int i = 0; i < first.size(); i++) {
            if(first.get(i) >= second.get(i)) {
                if(first.get(i) >= third.get(i)) {
                    win += 1;
                }
            }
        }
        return win;
    }
    
}
