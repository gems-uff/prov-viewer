/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.graphmatching;

import br.uff.ic.utility.GraphAttribute;
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
        Map<String, GraphAttribute> restrictionList = new HashMap<String, GraphAttribute>();
        GraphAttribute av1 = new GraphAttribute("a1", a1);
        GraphAttribute av2 = new GraphAttribute("a1", a2);
        GraphAttribute epsilon = new GraphAttribute("a1", e1);
        restrictionList.put("a1", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a2", "1.5");
        av2 = new GraphAttribute("a2", "2");
        epsilon = new GraphAttribute("a2", e2);
        restrictionList.put("a2", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a3", "3");
        av2 = new GraphAttribute("a3", "1.5");
        epsilon = new GraphAttribute("a3", e3);
        restrictionList.put("a3", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a4", "1");
        av2 = new GraphAttribute("a4", "-2");
        epsilon = new GraphAttribute("a4", e4);
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
        Map<String, GraphAttribute> restrictionList = new HashMap<String, GraphAttribute>();
        GraphAttribute av1 = new GraphAttribute("a1", "0");
        GraphAttribute av2 = new GraphAttribute("a1", "0");
        GraphAttribute epsilon = new GraphAttribute("a1", "0");
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
        Map<String, GraphAttribute> restrictionList = new HashMap<String, GraphAttribute>();
        GraphAttribute av1 = new GraphAttribute("a1", "0");
        GraphAttribute av2 = new GraphAttribute("a1", "0");
        GraphAttribute epsilon = new GraphAttribute("a1", "0");
        restrictionList.put("a1", epsilon);
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a2", "1.5");
        v1.addAttribute(av1);
        
        av2 = new GraphAttribute("a3", "1.5");
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a4", "1");
        av2 = new GraphAttribute("a4", "1");
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        GraphMatching instance = new GraphMatching(restrictionList, threshold);
        boolean result = instance.isSimilar(v1, v2);
        assertEquals(expResult, result);
    }

    /**
     * Test of combineVertices method, of class GraphMatching.
     */
    @Test
    public void testCombineVertices() {
        System.out.println("combineVertices");
        
        Vertex v1 = new ActivityVertex("v1", "test01", "0");
        Vertex v2 = new ActivityVertex("v2", "test02", "0");
        
        GraphAttribute av1;
        GraphAttribute av2;
        
        av1 = new GraphAttribute("a1", "Asd");
        av2 = new GraphAttribute("a1", "edf");
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a2", "4");
        av2 = new GraphAttribute("a2", "2");
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a3", "3");
        av2 = new GraphAttribute("a3", "1");
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a4", "2");
        av2 = new GraphAttribute("a4", "-2");
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        av1 = new GraphAttribute("a5", "5");
        v1.addAttribute(av1);
        
        av2 = new GraphAttribute("a6", "6");
        v2.addAttribute(av2);
        
        GraphMatching instance = new GraphMatching(null, 0.0);
        
        Vertex expResult = new ActivityVertex(v1.getID() + "," + v2.getID(), v1.getLabel() + "," + v2.getLabel(), v1.getTimeString());
        GraphAttribute aResult;
        
        aResult = new GraphAttribute("a1", "Asd, edf");
        aResult.incrementQuantity();
        expResult.addAttribute(aResult);
        
        aResult = new GraphAttribute("a2", "6");
        aResult.incrementQuantity();
        aResult.setMin(2);
        aResult.setMax(4);
        expResult.addAttribute(aResult);
        
        aResult = new GraphAttribute("a3", "4");
        aResult.incrementQuantity();
        aResult.setMin(1);
        aResult.setMax(3);
        expResult.addAttribute(aResult);
        
        aResult = new GraphAttribute("a4", "0");
        aResult.incrementQuantity();
        aResult.setMin(-2);
        aResult.setMax(2);
        expResult.addAttribute(aResult);
        
        aResult = new GraphAttribute("a5", "5");
        expResult.addAttribute(aResult);
        
        aResult = new GraphAttribute("a6", "6");
        expResult.addAttribute(aResult);
        
        Vertex result = instance.combineVertices(v1, v2);
        assertEquals(expResult.toString(), result.toString());
    }
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
    /**
     * Test of updateEdges method, of class GraphMatching.
     */
    @Test
    public void testUpdateEdges() {
        System.out.println("updateEdges");
        Collection<Edge> edges = new ArrayList<Edge>();
        GraphMatching instance = new GraphMatching(null, 0.0);
        
        Vertex v1 = new ActivityVertex("v1", "test01", "0");
        Vertex v2 = new ActivityVertex("v2", "test02", "0");
        Vertex source = new ActivityVertex("v3", "test03", "0");
        Vertex target = new ActivityVertex("v4", "test04", "0");
        
        Edge edge = new Edge("edge01", "Test", "Testing", "0", v1, source);
        edges.add(edge);
        edge = new Edge("edge02", "Test", "Testing", "0", target, v2);
        edges.add(edge);
        
        GraphAttribute av1;
        GraphAttribute av2;
        
        av1 = new GraphAttribute("a1", "0");
        av2 = new GraphAttribute("a1", "0");
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        
        Vertex updatedVertex = instance.combineVertices(v1, v2);
        
        Map<String, Edge> expResult = new HashMap<String, Edge>();
        Edge updatedEdge = new Edge("edge01", "Test", "Testing", "0", updatedVertex, source);
        expResult.put(updatedEdge.getID(), updatedEdge);
        
        updatedEdge = new Edge("edge02", "Test", "Testing", "0", target, updatedVertex);
        expResult.put(updatedEdge.getID(), updatedEdge);
        
        Collection<Edge> result = instance.updateEdges(edges);
        
        Edge result01 = ((Edge) result.toArray()[0]);
        Edge expResult01 = ((Edge) expResult.values().toArray()[1]);
        Edge result02 = ((Edge) result.toArray()[1]);
        Edge expResult02 = ((Edge) expResult.values().toArray()[0]);
        
        String result01Target = result01.getTarget().getID();
        String expResult01Target = expResult01.getTarget().getID();
        String result01Source = result01.getSource().getID();
        String expResult01Source = expResult01.getSource().getID();
        
        String result02Target = result02.getTarget().getID();
        String expResult02Target = expResult02.getTarget().getID();
        String result02Source = result02.getSource().getID();
        String expResult02Source = expResult02.getSource().getID();
        
//        System.out.println("Target: " + result01Target);
//        System.out.println("Exp Target: " + expResult01Target);
//        System.out.println("Source: " + result01Source);
//        System.out.println("Exp Source: " + expResult01Source);
//        System.out.println("Target: " + result02Target);
//        System.out.println("Exp Target: " + expResult02Target);
//        System.out.println("Source: " + result02Source);
//        System.out.println("Exp Source: " + expResult02Source);
        
        
        assertEquals(result01Target, expResult01Target);
        assertEquals(result01Source, expResult01Source);
        assertEquals(result02Target, expResult02Target);
        assertEquals(result02Source, expResult02Source);
    }
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
