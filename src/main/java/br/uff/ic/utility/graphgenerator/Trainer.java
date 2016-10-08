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

/**
 *
 * @author Kohwalter
 */
public class Trainer {
    OracleGraph oracleGraph = new OracleGraph("A", -200, 200);
    ClusteringEvaluator eval;
    private boolean isMonotonic = false;
    double noiseProbability = 1.0F;
    
    public void setMonotonic (boolean b) {
        isMonotonic = b;
        eval = new ClusteringEvaluator(isMonotonic, oracleGraph);
    }
    
    public double trainSimilarity(boolean updateError, boolean withinCluster, OracleGraph oracleGraph, 
            int NUMBER_OF_ORACLE_GRAPHS, 
            int NUMBER_OF_NOISE_GRAPHS,
            double INITIAL_NOISE_GRAPH_SIZE, 
            double NOISE_INCREASE_NUMBER,
            String typeGraph) throws IOException {
        
        int current_size = 3;
        int bestSize = 1; // 17
        int current_inc = 2;
        int bestInc = 1;
        int currentqnt = 2;
        int bestqnt = 3;
        double noiseFactor;
        ArrayList<Double> p = new ArrayList<>();
        ArrayList<Double> r = new ArrayList<>();
        ArrayList<Double> f = new ArrayList<>();
        ArrayList<Double> p2 = new ArrayList<>();
        ArrayList<Double> r2 = new ArrayList<>();
        ArrayList<Double> f2 = new ArrayList<>();
        ArrayList<Double> c = new ArrayList<>();
        ArrayList<Double> c2 = new ArrayList<>();
        int current = 0;
        int best = 0;
//        System.out.println("Training Size");
        for (int itqnt = 0; itqnt < 4; itqnt++) {
//            System.out.println("Training qnt: " + itqnt);
            for (int itinc = 0; itinc < 3; itinc++) {
//                System.out.println("Training inc: " + itinc);
                for (int itsize = 0; itsize < 3; itsize++) {
//                    System.out.println("Training size: " + itsize);
                    noiseFactor = INITIAL_NOISE_GRAPH_SIZE;
                    for(int oraclegraph = 0; oraclegraph < NUMBER_OF_ORACLE_GRAPHS; oraclegraph++) {
                        DirectedGraph<Object, Edge> oracle = eval.oracleGraph.createOracleGraph(typeGraph);
//                        System.out.println("oraclegraph: " + oraclegraph);
                        for(int noise = 0; noise < NUMBER_OF_NOISE_GRAPHS; noise++) {
                            NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute, isMonotonic);
                            DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + noise + oraclegraph);
                            StringBuffer clusters1 = new StringBuffer();
                            StringBuffer clusters2 = new StringBuffer();
                            ArrayList<Double> t1 = new ArrayList<>();
                            ArrayList<Double> t2 = new ArrayList<>();
                            SimilarityThread alg1 = new SimilarityThread(clusters1, oracleGraph, noiseGraph, updateError, withinCluster, t1, bestSize, bestInc, bestqnt);
                            SimilarityThread alg2 = new SimilarityThread(clusters2, oracleGraph, noiseGraph, updateError, withinCluster, t2, current_size, current_inc, currentqnt);
                            alg1.run();
                            alg2.run();
//                            eval.SimilarityCollapse(noiseGraph, updateError, withinCluster, clusters1, bestSize, bestInc, bestqnt);
//                            eval.SimilarityCollapse(noiseGraph, updateError, withinCluster, clusters2, current_size, current_inc, currentqnt);
                            eval.comparePRF(oracle, clusters1.toString(), p, r, f, c);
                            eval.comparePRF(oracle, clusters2.toString(), p2, r2, f2, c2);
                        }
                        noiseFactor *= NOISE_INCREASE_NUMBER;

                        current += eval.isWinner(f2, f);
                        best += eval.isWinner(f, f2);
                        eval.clearLists(p, r, f, c);
                        eval.clearLists(p2, r2, f2, c2);
                    }
                    if(current > best) {
                        bestSize = current_size;
                        bestInc = current_inc;
                        bestqnt = currentqnt;
                    }
                    current = 0;
                    best = 0;
                    current_size += 2;
                }
                current_size = 3;
                current_inc += 1;
            }
            current_inc = 2;
            currentqnt++;
        }
        
        
        
//        for (int w = 0; w < 50; w++) {
//            System.out.println("Training run: " + w);
//            noiseFactor = INITIAL_NOISE_GRAPH_SIZE;
//            current_size = (int) (Math.random() * 100);
//            current_inc = (int) (Math.random() * 100);
//            for(int i = 0; i < NUMBER_OF_ORACLE_GRAPHS * 0.5; i++) {
//                // Make oracle graph
//                DirectedGraph<Object, Edge> oracle = eval.createOracleGraph(oracleGraph, typeGraph);
//                
//                for (int j = 0; j < NUMBER_OF_ORACLE_GRAPHS * 0.25; j++) {
//                    // Make noise graphs
//                    NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute, isMonotonic);
//                    DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + j + i);
//                    eval.SimilarityCollapse(oracleGraph, oracle, noiseGraph, true, false, p, r, f, c, bestSize, bestInc, bestqnt);
//                    eval.SimilarityCollapse(oracleGraph, oracle, noiseGraph, true, false, p2, r2, f2, c2, current_size, current_inc, currentqnt);
//                }
//                noiseFactor += NOISE_INCREASE_NUMBER * 10;
//                
//                current += eval.isWinner(f2, f, f);
//                best += eval.isWinner(f, f2, f2);
//                eval.clearLists(p, r, f, c);
//                eval.clearLists(p2, r2, f2, c2);
//            }
//            if(current > best) {
////                bestSize = current_size;
//                bestInc = current_inc;
//            }
//            current = 0;
//            best = 0;
//        }
        System.out.println("Best size mod: " + bestSize);
        System.out.println("Best inc mod: " + bestInc);
        System.out.println("Best qnt mod: " + bestqnt);
        return bestSize;
    }
    
    public double trainDBSCAN(OracleGraph oracleGraph, 
            int NUMBER_OF_ORACLE_GRAPHS, 
            double INITIAL_NOISE_GRAPH_SIZE, 
            double NOISE_INCREASE_NUMBER,
            String typeGraph) throws IOException {
        int initialValue = 0;
        double current_eps = initialValue;
        double best_eps = 199;
        double noiseFactor;
        ArrayList<Double> p = new ArrayList<>();
        ArrayList<Double> r = new ArrayList<>();
        ArrayList<Double> f = new ArrayList<>();
        ArrayList<Double> p2 = new ArrayList<>();
        ArrayList<Double> r2 = new ArrayList<>();
        ArrayList<Double> f2 = new ArrayList<>();
        ArrayList<Double> c = new ArrayList<>();
        ArrayList<Double> c2 = new ArrayList<>();
        int current = 0;
        int best = 0;
        System.out.println("Training");
        for (int w = initialValue; w < 100; w++) {
            System.out.println("Training run: " + w);
            noiseFactor = INITIAL_NOISE_GRAPH_SIZE * 4;
            eval.clearLists(p, r, f, c);
            eval.clearLists(p2, r2, f2, c2);
            current = 0;
            best = 0;
            current_eps += 4;
            for(int i = 0; i < NUMBER_OF_ORACLE_GRAPHS * 0.2; i++) {
                // Iterations of the same graph size
                for(int iterations = 0; iterations < 3; iterations++) {
                    // Make oracle graph
                    DirectedGraph<Object, Edge> oracle = eval.oracleGraph.createOracleGraph(typeGraph);

                    for (int noise = 0; noise < NUMBER_OF_ORACLE_GRAPHS * 0.2; noise++) {
                        // Make noise graphs
                        NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute, isMonotonic);
                        DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + noise + i);
                        StringBuffer clusters1 = new StringBuffer();
                        StringBuffer clusters2 = new StringBuffer();
                        ArrayList<Double> t1 = new ArrayList<>(); 
                        ArrayList<Double> t2 = new ArrayList<>(); 
                        DbscanThread dbscan1 = new DbscanThread(clusters1, oracleGraph, noiseGraph, best_eps, t1);
                        DbscanThread dbscan2 = new DbscanThread(clusters1, oracleGraph, noiseGraph, current_eps, t2);
                        dbscan1.run();
                        dbscan2.run();
//                        eval.dbscan(noiseGraph, best_eps, clusters1);
//                        eval.dbscan(noiseGraph, current_eps, clusters2);
                        eval.comparePRF(oracle, clusters1.toString(), p, r, f, c);
                        eval.comparePRF(oracle, clusters2.toString(), p2, r2, f2, c2);
                    }
                }
                noiseFactor *= NOISE_INCREASE_NUMBER * 2;
            }
            current += eval.isWinner(f2, f);
            best += eval.isWinner(f, f2);
            
            if(current > (best * 1.25)) {
                if(Utils.mean(Utils.listToDoubleArray(f)) > Utils.mean(Utils.listToDoubleArray(f2))) {
                    best_eps = current_eps;
                    System.out.println("Best F-measure: " + Utils.mean(Utils.listToDoubleArray(f2)));
                    System.out.println("Old F-measure: " + Utils.mean(Utils.listToDoubleArray(f)));
                }
            }
            
        }
        System.out.println("Best eps mod: " + best_eps);
        return best_eps;
    }
}
