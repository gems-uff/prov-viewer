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
        ClusteringEvaluator eval = new ClusteringEvaluator();
        int NUMBER_OF_ORACLE_GRAPHS = 50;
        int NUMBER_OF_NOISE_GRAPHS = 100;
        double INITIAL_NOISE_GRAPH_SIZE = 2;
        double NOISE_INCREASE_NUMBER = 1;

        try {
            File file;
            double dagEps = 177.63;
            double linearEps = 199.23;
            double treeEps = 202.78;
//            linearEps = eval.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "Linear");
//            dagEps = eval.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "DAG");
//            treeEps = eval.trainDBSCAN(oracle, NUMBER_OF_ORACLE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, "TREE");
//          
            file = new File("LinearEvaluation.txt");
            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, file, "Linear", linearEps);
//            
//            file = new File("DAGEvaluation.txt");
//            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, file, "DAG", dagEps);
//            
//            file = new File("TreeEvaluation.txt");
//            eval.collapse(oracle, NUMBER_OF_ORACLE_GRAPHS, NUMBER_OF_NOISE_GRAPHS, INITIAL_NOISE_GRAPH_SIZE, NOISE_INCREASE_NUMBER, file, "TREE", treeEps);

        } catch (IOException ex) {
            Logger.getLogger(NoiseGraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
