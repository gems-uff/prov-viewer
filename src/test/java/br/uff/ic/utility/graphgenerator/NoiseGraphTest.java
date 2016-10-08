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
    public void testGenerateNoiseGraph() throws InterruptedException {
        System.out.println("generateNoiseGraph");
        OracleGraph oracle = new OracleGraph(attribute, -200, 200);
        ClusteringEvaluator eval = new ClusteringEvaluator(false, oracle);
        int NUMBER_OF_ORACLE_GRAPHS = 1;
        int NUMBER_OF_NOISE_GRAPHS = 1;
        double INITIAL_NOISE_GRAPH_SIZE = 10;
        double NOISE_INCREASE_NUMBER = 2;
        int NUMBER_ITERATIONS = 10;

        try {
            File file;
            File fileR;
            Trainer trainer = new Trainer();
            int smallCluster = 7;
            int threshold = 4;
            int smallClusterMod = 4;
            double dagEps = 288; // Dag previous 177.63
            double linearEps = 380; // Linear 248
            double treeEps = 364; // Tree 202.78
            double monotonicLinearEps = 40.64; // Monotonic 312
            double monotonicDagEps = 89.88; // Monotonic 268
            double monotonicTreeEps = 23.45; // Monotonic 328
            int TF_size; 
            int TF_increase; 
            int TF_qnt; 
            int TT_size; 
            int TT_increase; 
            int TT_qnt; 
            int FT_size; 
            int FT_increase; 
            int FT_qnt;
            
//            Main main = new Main();
//            main.main();
            
//            linearEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "Linear");
//            trainer.setMonotonic(true);
//            dagEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "DAG");
//            treeEps = trainer.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "TREE");
//            System.out.println("Best DAG: " + dagEps);
//            System.out.println("Best TREE: " + treeEps);
            // TODO
            // Train the similarity algorithm to determine the minimum size of clusters
//            trainer.setMonotonic(true);
//            System.out.println("True FALSE Monotonic");
//            linearEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "Linear");
//            dagEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "DAG");
//            treeEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "TREE");
//            trainer = new Trainer();
//            trainer.setMonotonic(false);
//            System.out.println("True FALSE Normal");
//            linearEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "Linear");
//            dagEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "DAG");
//            treeEps = trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "TREE");
//            
//            trainer = new Trainer();
//            trainer.setMonotonic(true);
//            System.out.println("True True Monotonic");
//            trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 4, "Linear");
//            trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 4, "DAG");
//            trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 4, "TREE");
//            trainer = new Trainer();
//            trainer.setMonotonic(false);
//            System.out.println("True True Normal");
//            trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 4, "Linear");
//            trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 4, "DAG");
//            trainer.trainSimilarity(true, true, oracle, 3, 3, 10, 4, "TREE");
//            
//            trainer = new Trainer();
//            trainer.setMonotonic(true);
//            System.out.println("Fase True Monotonic");
//            trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "Linear");
//            trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "DAG");
//            trainer.trainSimilarity(true, false, oracle, 3, 3, 10, 4, "TREE");
//            trainer = new Trainer();
//            trainer.setMonotonic(false);
//            System.out.println("False True Normal");
//            trainer.trainSimilarity(false, true, oracle, 3, 3, 10, 4, "Linear");
//            trainer.trainSimilarity(false, true, oracle, 3, 3, 10, 4, "DAG");
//            trainer.trainSimilarity(false, true, oracle, 3, 3, 10, 4, "TREE");
//            System.out.println("Best Linear: " + linearEps);
//            System.out.println("Best DAG: " + dagEps);
//            System.out.println("Best TREE: " + treeEps);

            eval = new ClusteringEvaluator(false, oracle);
            TF_size = 7; 
            TF_increase = 4; 
            TF_qnt = 4; 
            TT_size = 7; 
            TT_increase = 4; 
            TT_qnt = 5; 
            FT_size = 1; 
            FT_increase = 1; 
            FT_qnt = 3;
            eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS,  "Linear", "Linear", linearEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
            
//            eval = new ClusteringEvaluator(false, oracle);
//            TF_size = 5; 
//            TF_increase = 3; 
//            TF_qnt = 5; 
//            TT_size = 7; 
//            TT_increase = 3; 
//            TT_qnt = 5; 
//            FT_size = 1; 
//            FT_increase = 1; 
//            FT_qnt = 3;
//            eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "DAG", "DAG", dagEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
//            
//            eval = new ClusteringEvaluator(false, oracle);
//            TF_size = 7; 
//            TF_increase = 3; 
//            TF_qnt = 4; 
//            TT_size = 7; 
//            TT_increase = 2; 
//            TT_qnt = 5; 
//            FT_size = 1; 
//            FT_increase = 1; 
//            FT_qnt = 3;
//            eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "TREE", "TREE", treeEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
//            
//             // Monotonic
//            eval = new ClusteringEvaluator(true, oracle);
//            TF_size = 7; 
//            TF_increase = 3; 
//            TF_qnt = 5; 
//            TT_size = 7; 
//            TT_increase = 4; 
//            TT_qnt = 5; 
//            FT_size = 7; 
//            FT_increase = 4; 
//            FT_qnt = 5;
//            eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS,  "Monotonic_Linear", "Linear", monotonicLinearEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
////            
//            eval = new ClusteringEvaluator(true, oracle);
//            TF_size = 7; 
//            TF_increase = 3; 
//            TF_qnt = 5; 
//            TT_size = 7; 
//            TT_increase = 4; 
//            TT_qnt = 5; 
//            FT_size = 7; 
//            FT_increase = 3; 
//            FT_qnt = 5;
//            eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_Dag", "DAG", monotonicDagEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
////            
//            eval = new ClusteringEvaluator(true, oracle);
//            TF_size = 7; 
//            TF_increase = 2; 
//            TF_qnt = 6;
//            TT_size = 7; 
//            TT_increase = 3; 
//            TT_qnt = 5; 
//            FT_size = 5; 
//            FT_increase = 4; 
//            FT_qnt = 5;
//            eval.collapse(NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, NUMBER_ITERATIONS, "Monotonic_TREE", "TREE", monotonicTreeEps, TF_size, TF_increase, TF_qnt, TT_size, TT_increase, TT_qnt, FT_size, FT_increase, FT_qnt);
            
        } catch (IOException ex) {
            Logger.getLogger(NoiseGraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
