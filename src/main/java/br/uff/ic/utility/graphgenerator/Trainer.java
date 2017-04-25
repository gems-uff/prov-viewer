/*
 * Copyright (C) 2016 Kohwalter
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package br.uff.ic.utility.graphgenerator;

import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Kohwalter
 */
public class Trainer {

    OracleGraph oracleGraph = new OracleGraph("A", -200, 200);
    ClusteringEvaluator eval;
    private boolean isMonotonic = false;
    float noiseProbability = 1.0F;
    int n_iterations = 5;
    int n_oracleGraph = 20;
    int n_noiseGraph = 3;
    int initial_noise_size = 10;
    int noise_increase = 2;

    public void setMonotonic(boolean b) {
        isMonotonic = b;
        eval = new ClusteringEvaluator(isMonotonic, oracleGraph);
    }

    public float trainSimilarity(boolean updateError, boolean withinCluster, OracleGraph oracleGraph,
            //            int ITERATIONS,
            //            int NUMBER_OF_ORACLE_GRAPHS, 
            //            int NUMBER_OF_NOISE_GRAPHS,
            //            float INITIAL_NOISE_GRAPH_SIZE, 
            //            float NOISE_INCREASE_NUMBER,
            String typeGraph, float dbscanEps) throws IOException, InterruptedException {

        int current_size = 5;
        int bestSize = 5; // 17
        int inc_initial_value = 2;
        int current_inc = inc_initial_value;
        int bestInc = 1;
        float currentqnt = 0.3f;
        float bestqnt = 0.1f;
        float noiseFactor;
        float qntIncrement = 0.2f;
        int incIncrement = 1;
        ArrayList<Float> p = new ArrayList<>();
        ArrayList<Float> r = new ArrayList<>();
        ArrayList<Float> f_best = new ArrayList<>();
        ArrayList<Float> c = new ArrayList<>();
        ArrayList<Float> p2 = new ArrayList<>();
        ArrayList<Float> r2 = new ArrayList<>();
        ArrayList<Float> f2 = new ArrayList<>();
        ArrayList<Float> c2 = new ArrayList<>();
        ArrayList<Float> p3 = new ArrayList<>();
        ArrayList<Float> r3 = new ArrayList<>();
        ArrayList<Float> f3 = new ArrayList<>();
        ArrayList<Float> c3 = new ArrayList<>();
        ArrayList<Float> p4 = new ArrayList<>();
        ArrayList<Float> r4 = new ArrayList<>();
        ArrayList<Float> f4 = new ArrayList<>();
        ArrayList<Float> c4 = new ArrayList<>();
        ArrayList<Float> p5 = new ArrayList<>();
        ArrayList<Float> r5 = new ArrayList<>();
        ArrayList<Float> f_dbscan = new ArrayList<>();
        ArrayList<Float> c5 = new ArrayList<>();
        int current = 0;
        int best = 0;
        double best_fmeasure = 0;
        double dbscan_fmeasure = 0;
//        System.out.println("Training Size");
        // Train for the std multiplier
        for (int itqnt = 0; itqnt < 11; itqnt++) {
            System.out.println("itqnt: " + itqnt);
            for (int itinc = 0; itinc < 2; itinc++) {
                if (updateError == false) {
                    itinc = 10;
                }
//                System.out.println("Training inc: " + itinc);
//                for (int itsize = 0; itsize < 4; itsize++) {
//                    System.out.println("Training size: " + itsize);
                noiseFactor = initial_noise_size;
                // ITERATIONS
                for (int iteration = 0; iteration < n_iterations; iteration++) {
                    //NUMBER_OF_ORACLE_GRAPHS
                    for (int oraclegraph = 0; oraclegraph < n_oracleGraph; oraclegraph++) {
                        DirectedGraph<Object, Edge> oracle = eval.oracleGraph.createOracleGraph(typeGraph);
                        // NUMBER_OF_NOISE_GRAPHS
                        for (int noise = 0; noise < n_noiseGraph; noise++) {
                            NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute, isMonotonic);
                            DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + noise + iteration);
                            ArrayList<ConcurrentHashMap<String, Object>> clusters_best = new ArrayList<>();
                            ArrayList<ConcurrentHashMap<String, Object>> clusters_current1 = new ArrayList<>();
                            ArrayList<ConcurrentHashMap<String, Object>> clusters_current2 = new ArrayList<>();
                            ArrayList<ConcurrentHashMap<String, Object>> clusters_current3 = new ArrayList<>();
                            ArrayList<ConcurrentHashMap<String, Object>> clusters_dbscan = new ArrayList<>();
                            ArrayList<Float> t1 = new ArrayList<>();
                            ArrayList<Float> t2 = new ArrayList<>();
                            ArrayList<Float> t3 = new ArrayList<>();
                            ArrayList<Float> t4 = new ArrayList<>();
                            ArrayList<Float> t5 = new ArrayList<>();
                            
                            Thread worker1 = new Thread(new SimilarityThread(clusters_best, oracleGraph, noiseGraph, updateError, withinCluster, t1, bestSize, bestInc, bestqnt));
                            Thread worker2 = new Thread(new SimilarityThread(clusters_current1, oracleGraph, noiseGraph, updateError, withinCluster, t2, current_size, current_inc, currentqnt));
                            Thread worker3 = new Thread(new SimilarityThread(clusters_current2, oracleGraph, noiseGraph, updateError, withinCluster, t3, current_size, current_inc + incIncrement, currentqnt + qntIncrement));
                            Thread worker4 = new Thread(new SimilarityThread(clusters_current3, oracleGraph, noiseGraph, updateError, withinCluster, t4, current_size, current_inc + (2* incIncrement), currentqnt + (2* qntIncrement)));
                            
                            Thread worker5 = new Thread(new DbscanThread(clusters_dbscan, oracleGraph, noiseGraph, dbscanEps, t5));
                            
                            worker1.start();
                            worker2.start();
                            worker3.start();
                            worker4.start();
                            worker5.start();
                            worker1.join();
                            worker2.join();
                            worker3.join();
                            worker4.join();
                            worker5.join();

                            eval.comparePRF(oracle, clusters_best, p, r, f_best, c);
                            eval.comparePRF(oracle, clusters_current1, p2, r2, f2, c2);
                            eval.comparePRF(oracle, clusters_current2, p3, r3, f3, c3);
                            eval.comparePRF(oracle, clusters_current3, p4, r4, f4, c4);
                            eval.comparePRF(oracle, clusters_dbscan, p5, r5, f_dbscan, c5);
                        }
                    }
                    noiseFactor *= noise_increase;

                }                
                if (Utils.mean(Utils.listToFloatArray(f2)) > Utils.mean(Utils.listToFloatArray(f_best))) {
                    bestInc = current_inc;
                    bestqnt = currentqnt;
                    f_best.clear();
                    f_best.addAll(f2);
                    best_fmeasure = Utils.mean(Utils.listToFloatArray(f2));
                    dbscan_fmeasure = Utils.mean(Utils.listToFloatArray(f_dbscan));
//                    if (Utils.mean(Utils.listToFloatArray(f2)) > Utils.mean(Utils.listToFloatArray(f_dbscan))) {
//                        System.out.println("DBSCAN: " + Utils.mean(Utils.listToFloatArray(f_dbscan)));
//                        System.out.println("Better than DBSCAN: " + best_fmeasure + " / QNT: " + bestqnt);
//                    }
                }
                if (Utils.mean(Utils.listToFloatArray(f3)) > Utils.mean(Utils.listToFloatArray(f_best))) {
                    bestInc = current_inc + incIncrement;
                    bestqnt = currentqnt + qntIncrement;
                    f_best.clear();
                    f_best.addAll(f3);
                    best_fmeasure = Utils.mean(Utils.listToFloatArray(f3));
                    dbscan_fmeasure = Utils.mean(Utils.listToFloatArray(f_dbscan));
//                    if (Utils.mean(Utils.listToFloatArray(f3)) > Utils.mean(Utils.listToFloatArray(f_dbscan))) {
//                        System.out.println("DBSCAN: " + Utils.mean(Utils.listToFloatArray(f_dbscan)));
//                        System.out.println("Better than DBSCAN: " + best_fmeasure + " / QNT: " + bestqnt);
//                    }
                }
                if (Utils.mean(Utils.listToFloatArray(f4)) > Utils.mean(Utils.listToFloatArray(f_best))) {
                    bestInc = current_inc + (2* incIncrement);
                    bestqnt = currentqnt + (2* qntIncrement);
                    f_best.clear();
                    f_best.addAll(f4);
                    best_fmeasure = Utils.mean(Utils.listToFloatArray(f4));
                    dbscan_fmeasure = Utils.mean(Utils.listToFloatArray(f_dbscan));
//                    if (Utils.mean(Utils.listToFloatArray(f4)) > Utils.mean(Utils.listToFloatArray(f_dbscan))) {
//                        System.out.println("DBSCAN: " + Utils.mean(Utils.listToFloatArray(f_dbscan)));
//                        System.out.println("Better than DBSCAN: " + best_fmeasure + " / QNT: " + bestqnt);
//                    }
                }
//                }
                current = 0;
                best = 0;
                eval.clearLists(p, r, f_best, c);
                eval.clearLists(p2, r2, f2, c2);
                eval.clearLists(p3, r3, f3, c3);
                eval.clearLists(p4, r4, f4, c4);
                eval.clearLists(p5, r5, f_dbscan, c5);
//                    current_size += 2;
//                }
//                current_size = 3;
                current_inc += 3 * incIncrement;
            }
            current_inc = inc_initial_value;
            currentqnt += 3 * qntIncrement;
        }
//        System.out.println("Best size mod: " + bestSize);
        if (updateError == true) {
            System.out.println("Best inc mod: " + bestInc);
        }
        System.out.println("Best qnt mod: " + bestqnt);
        System.out.println("Mean F-Measure of the best: " + best_fmeasure);
        System.out.println("Mean F-Measure of DBSCAN: " + dbscan_fmeasure);
        return bestSize;
    }

    public float trainDBSCAN(OracleGraph oracleGraph,
            //            int ITERATIONS,
            //            int NUMBER_OF_ORACLE_GRAPHS,
            //            int NUMBER_OF_NOISE_GRAPHS,
            //            float INITIAL_NOISE_GRAPH_SIZE, 
            //            float NOISE_INCREASE_NUMBER,
            String typeGraph) throws IOException {
        float initialValue = 0.1F;
        float current_eps = initialValue;
        float best_eps = 1;
        float noiseFactor;
        ArrayList<Float> p = new ArrayList<>();
        ArrayList<Float> r = new ArrayList<>();
        ArrayList<Float> f_best = new ArrayList<>();
        ArrayList<Float> p2 = new ArrayList<>();
        ArrayList<Float> r2 = new ArrayList<>();
        ArrayList<Float> f_current = new ArrayList<>();
        ArrayList<Float> c = new ArrayList<>();
        ArrayList<Float> c2 = new ArrayList<>();
        int current = 0;
        int best = 0;
        System.out.println("Training");
        for (int w = 0; w < 15; w++) {
//            System.out.println("Training run: " + w);
            noiseFactor = initial_noise_size;
            eval.clearLists(p, r, f_best, c);
            eval.clearLists(p2, r2, f_current, c2);
            current = 0;
            best = 0;
            current_eps += 0.1; // Changed from 4
            // ITERATIONS
            for (int i = 0; i < n_iterations; i++) {
                // Iterations of the same graph size NUMBER_OF_ORACLE_GRAPHS
                for (int iterations = 0; iterations < n_oracleGraph; iterations++) {
                    // Make oracle graph
                    DirectedGraph<Object, Edge> oracle = eval.oracleGraph.createOracleGraph(typeGraph);
                    // NUMBER_OF_NOISE_GRAPHS
                    for (int noise = 0; noise < n_noiseGraph; noise++) {
                        // Make noise graphs
                        NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute, isMonotonic);
                        DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + noise + i);
                        ArrayList<ConcurrentHashMap<String, Object>> clusters_best = new ArrayList<>();
                        ArrayList<ConcurrentHashMap<String, Object>> clusters_current = new ArrayList<>();
                        ArrayList<Float> t1 = new ArrayList<>();
                        ArrayList<Float> t2 = new ArrayList<>();
                        DbscanThread dbscan1 = new DbscanThread(clusters_best, oracleGraph, noiseGraph, best_eps, t1);
                        DbscanThread dbscan2 = new DbscanThread(clusters_current, oracleGraph, noiseGraph, current_eps, t2);
                        dbscan1.run();
                        dbscan2.run();

                        eval.comparePRF(oracle, clusters_best, p, r, f_best, c);
                        eval.comparePRF(oracle, clusters_current, p2, r2, f_current, c2);
                    }
                }
                noiseFactor *= noise_increase;
            }
            current += eval.isWinner(f_current, f_best);
            best += eval.isWinner(f_best, f_current);

            if (current > (best)) {
                if (Utils.mean(Utils.listToFloatArray(f_current)) > Utils.mean(Utils.listToFloatArray(f_best))) {
                    best_eps = current_eps;
//                    System.out.println("Best F-measure: " + Utils.mean(Utils.listToFloatArray(f2)));
//                    System.out.println("Old F-measure: " + Utils.mean(Utils.listToFloatArray(f)));
                }
            }

        }
//        System.out.println("Best eps mod: " + best_eps);
        return best_eps;
    }
}
