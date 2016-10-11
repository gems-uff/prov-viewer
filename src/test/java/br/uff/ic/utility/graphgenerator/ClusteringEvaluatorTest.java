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

import br.uff.ic.utility.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class ClusteringEvaluatorTest {
    
     String attribute = "A";
    OracleGraph oracle = new OracleGraph(attribute, -200, 200);
    ClusteringEvaluator eval = new ClusteringEvaluator(false, oracle);
    int NUMBER_OF_ORACLE_GRAPHS = 10;
    int NUMBER_OF_NOISE_GRAPHS = 3;
    float INITIAL_NOISE_GRAPH_SIZE = 10;
    float NOISE_INCREASE_NUMBER = 2;
    int NUMBER_ITERATIONS = 10;
//    int smallCluster = 7;
//    int threshold = 4;
//    int smallClusterMod = 4;
    float dagEps = 288; // Dag previous 177.63
    float linearEps = 380; // Linear 248
    float treeEps = 364; // Tree 202.78
//    float monotonicLinearEps = 312F;
//    float monotonicDagEps = 268;
//    float monotonicTreeEps = 328F;
    float monotonicLinearEps = 40.64F; // Monotonic 312
    float monotonicDagEps = 89.88F; // Monotonic 268
    float monotonicTreeEps = 23.45F; // Monotonic 328
    int TF_size;
    int TF_increase;
    int TF_qnt;
    int TT_size;
    int TT_increase;
    int TT_qnt;
    int FT_size;
    int FT_increase;
    int FT_qnt;
    
    public ClusteringEvaluatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of collapse method, of class ClusteringEvaluator.
     */
    @Test
    public void testCollapse() throws Exception {
//        train();
        runExperiment();
    }
    
    private void runExperiment() throws InterruptedException {
        normalGraph();
//        monotonicGraph();
    }

    private void train() throws IOException, InterruptedException {
//        trainDBSCAN();
        trainSimilarity();
    }

    private void trainDBSCAN() throws IOException {
        trainDBSCANMonotonic();
        trainDBSCANRandom();
    }

    private void trainDBSCANMonotonic() throws IOException {
        Trainer trainer = new Trainer();
        trainer.setMonotonic(true);
        monotonicLinearEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "Linear");
        monotonicDagEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "DAG");
        monotonicTreeEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "TREE");
        System.out.println("Best monotonicLinear: " + monotonicLinearEps);
        System.out.println("Best monotonicDAG: " + monotonicDagEps);
        System.out.println("Best monotonicTREE: " + monotonicTreeEps);
    }

    private void trainDBSCANRandom() throws IOException {
        Trainer trainer = new Trainer();
        trainer.setMonotonic(false);
        linearEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "Linear");
        dagEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "DAG");
        treeEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "TREE");

        System.out.println("Best Linear: " + linearEps);
        System.out.println("Best DAG: " + dagEps);
        System.out.println("Best TREE: " + treeEps);
    }

    private void trainSimilarity() throws IOException, InterruptedException {
//        trainSimilarityMonotonic();
        trainSimilarityRandom();
    }

    private void trainSimilarityMonotonic() throws IOException, InterruptedException {
        Trainer trainer = new Trainer();
        System.out.println("======Similarity Training MONOTONIC===========");
        trainer.setMonotonic(true);
        System.out.println("True FALSE Monotonic");
        linearEps = trainer.trainSimilarity(true, false, oracle, 4, 3, 10, 2, "Linear");
        dagEps = trainer.trainSimilarity(true, false, oracle, 4, 3, 10, 2, "DAG");
        treeEps = trainer.trainSimilarity(true, false, oracle, 4, 3, 10, 2, "TREE");

        trainer = new Trainer();
        trainer.setMonotonic(true);
        System.out.println("True True Monotonic");
        trainer.trainSimilarity(true, true, oracle, 4, 3, 10, 4, "Linear");
        trainer.trainSimilarity(true, true, oracle, 4, 3, 10, 4, "DAG");
        trainer.trainSimilarity(true, true, oracle, 4, 3, 10, 4, "TREE");

        trainer = new Trainer();
        trainer.setMonotonic(true);
        System.out.println("Fase True Monotonic");
        trainer.trainSimilarity(true, false, oracle, 4, 3, 10, 4, "Linear");
        trainer.trainSimilarity(true, false, oracle, 4, 3, 10, 4, "DAG");
        trainer.trainSimilarity(true, false, oracle, 4, 3, 10, 4, "TREE");
    }

    private void trainSimilarityRandom() throws IOException, InterruptedException {
        Trainer trainer = new Trainer();
        System.out.println("======Similarity Training RANDOM===========");
        trainer = new Trainer();
        trainer.setMonotonic(false);
        System.out.println("True FALSE Normal");
        linearEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 2, "Linear");
//        dagEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 2, "DAG");
//        treeEps = trainer.trainSimilarity(true, false, oracle, 4, 3, 10, 2, "TREE");
        System.out.println("True True Monotonic");
        trainer = new Trainer();
        trainer.setMonotonic(false);
        System.out.println("True True Normal");
        trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 2, "Linear");
//        trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 2, "DAG");
//        trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 2, "TREE");
        System.out.println("Fase True Monotonic");
        trainer = new Trainer();
        trainer.setMonotonic(false);
        System.out.println("False True Normal");
        trainer.trainSimilarity(false, true, oracle, 3, 3, 10, 2, "Linear");
//        trainer.trainSimilarity(false, true, oracle, 3, 3, 10, 2, "DAG");
//        trainer.trainSimilarity(false, true, oracle, 3, 3, 10, 2, "TREE");
    }

    private void normalGraph() throws InterruptedException {
         try {
             System.out.println("Normal Graphs");
             eval = new ClusteringEvaluator(false, oracle);
//             TF_size = 5;
//             TF_increase = 4;
//             TF_qnt = 3;
//             TT_size = 4;
//             TT_increase = 4;
//             TT_qnt = 3;
//             FT_size = 5;
//             FT_increase = 4;
//             FT_qnt = 3;
             TF_size = 4;
             TF_increase = 5;
             TF_qnt = 6;
             TT_size = 6;
             TT_increase = 6;
             TT_qnt = 6;
             FT_size = 3;
             FT_increase = 5;
             FT_qnt = 6;
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Linear", "Linear", linearEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
             
//             eval = new ClusteringEvaluator(false, oracle);
//             TF_size = 7;
//             TF_increase = 7;
//             TF_qnt = 7;
//             TT_size = 3;
//             TT_increase = 7;
//             TT_qnt = 7;
//             FT_size = 1;
//             FT_increase = 1;
//             FT_qnt = 3;
//             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "DAG", "DAG", dagEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
//             
//             eval = new ClusteringEvaluator(false, oracle);
//             TF_size = 6;
//             TF_increase = 6;
//             TF_qnt = 7;
//             TT_size = 3;
//             TT_increase = 7;
//             TT_qnt = 7;
//             FT_size = 1;
//             FT_increase = 1;
//             FT_qnt = 3;
//             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "TREE", "TREE", treeEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
         } catch (IOException ex) {
             Logger.getLogger(ClusteringEvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    private void monotonicGraph() throws InterruptedException {
         try {
             System.out.println("Partial-Monotonic Graphs");
             // Monotonic
             eval = new ClusteringEvaluator(true, oracle);
             TF_size = 6;
             TF_increase = 7;
             TF_qnt = 7;
             TT_size = 4;
             TT_increase = 6;
             TT_qnt = 7;
             FT_size = 1; //To TRAIN
             FT_increase = 1;  //To TRAIN
             FT_qnt = 3; //To TRAIN
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_Linear", "Linear", monotonicLinearEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
             
             eval = new ClusteringEvaluator(true, oracle);
             TF_size = 7;
             TF_increase = 7;
             TF_qnt = 7;
             TT_size = 3;
             TT_increase = 7;
             TT_qnt = 7;
             FT_size = 1;  //To TRAIN
             FT_increase = 1;  //To TRAIN
             FT_qnt = 3; //To TRAIN
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_Dag", "DAG", monotonicDagEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
             
             eval = new ClusteringEvaluator(true, oracle);
             TF_size = 6;
             TF_increase = 6;
             TF_qnt = 7;
             TT_size = 3;
             TT_increase = 7;
             TT_qnt = 7;
             FT_size = 1; //To TRAIN
             FT_increase = 1;  //To TRAIN
             FT_qnt = 3; //To TRAIN
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_TREE", "TREE", monotonicTreeEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
         } catch (IOException ex) {
             Logger.getLogger(ClusteringEvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    
    
}
