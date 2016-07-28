/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.IO.XMLWriter;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class NoiseGraphTest {
    
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
        float noiseFactor = 3.0F;
        float noiseProbability = 0.7F;
        String attribute = "A";
        DirectedGraph<Object, Edge> templateGraph = generateTemplateGraph();
        NoiseGraph instance = new NoiseGraph(templateGraph, attribute);
        instance.generateNoiseGraph(noiseFactor, noiseProbability);
//        DirectedGraph<Object, Edge> expResult = null;
//        DirectedGraph<Object, Edge> result = instance.generateNoiseGraph(noiseFactor, noiseProbability);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
    private DirectedGraph<Object, Edge> generateTemplateGraph() {
        Vertex v1 = new ActivityVertex("v1", "v1", "0");
        Vertex v2 = new ActivityVertex("v2", "v2", "1");
        Vertex v3 = new ActivityVertex("v3", "v3", "2");
        Vertex v4 = new ActivityVertex("v4", "v4", "3");
        Vertex v5 = new ActivityVertex("v5", "v5", "4");
        Vertex v6 = new ActivityVertex("v6", "v6", "4");
        Vertex v7 = new ActivityVertex("v7", "v7", "4");
        Vertex v8 = new ActivityVertex("v8", "v8", "4");
        Vertex v9 = new ActivityVertex("v9", "v9", "4");
        Vertex v10 = new ActivityVertex("v10", "v10", "4");
        
        GraphAttribute av1;
        GraphAttribute av2;
        GraphAttribute av3;
        GraphAttribute av4;
        GraphAttribute av5;
        GraphAttribute av6;
        GraphAttribute av7;
        GraphAttribute av8;
        GraphAttribute av9;
        GraphAttribute av10;
        
        av1 = new GraphAttribute("A", "0");
        av2 = new GraphAttribute("A", "5");
        av3 = new GraphAttribute("A", "10");
        av4 = new GraphAttribute("A", "15");
        av5 = new GraphAttribute("A", "20");
        av6 = new GraphAttribute("A", "25");
        av7 = new GraphAttribute("A", "35");
        av8 = new GraphAttribute("A", "45");
        av9 = new GraphAttribute("A", "55");
        av10 = new GraphAttribute("A", "65");
        
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        v3.addAttribute(av3);
        v4.addAttribute(av4);
        v5.addAttribute(av5);
        v6.addAttribute(av6);
        v7.addAttribute(av7);
        v8.addAttribute(av8);
        v9.addAttribute(av9);
        v10.addAttribute(av10);
        
        av1 = new GraphAttribute("isTemplate", "yes");
        av2 = new GraphAttribute("isTemplate", "yes");
        av3 = new GraphAttribute("isTemplate", "yes");
        av4 = new GraphAttribute("isTemplate", "yes");
        av5 = new GraphAttribute("isTemplate", "yes");
        av6 = new GraphAttribute("isTemplate", "yes");
        av7 = new GraphAttribute("isTemplate", "yes");
        av8 = new GraphAttribute("isTemplate", "yes");
        av9 = new GraphAttribute("isTemplate", "yes");
        av10 = new GraphAttribute("isTemplate", "yes");
        
        v1.addAttribute(av1);
        v2.addAttribute(av2);
        v3.addAttribute(av3);
        v4.addAttribute(av4);
        v5.addAttribute(av5);
        v6.addAttribute(av6);
        v7.addAttribute(av7);
        v8.addAttribute(av8);
        v9.addAttribute(av9);
        v10.addAttribute(av10);
        
        Edge e1 = new Edge("E1", "", "", "", v1, v2);
        Edge e2 = new Edge("E2", "", "", "", v2, v3);
        Edge e3 = new Edge("E3", "", "", "", v3, v4);
        Edge e4 = new Edge("E4", "", "", "", v4, v5);
        Edge e5 = new Edge("E5", "", "", "", v5, v6);
        Edge e6 = new Edge("E6", "", "", "", v6, v7);
        Edge e7 = new Edge("E7", "", "", "", v7, v8);
        Edge e8 = new Edge("E8", "", "", "", v8, v9);
        Edge e9 = new Edge("E9", "", "", "", v9, v10);
        
        DirectedGraph<Object, Edge> templateGraph = new DirectedSparseMultigraph<>();
        templateGraph.addEdge(e1, e1.getSource(), e1.getTarget());
        templateGraph.addEdge(e2, e2.getSource(), e2.getTarget());
        templateGraph.addEdge(e3, e3.getSource(), e3.getTarget());
        templateGraph.addEdge(e4, e4.getSource(), e4.getTarget());
        templateGraph.addEdge(e5, e5.getSource(), e5.getTarget());
        templateGraph.addEdge(e6, e6.getSource(), e6.getTarget());
        templateGraph.addEdge(e7, e7.getSource(), e7.getTarget());
        templateGraph.addEdge(e8, e8.getSource(), e8.getTarget());
        templateGraph.addEdge(e9, e9.getSource(), e9.getTarget());
        Utils.exportGraph(templateGraph, "templateGraph");
        
        return templateGraph;
    }
    
}
