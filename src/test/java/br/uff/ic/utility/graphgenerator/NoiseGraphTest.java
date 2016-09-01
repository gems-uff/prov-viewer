/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kohwalter
 */
public class NoiseGraphTest {
    
    
    String attribute = "A";
    
    public NoiseGraphTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of generateNoiseGraph method, of class NoiseGraph.
     */
    @Test
    public void testGenerateNoiseGraph() {
        System.out.println("generateNoiseGraph");
        OracleGraph oracle = new OracleGraph(attribute, -200, 200);
        ClusteringEvaluator eval = new ClusteringEvaluator(false);
        int NUMBER_OF_ORACLE_GRAPHS = 10;
        int NUMBER_OF_NOISE_GRAPHS = 10;
        double INITIAL_NOISE_GRAPH_SIZE = 10;
        double NOISE_INCREASE_NUMBER = 2;
        int NUMBER_ITERATIONS = 4;

        try {
            File file;
            Trainer trainer = new Trainer();
            int smallCluster = 7;
            int threshold = 4;
            int smallClusterMod = 4;
            double dagEps = 177.63; // Linear
            double linearEps = 380; // Linear 248
            double treeEps = 202.78; // Linear
            double monotonicLinearEps = 40.64; // Monotonic
            double monotonicDagEps = 89.88; // Monotonic
            double monotonicTreeEps = 23.45; // Monotonic
//            linearEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "Linear");
//            dagEps = eval.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS * 2, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "DAG");
//            treeEps = eval.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS * 2, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "TREE");
            // TODO
            // Train the similarity algorithm to determine the minimum size of clusters
            
//            linearEps = trainer.trainSimilarity(oracle, 3, 3, 10, 4, "Linear");

            eval = new ClusteringEvaluator(false);
            file = new File("EvaluationLinear.txt");
            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS,  file, "Linear", linearEps, smallCluster, smallClusterMod, threshold);
            
//            eval = new ClusteringEvaluator(false);
//            file = new File("EvaluationDAG.txt");
//            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, file, "DAG", dagEps, smallCluster, smallClusterMod, threshold);
//            
//            eval = new ClusteringEvaluator(false);
//            file = new File("EvaluationTree.txt");
//            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, file, "TREE", treeEps, smallCluster, smallClusterMod, threshold);
//            
//            eval = new ClusteringEvaluator(true);
//            file = new File("MonotonicEvaluationLinear.txt");
//            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS,  file, "Linear", monotonicLinearEps, smallCluster, smallClusterMod, threshold);
//            
//            eval = new ClusteringEvaluator(true);
//            file = new File("MonotonicEvaluationDAG.txt");
//            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, file, "DAG", monotonicDagEps, smallCluster, smallClusterMod, threshold);
//            
//            eval = new ClusteringEvaluator(true);
//            file = new File("MonotonicEvaluationTree.txt");
//            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, file, "TREE", monotonicTreeEps, smallCluster, smallClusterMod, threshold);
            
        } catch (IOException ex) {
            Logger.getLogger(NoiseGraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
