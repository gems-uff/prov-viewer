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

package br.uff.ic.utility.graphgenerator;

import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kohwalter
 */
public class ClusteringEvaluatorTest {
    
     String attribute = "A";
    OracleGraph oracle = new OracleGraph(attribute, -200, 200);
    ClusteringEvaluator eval = new ClusteringEvaluator(false, oracle);
    int NUMBER_OF_ORACLE_GRAPHS = 40;
    int NUMBER_OF_NOISE_GRAPHS = 5;
    float INITIAL_NOISE_GRAPH_SIZE = 10;
    float NOISE_INCREASE_NUMBER = 2;
    int NUMBER_ITERATIONS = 7;
//    int smallCluster = 7;
//    int threshold = 4;
//    int smallClusterMod = 4;
    float dagEps;// = 288; // Dag previous 177.63
    float linearEps;// = 380; // Linear 248
    float treeEps;// = 364; // Tree 202.78
//    float monotonicLinearEps = 312F;
//    float monotonicDagEps = 268;
//    float monotonicTreeEps = 328F;
    float monotonicLinearEps;// = 40.64F; // Monotonic 312
    float monotonicDagEps;// = 89.88F; // Monotonic 268
    float monotonicTreeEps;// = 23.45F; // Monotonic 328
    int TF_size;
    int TF_increase;
    float TF_qnt;
    int TT_size;
    int TT_increase;
    float TT_qnt;
    int FT_size;
    int FT_increase;
    float FT_qnt;
    
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
        TF_size = 5;
        TT_size = 5;
        FT_size = 5;
        
        linearEps = 1.0f;//142;
        dagEps = 1.1f;//190;
        treeEps = 1.3f;//190;
        monotonicLinearEps = 0.2f;//26;
        monotonicDagEps = 0.2f;//22;
        monotonicTreeEps = 0.2f;//18;
//        train();
//        runExperiment();
//        trainForExperiment();
    }
    
    private void runExperiment() throws InterruptedException {
        normalGraph();
        monotonicGraph();
    }

    private void train() throws IOException, InterruptedException {
//        trainDBSCAN();
        trainSimilarity();
    }

    private void trainForExperiment() throws IOException, InterruptedException {
        boolean isMonotonic = false;
        
        // Train
//        Trainer trainer = new Trainer();
//        trainer.setMonotonic(isMonotonic);
        String graphType = "Dag";
//        int iterations = 1;
//        int numberOracles = 10;
//        int numberNoiseGraphs = 5;
        int initialNoise = 10;
//        int noiseIncrease = 1;
//        System.out.println("DBSCAN");
//        float epsilon = trainer.trainDBSCAN(oracle, iterations, numberOracles, numberNoiseGraphs,  initialNoise, noiseIncrease, graphType);
//        System.out.println("True True");
//        trainer.trainSimilarity(true, true, oracle, iterations, numberOracles, numberOracles, numberNoiseGraphs, noiseIncrease, graphType);
//        System.out.println("True False");
//        trainer.trainSimilarity(true, false, oracle, iterations, numberOracles, numberNoiseGraphs, initialNoise, noiseIncrease, graphType);
//        System.out.println("False True");
//        trainer.trainSimilarity(false, true, oracle, iterations, numberOracles, numberNoiseGraphs, initialNoise, noiseIncrease, graphType);
        
        // Generate the experiment graph
        DirectedGraph<Object, Edge> oracleGraph = eval.oracleGraph.createOracleGraph(graphType);
        NoiseGraph instance = new NoiseGraph(oracleGraph, "A", isMonotonic);
        DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(initialNoise, 1.0F, "");
        Utils.exportGraph(noiseGraph, "Experiment_graph_" + graphType + "_" + "01");
        instance = new NoiseGraph(oracleGraph, "A", isMonotonic);
        noiseGraph = instance.generateNoiseGraph(initialNoise, 1.0F, "");
        Utils.exportGraph(noiseGraph, "Experiment_graph_" + graphType + "_" + "02");
        instance = new NoiseGraph(oracleGraph, "A", isMonotonic);
        noiseGraph = instance.generateNoiseGraph(initialNoise, 1.0F, "");
        Utils.exportGraph(noiseGraph, "Experiment_graph_" + graphType + "_" + "03");
        instance = new NoiseGraph(oracleGraph, "A", isMonotonic);
        noiseGraph = instance.generateNoiseGraph(initialNoise, 1.0F, "");
        Utils.exportGraph(noiseGraph, "Experiment_graph_" + graphType + "_" + "04");
        instance = new NoiseGraph(oracleGraph, "A", isMonotonic);
        noiseGraph = instance.generateNoiseGraph(initialNoise, 1.0F, "");
        Utils.exportGraph(noiseGraph, "Experiment_graph_" + graphType + "_" + "05");
    }
    
    
    private void trainDBSCAN() throws IOException {
        trainDBSCANRandom();
        trainDBSCANMonotonic();
    }

    private void trainDBSCANMonotonic() throws IOException {
        Trainer trainer = new Trainer();
        trainer.setMonotonic(true);
        monotonicLinearEps = trainer.trainDBSCAN(oracle, "Linear");
        monotonicDagEps = trainer.trainDBSCAN(oracle, "DAG");
        monotonicTreeEps = trainer.trainDBSCAN(oracle, "TREE");
        System.out.println("Best monotonicLinear: " + monotonicLinearEps);
        System.out.println("Best monotonicDAG: " + monotonicDagEps);
        System.out.println("Best monotonicTREE: " + monotonicTreeEps);
    }

    private void trainDBSCANRandom() throws IOException {
        Trainer trainer = new Trainer();
        trainer.setMonotonic(false);
        linearEps = trainer.trainDBSCAN(oracle, "Linear");
        dagEps = trainer.trainDBSCAN(oracle, "DAG");
        treeEps = trainer.trainDBSCAN(oracle, "TREE");

        System.out.println("Best Linear: " + linearEps);
        System.out.println("Best DAG: " + dagEps);
        System.out.println("Best TREE: " + treeEps);
    }

    private void trainSimilarity() throws IOException, InterruptedException {
        trainSimilarityRandom();
        trainSimilarityMonotonic();
        
    }

    private void trainSimilarityMonotonic() throws IOException, InterruptedException {
        Trainer trainer = new Trainer();
        System.out.println("======Similarity Training MONOTONIC===========");
        trainer.setMonotonic(true);
        System.out.println("True FALSE Monotonic");
        System.out.println("Linear");
        linearEps = trainer.trainSimilarity(true, false, oracle, "Linear", monotonicLinearEps);
        System.out.println("Dag");
        dagEps = trainer.trainSimilarity(true, false, oracle, "DAG", monotonicDagEps);
        System.out.println("Tree");
        treeEps = trainer.trainSimilarity(true, false, oracle, "TREE", monotonicTreeEps);

        trainer = new Trainer();
        trainer.setMonotonic(true);
        System.out.println("True True Monotonic");
        System.out.println("Linear");
        trainer.trainSimilarity(true, true, oracle, "Linear", monotonicLinearEps);
        System.out.println("Dag");
        trainer.trainSimilarity(true, true, oracle, "DAG", monotonicDagEps);
        System.out.println("Tree");
        trainer.trainSimilarity(true, true, oracle, "TREE", monotonicTreeEps);

        trainer = new Trainer();
        trainer.setMonotonic(true);
        System.out.println("False True Monotonic");
        System.out.println("Linear");
        trainer.trainSimilarity(false, true, oracle, "Linear", monotonicLinearEps);
        System.out.println("Dag");
        trainer.trainSimilarity(false, true, oracle, "DAG", monotonicDagEps);
        System.out.println("Tree");
        trainer.trainSimilarity(false, true, oracle, "TREE", monotonicTreeEps);
    }

    private void trainSimilarityRandom() throws IOException, InterruptedException {
        Trainer trainer = new Trainer();
        System.out.println("======Similarity Training RANDOM===========");
//        trainer = new Trainer();
//        trainer.setMonotonic(false);
//        System.out.println("True FALSE Normal");
//        System.out.println("Linear");
//        linearEps = trainer.trainSimilarity(true, false, oracle, "Linear", linearEps);
//        System.out.println("Dag");
//        dagEps = trainer.trainSimilarity(true, false, oracle, "DAG", dagEps);
//        System.out.println("Tree");
//        treeEps = trainer.trainSimilarity(true, false, oracle, "TREE", treeEps);
        
        trainer = new Trainer();
        trainer.setMonotonic(false);
        System.out.println("True True Normal");
//        System.out.println("Linear");
//        trainer.trainSimilarity(true, true, oracle, "Linear", linearEps);
//        System.out.println("Dag");
//        trainer.trainSimilarity(true, true, oracle, "DAG", dagEps);
        System.out.println("Tree");
        trainer.trainSimilarity(true, true, oracle, "TREE", treeEps);
        
        trainer = new Trainer();
        trainer.setMonotonic(false);
        System.out.println("Linear");
        System.out.println("False True Normal");
        trainer.trainSimilarity(false, true, oracle, "Linear", linearEps);
        System.out.println("Dag");
        trainer.trainSimilarity(false, true, oracle, "DAG", dagEps);
        System.out.println("Tree");
        trainer.trainSimilarity(false, true, oracle, "TREE", treeEps);
    }

    private void normalGraph() throws InterruptedException {
         try {
             System.out.println("Normal Graphs");
             eval = new ClusteringEvaluator(false, oracle);
             
             TF_increase = 7;
             TF_qnt = 3.7f;
             TT_increase = 6;
             TT_qnt = 5.3f;
             FT_increase = 1;
             FT_qnt = 2.3f;
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Linear", "Linear", linearEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
             
             eval = new ClusteringEvaluator(false, oracle);

             TF_increase = 7;
             TF_qnt = 3.7f;
             TT_increase = 2;
             TT_qnt = 5.1f;
             FT_increase = 1;
             FT_qnt = 2.5f;
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "DAG", "DAG", dagEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
             
             eval = new ClusteringEvaluator(false, oracle);

             TF_increase = 6;
             TF_qnt = 3.5f;
             TT_increase = 5;
             TT_qnt = 5.1f;
             FT_increase = 1;
             FT_qnt = 2.5f;
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "TREE", "TREE", treeEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
         } catch (IOException ex) {
             Logger.getLogger(ClusteringEvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    private void monotonicGraph() throws InterruptedException {
         try {
             System.out.println("Partial-Monotonic Graphs");
             // Monotonic
             eval = new ClusteringEvaluator(true, oracle);

             TF_increase = 7;
             TF_qnt = 4.9f;
             TT_increase = 7;
             TT_qnt = 6.7f;
             FT_increase = 1;
             FT_qnt = 0.5f;
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_Linear", "Linear", monotonicLinearEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
//             
             eval = new ClusteringEvaluator(true, oracle);

             TF_increase = 5;
             TF_qnt = 6.3f;
             TT_increase = 6;
             TT_qnt = 6.5f;
             FT_increase = 1;
             FT_qnt = 0.5f;
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_Dag", "DAG", monotonicDagEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
             
             eval = new ClusteringEvaluator(true, oracle);

             TF_increase = 5;
             TF_qnt = 5.7f;
             TT_increase = 6;
             TT_qnt = 6.5f;
             FT_increase = 1;
             FT_qnt = 0.5f; // Training yet
             eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_TREE", "TREE", monotonicTreeEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
         } catch (IOException ex) {
             Logger.getLogger(ClusteringEvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    
    
}
