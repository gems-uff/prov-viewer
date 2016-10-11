/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kohwalter
 */
public class ClusteringEvaluator {

    float noiseProbability = 1.0F;
//    ArrayList<ArrayList<Double>> precision = new ArrayList<ArrayList<Double>>();
//    ArrayList<ArrayList<Double>> recall = new ArrayList<ArrayList<Double>>();
//    ArrayList<ArrayList<Double>> fmeasure = new ArrayList<ArrayList<Double>>();
//    ArrayList<ArrayList<Double>> clusters = new ArrayList<ArrayList<Double>>();

    ArrayList<Float> p_similarity = new ArrayList<>();
    ArrayList<Float> r_similarity = new ArrayList<>();
    ArrayList<Float> f_similarity = new ArrayList<>();
    ArrayList<Float> c_similarity = new ArrayList<>();
    ArrayList<Float> t_similarity = new ArrayList<>();

    ArrayList<Float> p_similarityFT = new ArrayList<>();
    ArrayList<Float> r_similarityFT = new ArrayList<>();
    ArrayList<Float> f_similarityFT = new ArrayList<>();
    ArrayList<Float> c_similarityFT = new ArrayList<>();
    ArrayList<Float> t_similarityFT = new ArrayList<>();

    ArrayList<Float> p_similarityTT = new ArrayList<>();
    ArrayList<Float> r_similarityTT = new ArrayList<>();
    ArrayList<Float> f_similarityTT = new ArrayList<>();
    ArrayList<Float> c_similarityTT = new ArrayList<>();
    ArrayList<Float> t_similarityTT = new ArrayList<>();

    ArrayList<Float> p_dbscan = new ArrayList<>();
    ArrayList<Float> r_dbscan = new ArrayList<>();
    ArrayList<Float> f_dbscan = new ArrayList<>();
    ArrayList<Float> c_dbscan = new ArrayList<>();  // number of clusters
    ArrayList<Float> t_dbscan = new ArrayList<>();

    private boolean isMonotonic = false;
    OracleGraph oracleGraph;

    int similarity = 0;
    int similarityFT = 0;
    int similarityTT = 0;
    int dbscan = 0;

    BufferedWriter bw;
    BufferedWriter bwR;

    ClusteringEvaluator(boolean b, OracleGraph oracle) {
        isMonotonic = b;
        oracleGraph = oracle;
    }

    public void setMonotonic(boolean t) {
        isMonotonic = t;
    }

    /**
     * Method to compare the clusters found with the ones from the Oracle and
     * adds the results (precision, recall, f-measure) in their respective lists
     *
     * @param oracle is the oracle that contains the "correct" answers
     * @param collapseGroups
     * @param list is the clusters found by an algorithm
     * @param p is a variable that will store the precision for this trial
     * @param r is a variable that will store the recall for this trial
     * @param f is a variable that will store the f-measure for this trial
     * @param c is a variable that will store the number of clusters found for
     * this trial
     * @throws IOException
     */
    public void comparePRF(DirectedGraph<Object, Edge> oracle, ArrayList<ConcurrentHashMap<String, Object>> collapseGroups, ArrayList<Float> p, ArrayList<Float> r, ArrayList<Float> f, ArrayList<Float> c) throws IOException {
//        List<String> clusters = new ArrayList<>();
        float relevantDocuments = oracle.getVertexCount();
        float retrievedDocuments;
        float intersection = 0;
        float precision = 0;
        float recall = 0;
        float fmeasure = 0;
//        String[] elements = list.split(" ");
//        clusters.addAll(Arrays.asList(elements));
//        retrievedDocuments = clusters.size();
        retrievedDocuments = collapseGroups.size();
        if(retrievedDocuments != 0) {
            for (ConcurrentHashMap<String, Object> subGraph : collapseGroups) {
                boolean computedCluster = false;
                if (subGraph.size() > 0) {
                    for (Object v : oracle.getVertices()) {
                        String id = ((Vertex) v).getID();
                        if (subGraph.containsKey(id) && !computedCluster) {
                            computedCluster = true;
                            intersection++;
                        }
                    }
                }
            }
            precision = intersection / retrievedDocuments;
            recall = intersection / relevantDocuments;
            fmeasure = 2 * (precision * recall) / (precision + recall);
        }
        else {
            System.out.println("intersection: " + intersection);
            System.out.println("retrievedDocuments: " + retrievedDocuments);
            System.out.println("precision: " + precision);
            System.out.println("recall: " + recall);
            System.out.println("fmeasure: " + fmeasure);
        }
        p.add(precision);
        r.add(recall);
        f.add(fmeasure);
        c.add(retrievedDocuments);
    }

    /**
     * Main method used in the experiment
     *
     * @param NUMBER_OF_ORACLE_GRAPHS defines how many oracles we will generate
     * in each iteration
     * @param NUMBER_OF_NOISE_GRAPHS defines the number of generated noise graph
     * for each oracle graph
     * @param INITIAL_NOISE_GRAPH_SIZE defines the initial number of noise
     * vertices in the first iteration
     * @param NOISE_INCREASE_NUMBER defines the growth factor (multiplicative)
     * of noise graphs in each iteration
     * @param NUMBER_ITERATIONS defines the number of iterations
     * @param fileName defines the file name for this trial
     * @param typeGraph defines the type of graph that we will experiment with
     * @param epsMod is a configuration value for dbscan algorithms
     * @param TF_size is a configuration value for one of the similarity
     * algorithms
     * @param TF_increase is a configuration value for one of the similarity
     * algorithms
     * @param TF_qnt is a configuration value for one of the similarity
     * algorithms
     * @param TT_size is a configuration value for one of the similarity
     * algorithms
     * @param TT_increase is a configuration value for one of the similarity
     * algorithms
     * @param TT_qnt is a configuration value for one of the similarity
     * algorithms
     * @param FT_size is a configuration value for one of the similarity
     * algorithms
     * @param FT_increase is a configuration value for one of the similarity
     * algorithms
     * @param FT_qnt is a configuration value for one of the similarity
     * algorithms
     * @throws IOException
     * @throws InterruptedException
     */
    public void collapse(int NUMBER_OF_ORACLE_GRAPHS,
            int NUMBER_OF_NOISE_GRAPHS,
            float INITIAL_NOISE_GRAPH_SIZE,
            float NOISE_INCREASE_NUMBER,
            int NUMBER_ITERATIONS,
            String fileName,
            String typeGraph,
            float epsMod,
            int TF_size,
            int TF_increase,
            int TF_qnt,
            int TT_size,
            int TT_increase,
            int TT_qnt,
            int FT_size,
            int FT_increase,
            int FT_qnt) throws IOException, InterruptedException {

        int i;
        int j;
        int w;
        int total_similarity = 0;
        int total_dbscan = 0;
        int total_tt = 0;
        int total_ft = 0;
        float noiseFactor = INITIAL_NOISE_GRAPH_SIZE;
        for (w = 0; w < NUMBER_ITERATIONS; w++) {
            System.out.println("Iteration NUMBER #" + w);
            createIterationFile(fileName, w, epsMod, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
            bw.write("==============================================================");
            bw.newLine();
            bw.write("ITERATION NUMBER #" + w);
            bw.newLine();
            DirectedGraph<Object, Edge> oracle;
            oracle = oracleGraph.createOracleGraph(typeGraph);
            bw.write("Oracle size: " + oracle.getVertexCount());
            bw.newLine();
            bw.write("NoiseGraph size: " + oracle.getVertexCount() * noiseFactor);
            bw.newLine();

            for (j = 1; j <= NUMBER_OF_ORACLE_GRAPHS; j++) {
                oracle = oracleGraph.createOracleGraph(typeGraph);

                for (i = 0; i < NUMBER_OF_NOISE_GRAPHS; i++) {
                    NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute, isMonotonic);
                    DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + j + i);
                    float time;

                    ArrayList<ConcurrentHashMap<String, Object>> clusters1 = new ArrayList<>();
                    ArrayList<ConcurrentHashMap<String, Object>> clusters2 = new ArrayList<>();
                    ArrayList<ConcurrentHashMap<String, Object>> clusters3 = new ArrayList<>();
                    ArrayList<ConcurrentHashMap<String, Object>> clusters4 = new ArrayList<>();

                    Thread t1 = new Thread(new SimilarityThread(clusters1, oracleGraph, noiseGraph, true, false, t_similarity, TF_size, TF_increase, TF_qnt));
                    Thread t2 = new Thread(new SimilarityThread(clusters2, oracleGraph, noiseGraph, false, true, t_similarityFT, FT_size, FT_increase, FT_qnt));
                    Thread t3 = new Thread(new SimilarityThread(clusters3, oracleGraph, noiseGraph, true, true, t_similarityTT, TT_size, TT_increase, TT_qnt));
                    Thread t4 = new Thread(new DbscanThread(clusters4, oracleGraph, noiseGraph, epsMod, t_dbscan));

                    t1.start();
                    t2.start();
                    t3.start();
                    t4.start();

                    t1.join();
                    t2.join();
                    t3.join();
                    t4.join();

//                    SimilarityThread alg1 = new SimilarityThread(clusters1, oracleGraph, noiseGraph, true, false, t_similarity, TF_size, TF_increase, TF_qnt);
//                    SimilarityThread alg2 = new SimilarityThread(clusters2, oracleGraph, noiseGraph, false, true, t_similarityFT, FT_size, FT_increase, FT_qnt);
//                    SimilarityThread alg3 = new SimilarityThread(clusters3, oracleGraph, noiseGraph, true, true, t_similarityTT, TT_size, TT_increase, TT_qnt);
//                    DbscanThread alg4 = new DbscanThread(clusters4, oracleGraph, noiseGraph, epsMod, t_dbscan);
//                    
//                    alg1.run();
//                    alg2.run();
//                    alg3.run();
//                    alg4.run();
//                    time = SimilarityCollapse(noiseGraph, true, false, clusters1, TF_size, TF_increase, TF_qnt);
//                    t_similarity.add(time);
//                    
//                    // Good when data is monotonic-ish
//                    time = SimilarityCollapse(noiseGraph, false, true, clusters2, FT_size, FT_increase, FT_qnt);
//                    t_similarityFT.add(time);
//                    
//                    // Terrible in all types of data so far
//                    time = SimilarityCollapse(noiseGraph, true, true, clusters3, TT_size, TT_increase, TT_qnt);
//                    t_similarityTT.add(time);
//
////                    time = SimilarityCollapse(noiseGraph, false, false, clusters4, 1, 1, 1);
//                    time = dbscan(noiseGraph, epsMod, clusters4);
//                    t_dbscan.add(time);
                    comparePRF(oracle, clusters1, p_similarity, r_similarity, f_similarity, c_similarity);
                    comparePRF(oracle, clusters2, p_similarityFT, r_similarityFT, f_similarityFT, c_similarityFT);
                    comparePRF(oracle, clusters3, p_similarityTT, r_similarityTT, f_similarityTT, c_similarityTT);
                    comparePRF(oracle, clusters4, p_dbscan, r_dbscan, f_dbscan, c_dbscan);
                }
            }
            printResults(bw, bwR, w);
            bw.newLine();
            bw.write("Similarity wins: " + similarity);
            bw.newLine();
            bw.write("dbscan wins: " + dbscan);
            bw.newLine();
            bw.write("Similarity TF wins: " + similarityFT);
            bw.newLine();
            bw.write("Similarity TT wins: " + similarityTT);
            bw.newLine();
            total_similarity += similarity;
            total_dbscan += dbscan;
            total_ft += similarityFT;
            total_tt += similarityTT;
            similarity = 0;
            dbscan = 0;
            similarityTT = 0;
            similarityFT = 0;
            noiseFactor *= NOISE_INCREASE_NUMBER;
            bw.close();
            bwR.close();
        }
        createFinalResultsFile(fileName, total_similarity, total_dbscan, total_ft, total_tt, epsMod, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
    }

    /**
     * Method to export the results in a txt file during each iteration
     *
     * @param name
     * @param iteration
     * @param epsMod
     * @param TF_size
     * @param TF_increase
     * @param TF_qnt
     * @param TT_size
     * @param TT_increase
     * @param TT_qnt
     * @param FT_size
     * @param FT_increase
     * @param FT_qnt
     * @throws IOException
     */
    public void createIterationFile(String name, int iteration, float epsMod,
            int TF_size, int TF_increase, int TF_qnt, int TT_size, int TT_increase, int TT_qnt, int FT_size, int FT_increase, int FT_qnt) throws IOException {
        File file = new File("Evaluation_" + name + iteration + ".txt");
        File fileR = new File("R_Data_" + name + iteration + ".txt");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        if (!fileR.exists()) {
            fileR.createNewFile();
        }

        bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        bwR = new BufferedWriter(new FileWriter(fileR.getAbsoluteFile()));

        bw.write("DBSCAN EPS: " + epsMod);
        bw.newLine();
        bw.write("(TF) Similarity small cluster definition: " + TF_increase);
        bw.newLine();
        bw.write("(TF) Similarity threshold small cluster (multiplicates the normal threshold by): " + TF_increase);
        bw.newLine();
        bw.write("(TF) Similarity Threshold (in STD): " + TF_qnt);
        bw.newLine();
        bw.write("(TT) Similarity small cluster definition: " + TT_increase);
        bw.newLine();
        bw.write("(TT) Similarity threshold small cluster (multiplicates the normal threshold by): " + TT_increase);
        bw.newLine();
        bw.write("(TT) Similarity Threshold (in STD): " + TT_qnt);
        bw.newLine();
        bw.write("(FT) Similarity small cluster definition: " + FT_increase);
        bw.newLine();
        bw.write("(FT) Similarity threshold small cluster (multiplicates the normal threshold by): " + FT_increase);
        bw.newLine();
        bw.write("(FT) Similarity Threshold (in STD): " + FT_qnt);
        bw.newLine();
    }

    /**
     * Method to export the final results from the trial
     *
     * @param fileName
     * @param total_similarity
     * @param total_dbscan
     * @param total_ft
     * @param total_tt
     * @param epsMod
     * @param TF_size
     * @param TF_increase
     * @param TF_qnt
     * @param TT_size
     * @param TT_increase
     * @param TT_qnt
     * @param FT_size
     * @param FT_increase
     * @param FT_qnt
     * @throws IOException
     */
    public void createFinalResultsFile(String fileName, int total_similarity, int total_dbscan, int total_ft, int total_tt, float epsMod,
            int TF_size, int TF_increase, int TF_qnt, int TT_size, int TT_increase, int TT_qnt, int FT_size, int FT_increase, int FT_qnt) throws IOException {
        BufferedWriter results;
        File file = new File("Evaluation_" + fileName + "_Results.txt");
        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        results = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        System.out.println("===============================");
        System.out.println("Final Result");
        System.out.println("Similarity: " + total_similarity);
        System.out.println("dbscan: " + total_dbscan);

        results.write("DBSCAN EPS: " + epsMod);
        results.newLine();
        results.write("(TF) Similarity small cluster definition: " + TF_increase);
        results.newLine();
        results.write("(TF) Similarity threshold small cluster (multiplicates the normal threshold by): " + TF_increase);
        results.newLine();
        results.write("(TF) Similarity Threshold (in STD): " + TF_qnt);
        results.newLine();
        results.write("(TT) Similarity small cluster definition: " + TT_increase);
        results.newLine();
        results.write("(TT) Similarity threshold small cluster (multiplicates the normal threshold by): " + TT_increase);
        results.newLine();
        results.write("(TT) Similarity Threshold (in STD): " + TT_qnt);
        results.newLine();
        results.write("(FT) Similarity small cluster definition: " + FT_increase);
        results.newLine();
        results.write("(FT) Similarity threshold small cluster (multiplicates the normal threshold by): " + FT_increase);
        results.newLine();
        results.write("(FT) Similarity Threshold (in STD): " + FT_qnt);
        results.newLine();

        results.write("===============================");
        results.newLine();
        results.write("Final Result");
        results.newLine();
        results.write("Similarity (tf): " + total_similarity);
        results.newLine();
        results.write("dbscan (ff): " + total_dbscan);
        results.newLine();
        results.write("Similarity (ft): " + total_ft);
        results.newLine();
        results.write("Similarity (tt): " + total_tt);
        results.newLine();

        results.close();
    }

    /**
     * Method to print the results of each algorithm
     *
     * @param bw
     * @param bwR
     * @param iteration
     * @throws IOException
     */
    private void printResults(BufferedWriter bw, BufferedWriter bwR, int iteration) throws IOException {
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse (TF)");
        bw.newLine();
        printPrf(p_similarity, r_similarity, f_similarity, c_similarity, t_similarity, bw, bwR, "s", iteration);
        bw.write("=========================");
        bw.newLine();
        bw.write("DBSCAN (FF)");
        bw.newLine();
        printPrf(p_dbscan, r_dbscan, f_dbscan, c_dbscan, t_dbscan, bw, bwR, "d", iteration);
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse (FT)");
        bw.newLine();
        printPrf(p_similarityFT, r_similarityFT, f_similarityFT, c_similarityFT, t_similarityFT, bw, bwR, "ft", iteration);
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse (TT)");
        bw.newLine();
        printPrf(p_similarityTT, r_similarityTT, f_similarityTT, c_similarityTT, t_similarityTT, bw, bwR, "tt", iteration);

        checkWinner();

        clearLists(p_similarity, r_similarity, f_similarity, c_similarity, t_similarity);
        clearLists(p_similarityFT, r_similarityFT, f_similarityFT, c_similarityFT, t_similarityFT);
        clearLists(p_similarityTT, r_similarityTT, f_similarityTT, c_similarityTT, t_similarityTT);
        clearLists(p_dbscan, r_dbscan, f_dbscan, c_dbscan, t_dbscan);
    }

    /**
     * Method that summarizes the results of each algorithm
     *
     * @param p
     * @param r
     * @param f
     * @param c
     * @param t
     * @param bw
     * @param bwR
     * @param name
     * @param iteration
     * @throws IOException
     */
    private void printPrf(ArrayList<Float> p,
            ArrayList<Float> r,
            ArrayList<Float> f,
            ArrayList<Float> c,
            ArrayList<Float> t,
            BufferedWriter bw,
            BufferedWriter bwR,
            String name,
            int iteration) throws IOException {

        String precision;
        String recall;
        String fmeasure;
        String clusters;
        String time;
        String efficiency;

        precision = "Precision> Mean: " + Utils.mean(Utils.listToFloatArray(p))
                + " / STD:" + Utils.stdev(Utils.listToFloatArray(p))
                + " / Min: " + Utils.minimumValue(Utils.listToFloatArray(p));

        recall = "Recall> Mean: " + Utils.mean(Utils.listToFloatArray(r))
                + " / STD:" + Utils.stdev(Utils.listToFloatArray(r))
                + " / Min: " + Utils.minimumValue(Utils.listToFloatArray(r));

        fmeasure = "F-Measure> Mean: " + Utils.mean(Utils.listToFloatArray(f))
                + " / STD:" + Utils.stdev(Utils.listToFloatArray(f))
                + " / Min: " + Utils.minimumValue(Utils.listToFloatArray(f));
        clusters = "#Clusters> Mean: " + Utils.mean(Utils.listToFloatArray(c))
                + " / STD:" + Utils.stdev(Utils.listToFloatArray(c))
                + " / Min: " + Utils.minimumValue(Utils.listToFloatArray(c))
                + " / Max: " + Utils.maximumValue(Utils.listToFloatArray(c));
        time = "Time (milliseconds)> Mean: " + Utils.mean(Utils.listToFloatArray(t))
                + " / STD:" + Utils.stdev(Utils.listToFloatArray(t))
                + " / Min: " + Utils.minimumValue(Utils.listToFloatArray(t))
                + " / Max: " + Utils.maximumValue(Utils.listToFloatArray(t));

        efficiency = "Efficiency (precision/time)>: " + Utils.mean(Utils.listToFloatArray(p)) / (Utils.mean(Utils.listToFloatArray(t)) / 60000);

        bw.write(precision);
        bw.newLine();
        bw.write(recall);
        bw.newLine();
        bw.write(fmeasure);
        bw.newLine();
        bw.write(clusters);
        bw.newLine();
        bw.write(time);
        bw.newLine();
        bw.write(efficiency);
        bw.newLine();
        bwR.write(printValues(p, "p" + name + iteration));
        bwR.newLine();
        bwR.write(printValues(r, "r" + name + iteration));
        bwR.newLine();
        bwR.write(printValues(f, "f" + name + iteration));
        bwR.newLine();
        bwR.write(printValues(t, "t" + name + iteration));
        bwR.newLine();
        bwR.newLine();
    }

    /**
     * Method that converts the values for exporting
     *
     * @param v
     * @param type
     * @return
     */
    private String printValues(ArrayList<Float> v, String type) {
        DecimalFormat df = new DecimalFormat("#.###");
        String values = type + " <- c(";
        for (Float e : v) {
            if(e.isNaN()) {
                values += 0 + ",";
                System.out.println("NaN");
            }
            else
                values += Float.valueOf(df.format(e)) + ",";
        }
        values = values.substring(0, values.length() - 1);
        values += ")";
        return values;
    }

    /**
     * Method to clean the arraylists
     *
     * @param p
     * @param r
     * @param f
     * @param c
     * @param t
     */
    public void clearLists(ArrayList<Float> p, ArrayList<Float> r, ArrayList<Float> f, ArrayList<Float> c, ArrayList<Float> t) {
        p.clear();
        r.clear();
        f.clear();
        c.clear();
        t.clear();
    }

    /**
     * Method to clean the array lists
     *
     * @param p
     * @param r
     * @param f
     * @param c
     */
    public void clearLists(ArrayList<Float> p, ArrayList<Float> r, ArrayList<Float> f, ArrayList<Float> c) {
        p.clear();
        r.clear();
        f.clear();
        c.clear();
    }

    /**
     * Method to count the number of times each algorithm "won"
     */
    private void checkWinner() {
        countWinnings(f_similarity, f_dbscan, f_similarityFT, f_similarityTT);
    }

    public int isWinner(ArrayList<Float> first, ArrayList<Float> second) {
        int win = 0;
        for (int i = 0; i < first.size(); i++) {
            if (first.get(i) >= second.get(i)) {
                win += 1;
            }
        }
        return win;
    }

    /**
     * Method to count the number of times each algorithm "won"
     *
     * @param sim
     * @param db
     * @param ft
     * @param tt
     */
    public void countWinnings(ArrayList<Float> sim, ArrayList<Float> db, ArrayList<Float> ft, ArrayList<Float> tt) {
        for (int i = 0; i < sim.size(); i++) {

            if ((sim.get(i) >= db.get(i))
                    && (sim.get(i) >= ft.get(i))
                    && (sim.get(i) >= tt.get(i))) {
                similarity += 1;
            }
            if ((db.get(i) >= sim.get(i))
                    && (db.get(i) >= ft.get(i))
                    && (db.get(i) >= tt.get(i))) {
                dbscan += 1;
            }
            if ((ft.get(i) >= db.get(i))
                    && (ft.get(i) >= sim.get(i))
                    && (ft.get(i) >= tt.get(i))) {
                similarityFT += 1;
            }
            if ((tt.get(i) >= db.get(i))
                    && (tt.get(i) >= ft.get(i))
                    && (tt.get(i) >= sim.get(i))) {
                similarityTT += 1;
            }
        }
    }

}
