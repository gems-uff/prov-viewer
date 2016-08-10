/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import br.uff.ic.utility.graphgenerator.OracleGraph;
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
public class dbscanTest {
    
    String attribute = "A";
    double e = 200;
    int minpt = 1;
    
    public dbscanTest() {
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
     * Test of applyDbscan method, of class dbscan.
     */
    @Test
    public void testApplyDbscan() {
        System.out.println("applyDbscan");
        OracleGraph oracle;
        oracle = new OracleGraph(attribute, -200, 200);
        Dbscan instance = new Dbscan(oracle.generateLinearGraph(), attribute, e, minpt);
//        String expResult = "";
        String result = instance.applyDbscan();
        instance.printClusters(result);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
