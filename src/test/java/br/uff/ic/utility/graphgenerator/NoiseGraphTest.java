/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

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
//        DirectedGraph<Object, Edge> templateGraph = oracle.generateLinearGraph();
//        NoiseGraph instance = new NoiseGraph(templateGraph, attribute);
//        instance.generateNoiseGraph(noiseFactor, noiseProbability);
        try {
            eval.collapse(oracle, 50, 100, 2, 1);
//        DirectedGraph<Object, Edge> expResult = null;
//        DirectedGraph<Object, Edge> result = instance.generateNoiseGraph(noiseFactor, noiseProbability);
//        assertEquals(expResult, result);
// TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
        } catch (IOException ex) {
            Logger.getLogger(NoiseGraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
