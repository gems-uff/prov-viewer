/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.utility.Attribute;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class GraphMatchingTest {
    
    public GraphMatchingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of isSimilar method, of class GraphMatching.
     */
    @Test
    public void testIsSimilar() {
        System.out.println("isSimilar");
        equals(1.00, true);
        almostEquals(0.75, true);
        halfEquals(0.5, true);
        quarterEquals(0.25, true);
        notEquals(0.00, true);
        
        equals(0.75, true);
        almostEquals(0.85, false);
        halfEquals(0.75, false);
        quarterEquals(0.5, false);
        notEquals(0.10, false);
        
        differentTypes(1.00, false);
        differentAttributes(0.5, true);
        differentAttributes(0.51, false);
    }
    
    public void Comparing(double threshold, boolean expResult, String a1, String a2, String e1, String e2, String e3, String e4) {
        Vertex v1 = new ActivityVertex("v1", "test", "0");
        Vertex v2 = new ActivityVertex("v2", "test", "0");
        Map<String, Attribute> restrictionList = new HashMap<String, Attribute>();
        Attribute av1 = new Attribute("a1", a1);
        Attribute av2 = new Attribute("a1", a2);
        Attribute epsilon = new Attribute("a1", e1);
        restrictionList.put("a1", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new Attribute("a2", "1.5");
        av2 = new Attribute("a2", "2");
        epsilon = new Attribute("a2", e2);
        restrictionList.put("a2", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new Attribute("a3", "3");
        av2 = new Attribute("a3", "1.5");
        epsilon = new Attribute("a3", e3);
        restrictionList.put("a3", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new Attribute("a4", "1");
        av2 = new Attribute("a4", "-2");
        epsilon = new Attribute("a4", e4);
        restrictionList.put("a4", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        GraphMatching instance = new GraphMatching(restrictionList, threshold);
        boolean result = instance.isSimilar(v1, v2);
        assertEquals(expResult, result);
    }
    
    public void equals(double threshold, boolean expResult) {
        System.out.println("Equal");
        Comparing(threshold, expResult, "asd", "AsD", "0", "0.5", "2", "3");
    }
    
    public void almostEquals(double threshold, boolean expResult) {
        System.out.println("AlmostEqual");
        Comparing(threshold, expResult, "asd", "AsD", "0", "0.5", "2", "0");
    }
    
    public void halfEquals(double threshold, boolean expResult) {
        System.out.println("HalfEqual");
        Comparing(threshold, expResult, "asd", "AsD", "0", "0.5", "0", "0");
    }
    
    public void quarterEquals(double threshold, boolean expResult) {
        System.out.println("notEqual");
        Comparing(threshold, expResult, "asd", "AsD", "0", "0", "0", "0");
    }
    
    public void notEquals(double threshold, boolean expResult) {
        System.out.println("notEqual");
        Comparing(threshold, expResult, "asd", "AsDD", "0", "0", "0", "0");
    }
    
    public void differentTypes(double threshold, boolean expResult) {
        System.out.println("Different Types");
        Vertex v1 = new ActivityVertex("v1", "test", "0");
        Vertex v2 = new EntityVertex("v2", "test", "0");
        Map<String, Attribute> restrictionList = new HashMap<String, Attribute>();
        Attribute av1 = new Attribute("a1", "0");
        Attribute av2 = new Attribute("a1", "0");
        Attribute epsilon = new Attribute("a1", "0");
        restrictionList.put("a1", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        GraphMatching instance = new GraphMatching(restrictionList, threshold);
        boolean result = instance.isSimilar(v1, v2);
        assertEquals(expResult, result);
    }
    
    public void differentAttributes(double threshold, boolean expResult) {
        System.out.println("Different Types");
        Vertex v1 = new ActivityVertex("v1", "test", "0");
        Vertex v2 = new ActivityVertex("v2", "test", "0");
        Map<String, Attribute> restrictionList = new HashMap<String, Attribute>();
        Attribute av1 = new Attribute("a1", "0");
        Attribute av2 = new Attribute("a1", "0");
        Attribute epsilon = new Attribute("a1", "0");
        restrictionList.put("a1", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new Attribute("a2", "1.5");
        v1.addAttribute(av1);
        
        av2 = new Attribute("a3", "1.5");
        v2.addAttribute(av2);
        
        av1 = new Attribute("a4", "1");
        av2 = new Attribute("a4", "1");
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        GraphMatching instance = new GraphMatching(restrictionList, threshold);
        boolean result = instance.isSimilar(v1, v2);
        assertEquals(expResult, result);
    }

//    /**
//     * Test of combineVertices method, of class GraphMatching.
//     */
//    @Test
//    public void testCombineVertices() {
//        System.out.println("combineVertices");
//        Vertex v1 = null;
//        Vertex v2 = null;
//        GraphMatching instance = new GraphMatching();
//        Vertex expResult = null;
//        Vertex result = instance.combineVertices(v1, v2);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addVertex method, of class GraphMatching.
//     */
//    @Test
//    public void testAddVertex() {
//        System.out.println("addVertex");
//        Vertex vertex = null;
//        GraphMatching instance = new GraphMatching();
//        instance.addVertex(vertex);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addVertices method, of class GraphMatching.
//     */
//    @Test
//    public void testAddVertices() {
//        System.out.println("addVertices");
//        Collection<Object> vertices = null;
//        GraphMatching instance = new GraphMatching();
//        instance.addVertices(vertices);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of updateEdges method, of class GraphMatching.
//     */
//    @Test
//    public void testUpdateEdges() {
//        System.out.println("updateEdges");
//        Collection<Edge> edges = null;
//        GraphMatching instance = new GraphMatching();
//        Collection<Edge> expResult = null;
//        Collection<Edge> result = instance.updateEdges(edges);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addEdges method, of class GraphMatching.
//     */
//    @Test
//    public void testAddEdges() {
//        System.out.println("addEdges");
//        Collection<Edge> edges = null;
//        GraphMatching instance = new GraphMatching();
//        instance.addEdges(edges);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of combineEdges method, of class GraphMatching.
//     */
//    @Test
//    public void testCombineEdges() {
//        System.out.println("combineEdges");
//        GraphMatching instance = new GraphMatching();
//        instance.combineEdges();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
