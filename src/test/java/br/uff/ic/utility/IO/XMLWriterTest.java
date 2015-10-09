/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.IO;

import br.uff.ic.provviewer.GUI.GuiRun;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kohwalter
 */
public class XMLWriterTest {
    
    public XMLWriterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of saveToXML method, of class XMLWriter.
     */
    @Test
    public void testSaveToXML() throws Exception {
        System.out.println("saveToXML");
        System.out.println("File: " + Variables.demo);
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        UnityReader file = new UnityReader(f);
        file.readFile();
        DirectedGraph<Vertex, Edge> g = new DirectedSparseMultigraph<Vertex, Edge>();
        
        for (Edge edge : file.getEdges()) {
                g.addEdge(edge, edge.getSource(), edge.getTarget());
            }
            
        XMLWriter xmlWriter = new XMLWriter(g.getVertices(), g.getEdges());
        xmlWriter.saveToXML("XML_Writer_Test");
    }
    
}
